package org.hcm.pcn.formula_validator.service.expression;

import org.hcm.pcn.formula_validator.enums.ExpressionType;

public class Variable extends Expression {
    private String idName;

    public Variable(String idName) {
        super(ExpressionType.VARIABLE_EXPRESSION);
        this.idName = idName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }
}
