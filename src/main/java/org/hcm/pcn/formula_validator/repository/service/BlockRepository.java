package org.hcm.pcn.formula_validator.repository.service;

import org.hcm.pcn.formula_validator.enums.BlockType;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findAllByProductCodeAndType(String productCode, BlockType type);
}
