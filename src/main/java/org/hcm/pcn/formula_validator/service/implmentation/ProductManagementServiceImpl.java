package org.hcm.pcn.formula_validator.service.implmentation;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.ProductDto;
import org.hcm.pcn.formula_validator.exception.HandledError;
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
    public List<ProductDto> getAllProduct() {
        return productMapper.productListToProductDtoList(
                productRepository.findAll()
        );
    }

    @Override
    @Transactional
    public Boolean editProduct(ProductDto productDto) {
        Product dbProduct = productRepository
                .streamByCode(productDto.getCode())
                .findFirst()
                .orElseThrow(() -> new HandledError("Code Is Not Valid"));
        dbProduct.setTitle(productDto.getTitle());
        dbProduct.setEnTitle(productDto.getEnTitle());
        try {
            productRepository.save(dbProduct);
        } catch (Exception e) {
            throw new HandledError("Edit Product Has Error: " + productDto.getCode());
        }
        return true;
    }

    @Override
    public Boolean addProduct(ProductDto productDto) {
        if (productRepository.existsByCode(productDto.getCode()))
            throw new HandledError("Code Is Duplicate");
        Product product = new Product(
                productDto.getCode(),
                productDto.getTitle(),
                productDto.getEnTitle()
        );
        try {
            productRepository.save(product);
        } catch (Exception e) {
            throw new HandledError("Persist Product Has Error: " + productDto.getCode());
        }
        return true;
    }

    @Override
    public List<BlockDto> getAllGroup(String productCode) {
        return null;
    }
}
