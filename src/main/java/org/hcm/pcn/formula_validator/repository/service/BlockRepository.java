package org.hcm.pcn.formula_validator.repository.service;

import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
}
