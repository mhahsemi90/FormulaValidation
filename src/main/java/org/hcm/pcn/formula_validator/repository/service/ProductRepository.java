package org.hcm.pcn.formula_validator.repository.service;

import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Stream<Product> streamByCode(String code);
    Boolean existsByCode(String code);
}
