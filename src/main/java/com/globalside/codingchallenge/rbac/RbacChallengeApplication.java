package com.globalside.codingchallenge.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Spring Boot application demonstrating role-based access control (RBAC)
 * on a simple product CRUD API.
 */
@SpringBootApplication
public class RbacChallengeApplication {

    /**
     * Boots the Spring application context.
     * 
     * @param args command-line arguments passed through to {@link SpringApplication#run}
     */
    public static void main(String[] args) {
        SpringApplication.run(RbacChallengeApplication.class, args);
    }

}
