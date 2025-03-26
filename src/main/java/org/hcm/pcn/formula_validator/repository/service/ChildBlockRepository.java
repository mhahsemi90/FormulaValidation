package org.hcm.pcn.formula_validator.repository.service;

import org.hcm.pcn.formula_validator.repository.entity.ChildBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildBlockRepository extends JpaRepository<ChildBlock, Long> {
    Integer deleteAllByParentBlock_CodeAndChildBlock_Code(String parenCode, String childCode);
}
