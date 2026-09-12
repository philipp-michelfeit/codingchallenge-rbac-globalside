# Challenge: Implement Role-Based Access Control (RBAC) for the "Products" API

## Objective (User Story)

As an **admin**, I want to manage products by adding, updating, and deleting them using a secure REST API, so that I can control the product inventory.  
As a **user**, I want to view products but not have the ability to modify them, so that I can safely browse the product list without making accidental changes.

## Description

In this challenge, you are required to extend an existing Java Spring Boot project by implementing **Role-Based Access Control (RBAC)**. The existing project contains:
- A REST API for managing **products** with CRUD operations.
- A database pre-populated with **5 products**.
- Unprotected CRUD endpoints (`GET`, `POST`, `PUT`, `DELETE`) for products.

You must secure the API endpoints by implementing **Spring Security** and assigning role-based access to them. There should be two roles:
- **Admin**: Can access all CRUD operations (create, update, delete, and view products).
- **User**: Can only view products (read-only access).

This challenge needs to be completed in **Java** with **Spring Boot**. You must ensure that the project compiles and includes unit/integration tests that verify the expected behavior of the RBAC functionality.

## Acceptance Criteria

1. **Basic Authentication Setup**:
    - Implement basic authentication in the project using **Spring Security**.
    - Define two roles: **Admin** and **User**.
    - Store credentials and roles in-memory (or another method, if preferred).

2. **Role-Based Access Control**:
    - Protect the CRUD endpoints for products.
    - Ensure that **Admin** users can perform all operations (`GET`, `POST`, `PUT`, `DELETE`).
    - Ensure that **User** users can only perform `GET /products` and `GET /products/{id}` (read-only access).
    - Block access to `POST`, `PUT`, and `DELETE` for **User** users and return a `403 Forbidden` response.

3. **Error Handling**:
    - Unauthorized access attempts should return an appropriate **403 Forbidden** status with a meaningful error message.

4. **Testing**:
    - Write unit and/or integration tests to verify:
        - Admin users have full access to all CRUD operations.
        - User users can only view products.
        - Unauthorized users are prevented from accessing restricted endpoints.
    - Ensure the tests cover various scenarios, such as incorrect credentials or unauthorized access.

5. **Code Quality**:
    - Ensure the code is clean, well-organized, and follows industry best practices.
    - Provide comments where necessary to explain key parts of the code.

## Provided Resources

- A **Spring Boot** project with the following:
    - Existing CRUD endpoints for managing products.
    - A **database** containing 5 products.
    - Endpoints are currently **unprotected**.

You are required to extend this project to fulfill the above requirements. At the end of the challenge, the project must compile and include tests that ensure the expected RBAC functionality is correctly implemented.