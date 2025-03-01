package org.hcm.pcn.formula_validator.dto;

import java.util.Map;

public enum BlockType {
    VARIABLE,
    STRING_VARIABLE,
    NUMBER_VARIABLE,
    OBJECT,
    LITERAL,
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
    GROUP;

    public Block getBlock(Map<String, Block> blockPool, String identifier) {
        return blockPool.get(identifier) != null ?
                blockPool.get(identifier) :
                new Block(this, identifier, identifier, identifier);
    }

    public Block getBlock(String identifier) {
        return new Block(this, identifier, identifier, identifier);
    }

    public Block getBlock() {
        return new Block();
    }
}
