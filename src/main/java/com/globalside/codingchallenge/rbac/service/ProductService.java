package com.globalside.codingchallenge.rbac.service;


import com.globalside.codingchallenge.rbac.db.model.ProductDbo;
import com.globalside.codingchallenge.rbac.db.repository.ProductRepository;
import com.globalside.codingchallenge.rbac.mapper.ProductMapper;
import com.globalside.codingchallenge.rbac.model.ProductDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getAllProducts() {
        List<ProductDbo> dbos = productRepository.findAll();
        List<ProductDto> dtos = productMapper.toProductDtos(dbos);
        return dtos;
    }

    public ProductDto getProductById(Integer id) {
        Optional<ProductDbo> productDbo = productRepository.findById(id);
        if(productDbo.isPresent()) {
            return productMapper.toProductDto(productDbo.get());
        }
        throw new EntityNotFoundException("Product with id " + id + " not found");
    }

    public ProductDto createProduct(ProductDto productDto) {
        ProductDbo productDbo = productMapper.toProductDbo(productDto);
        ProductDbo savedProductDbo = productRepository.save(productDbo);
        return productMapper.toProductDto(savedProductDbo);
    }

    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        Optional<ProductDbo> existingDbo = productRepository.findById(id);
        if(existingDbo.isPresent()) {
            ProductDbo updatedProductDbo = productMapper.toProductDbo(productDto);
            updatedProductDbo.setId(id);
            ProductDbo savedProductDbo = productRepository.save(updatedProductDbo);
            return productMapper.toProductDto(savedProductDbo);
        }
        throw new EntityNotFoundException("Product with id " + id + " not found");
    }

    public void deleteProduct(Integer id) {
        Optional<ProductDbo> productDbo = productRepository.findById(id);
        if(productDbo.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }
    }
}
