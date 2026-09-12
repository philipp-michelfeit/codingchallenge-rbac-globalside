package com.globalside.codingchallenge.rbac.db.repository;

import com.globalside.codingchallenge.rbac.db.model.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link ProductDbo}; CRUD and query methods are provided
 * automatically by {@link JpaRepository}.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductDbo, Integer> {
}
