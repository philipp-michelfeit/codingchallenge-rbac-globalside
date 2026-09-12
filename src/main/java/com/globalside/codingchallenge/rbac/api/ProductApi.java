package com.globalside.codingchallenge.rbac.api;

import com.globalside.codingchallenge.rbac.model.ProductDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductApi {
    @GetMapping("/products")
    List<ProductDto> getAllProducts();

    @GetMapping("/products/{id}")
    ProductDto getProductById(@PathVariable("id") Integer id);

    @PostMapping("/products")
    ProductDto createProduct(@RequestBody ProductDto productDto);

    @PutMapping("/products/{id}")
    ProductDto updateProduct(@PathVariable("id") Integer id, @RequestBody ProductDto productDto);

    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable("id") Integer id);
}
