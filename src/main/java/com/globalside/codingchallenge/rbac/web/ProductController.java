package com.globalside.codingchallenge.rbac.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globalside.codingchallenge.rbac.api.ProductApi;
import com.globalside.codingchallenge.rbac.model.ProductDto;
import com.globalside.codingchallenge.rbac.service.ProductService;

/**
 * Thin REST layer that delegates to {@link ProductService}; the actual RBAC checks live there
 * (via {@code PreAuthorize}) and in {@code SecurityConfig}'s URL-based rules. See {@link
 * ProductApi} for the documented route contract.
 */
@RequestMapping("/products")
@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    /**
     * @param productService business logic backing every endpoint below
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** {@inheritDoc} */
    @Override
    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    /** {@inheritDoc} */
    @Override
    @GetMapping("/{id}")
    public ProductDto getProductById(Integer id) {
        return productService.getProductById(id);
    }

    /** {@inheritDoc} */
    @Override
    @PostMapping
    public ProductDto createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    /** {@inheritDoc} */
    @Override
    @PutMapping("/{id}")
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    /** {@inheritDoc} */
    @Override
    @DeleteMapping("/{id}")
    public void deleteProduct(Integer id) {
        productService.deleteProduct(id);
    }
}
