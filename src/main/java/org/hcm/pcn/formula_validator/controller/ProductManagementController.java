package org.hcm.pcn.formula_validator.controller;

import graphql.GraphQLError;
import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.interfaces.ProductManagementService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductManagementController {
    private final ProductManagementService productManagementService;

    public ProductManagementController(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @QueryMapping
    List<ProductDto> getAllProduct(@Argument String lang) {
        return productManagementService.getAllProduct(lang);
    }

    @MutationMapping
    Boolean addProduct(@Argument ProductDto product, @Argument String lang) {
        return productManagementService.addProduct(product, lang);
    }

    @MutationMapping
    Boolean editProduct(@Argument ProductDto product, @Argument String lang) {
        return productManagementService.editProduct(product, lang);
    }
}
