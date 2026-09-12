package com.globalside.codingchallenge.rbac.model;

import lombok.Data;

/**
 * API-facing representation of a product, used as the request/response body for the
 * {@code /products} endpoints.
 */
@Data
public class ProductDto {
    /** Product id; {@code null} for a not-yet-created product. */
    Integer id;
    /** Product name. */
    String name;
    /** Free-text product description. */
    String description;
    /** Price amount, denominated in {@link #currency}. */
    Double price;
    /** ISO currency code for {@link #price} (e.g. {@code USD}). */
    String currency;
    /** Product category label. */
    String category;
    /** Product brand name. */
    String brand;
    /** Product color. */
    String color;
}
