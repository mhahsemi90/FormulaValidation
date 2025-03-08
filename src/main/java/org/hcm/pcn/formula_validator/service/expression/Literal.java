package org.hcm.pcn.formula_validator.service.expression;

import org.hcm.pcn.formula_validator.enums.ExpressionType;

public class Literal extends Expression {
    private String value;

    public Literal(String value) {
        super(ExpressionType.LITERAL_EXPRESSION);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
