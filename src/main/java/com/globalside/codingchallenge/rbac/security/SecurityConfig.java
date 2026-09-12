package com.globalside.codingchallenge.rbac.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.globalside.codingchallenge.rbac.web.ProductController;

/**
 * Configures HTTP Basic authentication, per-role URL access rules, and the two in-memory demo
 * users ({@code user}/{@code User} role and {@code admin}/{@code Admin} role) used by this
 * challenge. Also enables {@code @PreAuthorize}/{@code PostAuthorize} annotations on service
 * methods (e.g. {@code ProductService}), which is not on by default in Spring Security.
 */
@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity 
public class SecurityConfig {

    private final RestAccessDeniedHandler restAccessDeniedHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /**
     * @param restAccessDeniedHandler returns a JSON body on 403 responses
     * @param restAuthenticationEntryPoint returns a JSON body on 401 responses
     */
    public SecurityConfig(RestAccessDeniedHandler restAccessDeniedHandler,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint, ProductController productController) {
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    /**
     * Builds the HTTP security filter chain: disables CSRF/CORS (stateless Basic-auth API),
     * allows same-origin framing for the H2 console, applies the per-method role rules for
     * {@code /products/**}, and wires the JSON 401/403 handlers.
     * 
     * @param http the security configuration builder supplied by Spring Security
     * @return the configured filter chain
     * @throws Exception if the security configuration cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST API authenticated via HTTP Basic (no cookie-based session auth),
            // so CSRF protection has no session to protect and only blocks legitimate requests.
            .csrf(csrf -> csrf.disable())
            // Allow same-origin frame rendering for H2's internal frame layout
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth
                // H2 console is for local dev/debugging of the in-memory database; not access-controlled.
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                // Reads are open to both roles; writes (POST/PUT/DELETE) require Admin.
                // These URL-level rules are enforced by the security filter chain before a request
                // ever reaches the controller/service, mirroring the @PreAuthorize checks in ProductService.
                .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("User", "Admin")
                .requestMatchers(HttpMethod.POST, "/products/**").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("Admin")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(restAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
            );

        return http.build();
    }

    /**
     * Demo users held in memory (no user database) for exercising the {@code User} vs.
     * {@code Admin} roles
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("User")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("Admin")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * @return the BCrypt password encoder used to hash and verify demo user passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
