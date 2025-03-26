package org.hcm.pcn.formula_validator.service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-26T08:10:42+0330",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto productToProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setCode( product.getCode() );
        productDto.setTitle( product.getTitle() );
        productDto.setEnTitle( product.getEnTitle() );

        return productDto;
    }

    @Override
    public List<ProductDto> productListToProductDtoList(List<Product> product) {
        if ( product == null ) {
            return null;
        }

        List<ProductDto> list = new ArrayList<ProductDto>( product.size() );
        for ( Product product1 : product ) {
            list.add( productToProductDto( product1 ) );
        }

        return list;
    }

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productDto.getId() );
        product.setCode( productDto.getCode() );
        product.setTitle( productDto.getTitle() );
        product.setEnTitle( productDto.getEnTitle() );

        return product;
    }
}
