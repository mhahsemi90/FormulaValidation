package org.hcm.pcn.formula_validator.enums;

import org.hcm.pcn.formula_validator.dto.BlockDto;

import java.util.Map;

public enum BlockType {
    VARIABLE,
    STRING_LITERAL,
    NUMBER_LITERAL,
    OBJECT,
    LITERAL,
    LIST,
    ARITHMETIC_OPERATOR,
    ASSIGNMENT_OPERATOR,
    COMPARISON_OPERATOR,
    LOGICAL_OPERATOR,
    BITWISE_OPERATOR,
    KEYWORD,
    SEPARATOR,
    LABEL,
    LABEL_ASSIGN,
    OPEN_PARENTHESES,
    CLOSE_PARENTHESES,
    FUNCTION,
    ARGUMENT,
    GROUP;

    public BlockDto getBlock(Map<String, BlockDto> blockPool, String identifier) {
        return blockPool.get(identifier) != null ?
                blockPool.get(identifier) :
                new BlockDto(this, identifier, identifier, identifier);
    }

    public BlockDto getBlock(String identifier) {
        return new BlockDto(this, identifier, identifier, identifier);
    }

    public BlockDto getBlock() {
        return new BlockDto();
    }
}
