package com.globalside.codingchallenge.rbac;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalside.codingchallenge.rbac.model.ProductDto;

/**
 * End-to-end RBAC tests driving the real {@code /products} endpoints through MockMvc against an
 * in-memory H2 database, verifiying that role and authentication requirements are enforced as
 * configured in {@code SecurityConfig} and {@code ProductService}.
 */
@SpringBootTest
@AutoConfigureMockMvc 
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:rbac_challenge_test;DB_CLOSE_DELAY=-1")
class RbacChallengeApplicationTests {

    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;

    /**
     * Builds a {@code "Basic <base64(user:pass)>"} header value for HTTP Basic Auth requests.
     * 
     * @param username the account username
     * @param password the account password
     * @return the value to use for the {@code Authorization} header
     */
    private static String basicAuthHeader(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Serializes a sample {@link ProductDto} to JSON for use as a request body in create/update
     * calls.
     * 
     * @return the JSON representation of a sample product
     * @throws Exception if serialization fails
     */
    private String newProductJson() throws Exception {
        ProductDto product = new ProductDto();
        product.setName("Test Product");
        product.setDescription("A product used for testing");
        product.setPrice(9.99);
        product.setCurrency("USD");
        product.setCategory("Test Category");
        product.setBrand("Test Brand");
        product.setColor("Black");
        return objectMapper.writeValueAsString(product);
    }

    /** Sanity check that Spring application context starts up without errors. */
    @Test
    void contextLoads() {
    }

    /**
     * Verifies an admin user can perform every CRUD operation (create, read, update, delete) on
     * products.
     */
    @Test
    void adminUsersHaveFullAccessToAllCrudOperations() throws Exception {
        String adminAuth = basicAuthHeader("admin", "admin123");

        String createResponse = mockMvc.perform(post("/products")
                .header(HttpHeaders.AUTHORIZATION, adminAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDto created = objectMapper.readValue(createResponse, ProductDto.class);
        Integer id = created.getId();

        mockMvc.perform(get("/products")
                .header(HttpHeaders.AUTHORIZATION, adminAuth))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products/" + id)
                .header(HttpHeaders.AUTHORIZATION, adminAuth))
                .andExpect(status().isOk());

        created.setName("Updated Test Product");
        mockMvc.perform(put("/products/" + id)
                .header(HttpHeaders.AUTHORIZATION, adminAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/products/" + id)
                .header(HttpHeaders.AUTHORIZATION, adminAuth))
                .andExpect(status().isOk());
    }

    /**
     * Verifies a regular (non-admin) user can only read products, and is forbidden from
     * creating, updating, or deleting them.
     */
    @Test
    void regularUsersCanOnlyViewProducts() throws Exception {
        String adminAuth = basicAuthHeader("admin", "admin123");
        String userAuth = basicAuthHeader("user", "user123");

        // Admin creates a product up front so the regular user has something to read/mutate.
        String createResponse = mockMvc.perform(post("/products")
                .header(HttpHeaders.AUTHORIZATION, adminAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = objectMapper.readValue(createResponse, ProductDto.class).getId();

        // Regular user can list and view products.
        mockMvc.perform(get("/products")
                .header(HttpHeaders.AUTHORIZATION, userAuth))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products/" + id)
                .header(HttpHeaders.AUTHORIZATION, userAuth))
                .andExpect(status().isOk());

        // Regular user is forbidden from creating, updating or deleting products.
        mockMvc.perform(post("/products")
                .header(HttpHeaders.AUTHORIZATION, userAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/products/" + id)
                .header(HttpHeaders.AUTHORIZATION, userAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/products/" + id)
                .header(HttpHeaders.AUTHORIZATION, userAuth))
                .andExpect(status().isForbidden());
    }

    /**
     * Verifies that requests without credentials are rejected as unauthorized, and that an
     * authenticated but under-privileged user is rejected as forbidden rather than unauthorized.
     */
    @Test
    void unauthorizedUsersArePreventedFromAccessingRestrictedEndpoints() throws Exception {
        // No Authorization header on any of these reuqests -> 401 Unauthorized.
        mockMvc.perform(get("/products"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isUnauthorized());
            
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isUnauthorized());

        // Authenticated as a regular user, but lacking permission to create -> 403 Forbidden
        String userAuth = basicAuthHeader("user", "user123");
        mockMvc.perform(post("/products")
                .header(HttpHeaders.AUTHORIZATION, userAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isForbidden());
    }

    /** 
     * Verifies that requests with wrong credentials (a known username paired with an incorrect 
     * password, and a username that does not exist) are rejected as unauthorized.
    */
    @Test
    void wrongCredentialsAreRejectedAsUnauthorized() throws Exception {
        String wrongPasswordAuth = basicAuthHeader("admin", "wrongpassword");
        String unknownUserAuth = basicAuthHeader("nosuchuser", "whatever");

        mockMvc.perform(get("/products")
                .header(HttpHeaders.AUTHORIZATION, wrongPasswordAuth))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/products")
                .header(HttpHeaders.AUTHORIZATION, wrongPasswordAuth)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newProductJson()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/products")
                .header(HttpHeaders.AUTHORIZATION, unknownUserAuth))
                .andExpect(status().isUnauthorized());
    }
}
