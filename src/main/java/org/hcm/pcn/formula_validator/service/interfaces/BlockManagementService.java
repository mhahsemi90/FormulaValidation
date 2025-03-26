package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.enums.BlockType;

import java.util.List;
import java.util.Map;

public interface BlockManagementService {
    String findGroupCodeBlock(String code);

    List<BlockDto> getAllBlockByProductCode(String productCode, String lang);

    Map<String, BlockDto> getAllOperandMapByProductCode(String productCode);

    List<BlockDto> getAllBlockByProductCodeAndType(String productCode, BlockType blockType, String lang);

    Boolean addBlock(BlockDto blockDto, String lang);

    Boolean editBlock(BlockDto blockDto, String lang);

    Boolean addBlockToList(String parentCode, List<String> childCode, String lang);

    Boolean deleteBlockFromList(String parentCode, List<String> childCode, String lang);
}
