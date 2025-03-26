package org.hcm.pcn.formula_validator.enums;

import org.hcm.pcn.formula_validator.exception.HandledError;

import java.util.Arrays;

public enum ExceptionMessage {
    TOKEN_NOT_VALID_IN_LINE("TOKEN_NOT_VALID_IN_LINE"),
    UNEXPECTED_END_OF_INPUT_LINE("UNEXPECTED_END_OF_INPUT_LINE"),
    CODE_IS_DUPLICATE("CODE_IS_DUPLICATE"),
    CODE_IS_NOT_VALID("CODE_IS_NOT_VALID"),
    PARENT_CODE_IS_NOT_VALID("PARENT_CODE_IS_NOT_VALID"),
    CHILD_CODE_IS_NOT_VALID("CHILD_CODE_IS_NOT_VALID"),
    PRODUCT_CODE_NOT_EXIST("PRODUCT_CODE_NOT_EXIST"),
    PRODUCT_CODE_IS_NOT_VALID("PRODUCT_CODE_IS_NOT_VALID"),
    PERSIST_PRODUCT_HAS_ERROR("PERSIST_PRODUCT_HAS_ERROR"),
    PERSIST_BLOCK_HAS_ERROR("PERSIST_BLOCK_HAS_ERROR"),
    EDIT_PRODUCT_HAS_ERROR("EDIT_PRODUCT_HAS_ERROR"),
    EDIT_BLOCK_HAS_ERROR("EDIT_BLOCK_HAS_ERROR"),
    ADD_BLOCK_TO_GROUP_HAS_ERROR("ADD_BLOCK_TO_GROUP_HAS_ERROR"),
    ADD_BLOCK_TO_HAS_ERROR("ADD_BLOCK_TO_HAS_ERROR"),
    DELETE_BLOCK_FROM_HAS_ERROR("DELETE_BLOCK_FROM_HAS_ERROR"),
    PARENT_BLOCK_AND_CHILD_BLOCK_IS_SAME("PARENT_BLOCK_AND_CHILD_BLOCK_IS_SAME"),
    ;

    private final String code;

    ExceptionMessage(String code) {
        this.code = code;
    }

    public HandledError getException(String lang) {
        return getExceptionWithParam(lang);
    }

    public HandledError getExceptionWithParam(String lang, Object... args) {
        return new HandledError(
                code,
                lang,
                Arrays.stream(args)
                        .map(String::valueOf)
                        .toArray(String[]::new)
        );
    }

    public void throwException(String lang) {
        throw getException(lang);
    }

    public void throwExceptionWithParam(String lang, Object... args) {
        throw getExceptionWithParam(lang, args);
    }
}
