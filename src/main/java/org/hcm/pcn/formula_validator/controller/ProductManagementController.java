package org.hcm.pcn.formula_validator.controller;

import graphql.GraphQLError;
import org.hcm.pcn.formula_validator.dto.BlockDto;
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
    List<ProductDto> getAllProduct() {
        return productManagementService.getAllProduct();
    }

    @MutationMapping
    Boolean editProduct(@Argument ProductDto product) {
        return productManagementService.editProduct(product);
    }

    @MutationMapping
    Boolean addProduct(@Argument ProductDto product) {
        return productManagementService.addProduct(product);
    }
    @QueryMapping
    List<BlockDto> getAllGroup(@Argument String productCode) {
        return productManagementService.getAllGroup(productCode);
    }
    @GraphQlExceptionHandler(HandledError.class)
    public GraphQLError handelParsingError(Exception e) {
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }
}
