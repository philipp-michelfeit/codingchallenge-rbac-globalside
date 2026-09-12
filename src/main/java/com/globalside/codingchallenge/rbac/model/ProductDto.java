package com.globalside.codingchallenge.rbac.model;

import lombok.Data;

@Data
public class ProductDto {
    Integer id;
    String name;
    String description;
    Double price;
    String currency;
    String category;
    String brand;
    String color;
}
