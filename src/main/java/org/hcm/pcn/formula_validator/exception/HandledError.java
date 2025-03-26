package org.hcm.pcn.formula_validator.exception;

import lombok.Getter;

@Getter
public class HandledError extends RuntimeException {
    private final String[] args;
    private final String lang;

    public HandledError(String message, String lang, String... args) {
        super(message);
        this.lang = lang;
        this.args = args;
    }
}
