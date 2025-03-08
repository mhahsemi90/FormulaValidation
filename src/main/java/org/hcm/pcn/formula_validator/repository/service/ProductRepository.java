package org.hcm.pcn.formula_validator.repository.service;

import org.hcm.pcn.formula_validator.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
