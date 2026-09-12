package com.globalside.codingchallenge.rbac.service;


import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.globalside.codingchallenge.rbac.db.model.ProductDbo;
import com.globalside.codingchallenge.rbac.db.repository.ProductRepository;
import com.globalside.codingchallenge.rbac.mapper.ProductMapper;
import com.globalside.codingchallenge.rbac.model.ProductDto;

import jakarta.persistence.EntityNotFoundException;

/**
 * Business logic for product CRUD. Enforces role requirements at the method level via
 * {@link PreAutorize}, in addition to the URL-based rules in {@code SecurityConfig}, so access
 * control still applies even if this service is called from somewhere other than the REST layer.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    /**
     * @param productRepository persistence access for {@link ProductDbo}
     * @param productMapper converts between {@link ProductDbo} and {@link ProductDto}
     */
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Lists all products. Allowed for both {@code User} and {@code Admin} roles.
     * 
     * @return every product currently stored.
     */
    @PreAuthorize("hasAnyRole('User', 'Admin')")
    public List<ProductDto> getAllProducts() {
        List<ProductDbo> dbos = productRepository.findAll();
        List<ProductDto> dtos = productMapper.toProductDtos(dbos);
        return dtos;
    }

    /**
     * Fetches a single product by id. Allowed for both {@code User} and {@code Admin} roles.
     * 
     * @param id the product id
     * @return the matching product
     * @throws EntityNotFoundException if no product exists with the given id
     */
    @PreAuthorize("hasAnyRole('User', 'Admin')")
    public ProductDto getProductById(Integer id) {
        Optional<ProductDbo> productDbo = productRepository.findById(id);
        if(productDbo.isPresent()) {
            return productMapper.toProductDto(productDbo.get());
        }
        throw new EntityNotFoundException("Product with id " + id + " not found");
    }

    /**
     * Creates a new product. Restricted to the {@code Admin} role.
     * 
     * @param productDto the product to create
     * @return the created product, including its generated id
     */
    @PreAuthorize("hasRole('Admin')")
    public ProductDto createProduct(ProductDto productDto) {
        ProductDbo productDbo = productMapper.toProductDbo(productDto);
        ProductDbo savedProductDbo = productRepository.save(productDbo);
        return productMapper.toProductDto(savedProductDbo);
    }

    /**
     * Replaces an existing product's fields. Restricted to the {@code Admin} role.
     * 
     * @param id the id of the product to update
     * @param productDto the new field values
     * @return the updated product
     * @throws EntityNotFoundException if no product exists with the given id
     */
    @PreAuthorize("hasRole('Admin')")
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        Optional<ProductDbo> existingDbo = productRepository.findById(id);
        if(existingDbo.isPresent()) {
            // Map the incoming DTO to a fresh entity and re-attach the path id, rather than
            // mutating the existing entity in place, so the mapper is the single source of
            // truth for which fields get copied.
            ProductDbo updatedProductDbo = productMapper.toProductDbo(productDto);
            updatedProductDbo.setId(id);
            ProductDbo savedProductDbo = productRepository.save(updatedProductDbo);
            return productMapper.toProductDto(savedProductDbo);
        }
        throw new EntityNotFoundException("Product with id " + id + " not found");
    }

    /**
     * Deletes a product by id. Restricted to the {@code Admin} role.
     * 
     * @param id the id of the product to delete
     * @throws EntityNotFoundException if no product exists with the given id
     */
    @PreAuthorize("hasRole('Admin')")
    public void deleteProduct(Integer id) {
        Optional<ProductDbo> productDbo = productRepository.findById(id);
        if(productDbo.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }
    }
}
