package com.globalside.codingchallenge.rbac.api;

import com.globalside.codingchallenge.rbac.model.ProductDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Declares the {@code /products} REST contract (routes and request/response shapes) separately
 * from the controller implementation, so the mapping annotations are documented in one place.
 */
public interface ProductApi {

    /**
     * Lists all products. Allowed for both {@code User} and {@code Admin} roles.
     * @return every product currently stored
     */
    @GetMapping("/products")
    List<ProductDto> getAllProducts();

    /**
     * Fetches a single product by id. Allowed for both {@code User} and {@code Admin} roles.
     */
    @GetMapping("/products/{id}")
    ProductDto getProductById(@PathVariable("id") Integer id);

    /**
     * Creates a new product. Restricted to the {@Admin} role.
     * 
     * @param productDto the product to create
     * @return the created product, including its generated id
     */
    @PostMapping("/products")
    ProductDto createProduct(@RequestBody ProductDto productDto);

    /**
     * Replaces an existing product's fields. Restricted to the {@code Admin} role.
     * 
     * @param id the id of the product to update
     * @param productDto the new field values
     * @return the updated product
     */
    @PutMapping("/products/{id}")
    ProductDto updateProduct(@PathVariable("id") Integer id, @RequestBody ProductDto productDto);

    /**
     * Deletes a product by id. Restricted to the {@code Admin} role.
     *  
     * @param id the id of the product to delete
     */
    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable("id") Integer id);
}
