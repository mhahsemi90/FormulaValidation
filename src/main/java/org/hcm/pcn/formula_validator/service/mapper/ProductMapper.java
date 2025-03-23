package org.hcm.pcn.formula_validator.service.mapper;

import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Named("productToProductDto")
    ProductDto productToProductDto(Product product);

    @IterableMapping(qualifiedByName = "productToProductDto")
    List<ProductDto> productListToProductDtoList(List<Product> product);

    Product productDtoToProduct(ProductDto productDto);
}
