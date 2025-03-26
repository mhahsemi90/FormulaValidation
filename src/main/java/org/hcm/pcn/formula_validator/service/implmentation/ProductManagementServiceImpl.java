package org.hcm.pcn.formula_validator.service.implmentation;

import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.enums.ExceptionMessage;
import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.hcm.pcn.formula_validator.repository.service.ProductRepository;
import org.hcm.pcn.formula_validator.service.interfaces.ProductManagementService;
import org.hcm.pcn.formula_validator.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductManagementServiceImpl(
            ProductMapper productMapper,
            ProductRepository productRepository
    ) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDto> getAllProduct(String lang) {
        return productMapper.productListToProductDtoList(
                productRepository.findAll()
        );
    }

    @Override
    @Transactional
    public Boolean editProduct(ProductDto productDto, String lang) {
        Product dbProduct = productRepository
                .streamByCode(productDto.getCode())
                .findFirst()
                .orElseThrow(() -> ExceptionMessage.PRODUCT_CODE_IS_NOT_VALID.getExceptionWithParam(lang, productDto.getCode()));
        dbProduct.setTitle(productDto.getTitle());
        dbProduct.setEnTitle(productDto.getEnTitle());
        try {
            productRepository.save(dbProduct);
        } catch (Exception e) {
            ExceptionMessage.EDIT_PRODUCT_HAS_ERROR.throwExceptionWithParam(lang, dbProduct, e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean addProduct(ProductDto productDto, String lang) {
        if (productRepository.existsByCode(productDto.getCode()))
            ExceptionMessage.CODE_IS_DUPLICATE.throwException(lang);
        Product product = new Product(
                productDto.getCode(),
                productDto.getTitle(),
                productDto.getEnTitle()
        );
        try {
            productRepository.save(product);
        } catch (Exception e) {
            ExceptionMessage.PERSIST_PRODUCT_HAS_ERROR.throwExceptionWithParam(lang, product, e.getMessage());
        }
        return true;
    }


}
