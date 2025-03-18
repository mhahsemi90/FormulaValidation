package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.ProductDto;

import java.util.List;

public interface ProductManagementService extends BaseFormulaConcept {
    List<ProductDto> getAllProduct();

    Boolean editProduct(ProductDto productDto);

    Boolean addProduct(ProductDto productDto);
}
