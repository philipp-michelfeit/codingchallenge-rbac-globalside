package com.globalside.codingchallenge.rbac.web;

import com.globalside.codingchallenge.rbac.api.ProductApi;
import com.globalside.codingchallenge.rbac.model.ProductDto;
import com.globalside.codingchallenge.rbac.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    public ProductDto getProductById(Integer id) {
        return productService.getProductById(id);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @Override
    public void deleteProduct(Integer id) {
        productService.deleteProduct(id);
    }
}
