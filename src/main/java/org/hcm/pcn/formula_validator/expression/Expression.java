package org.hcm.pcn.formula_validator.expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Expression {
    private ExpressionType type;
    @JsonIgnore
    private Expression parent;

    public Expression() {

    }

    public Expression(ExpressionType type) {
        this.type = type;
    }

    public ExpressionType getType() {
        return type;
    }

    public void setType(ExpressionType type) {
        this.type = type;
    }

    public Expression getParent() {
        return parent;
    }

    public void setParent(Expression parent) {
        this.parent = parent;
    }
}
