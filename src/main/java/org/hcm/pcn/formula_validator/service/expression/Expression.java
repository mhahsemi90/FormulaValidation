package org.hcm.pcn.formula_validator.service.expression;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hcm.pcn.formula_validator.enums.ExpressionType;

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
