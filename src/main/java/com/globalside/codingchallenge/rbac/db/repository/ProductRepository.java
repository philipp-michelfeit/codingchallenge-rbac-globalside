package com.globalside.codingchallenge.rbac.db.repository;

import com.globalside.codingchallenge.rbac.db.model.ProductDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDbo, Integer> {
}
