package org.hcm.pcn.formula_validator.repository.service;

import org.hcm.pcn.formula_validator.enums.BlockType;
import org.hcm.pcn.formula_validator.repository.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    Stream<Block> streamByCode(String code);

    Stream<Block> streamByCodeIn(List<String> code);

    Boolean existsByCode(String code);

    List<Block> findAllByProductCodeAndType(String productCode, BlockType type);

    List<Block> findAllByProductCode(String productCode);

    @Query("select b.code from Block b " +
            "inner join ChildBlock c on b.id = c.parentBlock.id " +
            "where c.childBlock.code = :code and c.parentBlock.type = 'GROUP' ")
    List<String> findGroupBlockList(@Param("code") String code);
}
