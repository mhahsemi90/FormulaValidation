package org.hcm.pcn.formula_validator.service.mapper;

import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto productToProductDto(Product product);

    @Mapping(target = "blockList", ignore = true)
    @Named("productToProductDtoWithoutBlockList")
    ProductDto productToProductDtoWithoutBlockList(Product product);

    @IterableMapping(qualifiedByName = "productToProductDtoWithoutBlockList")
    List<ProductDto> productListToProductDtoListWithoutBlockList(List<Product> product);

    Product productDtoToProduct(ProductDto productDto);
}
