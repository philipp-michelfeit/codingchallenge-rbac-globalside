package com.globalside.codingchallenge.rbac.mapper;


import com.globalside.codingchallenge.rbac.db.model.ProductDbo;
import com.globalside.codingchallenge.rbac.model.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct-generated mapper between the JPA entity ({@link ProductDbo}) and the API model
 * ({@link ProductDto}). {@code componentModel = "spring"} registers the generated 
 * implementation as a Spring bean.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /** 
     * Converts a persistence entity to its API representation.
     * 
     * @param productDbo the entity to convert
     * @return the corresponding DTO
     */
    ProductDto toProductDto(ProductDbo productDbo);

    /**
     * Converts an API model to its persistence representation.
     * 
     * @param productDto the DTO to convert
     * @return the corresponding entity
     */
    ProductDbo toProductDbo(ProductDto productDto);

    /**
     * Converts a list of entities to a list of DTOs.
     * 
     * @param productDbos the entities to convert
     * @return the corresponding DTOs, in the same order
     */
    List<ProductDto> toProductDtos(List<ProductDbo> productDbos);

    /**
     * Converts a list of DTOs to a list of entities.
     * 
     * @param productDtos the DTOs to convert
     * @return the corresponding entities, in the same order
     */
    List<ProductDbo> toProductDbos(List<ProductDto> productDtos);
}
