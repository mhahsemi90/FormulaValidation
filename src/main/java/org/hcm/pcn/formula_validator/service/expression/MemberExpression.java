package org.hcm.pcn.formula_validator.service.expression;

import org.hcm.pcn.formula_validator.enums.ExpressionType;

public class MemberExpression extends Expression {
    private Expression object;
    private Expression property;

    public MemberExpression() {
        super(ExpressionType.MEMBER_EXPRESSION);
    }

    public Expression getObject() {
        return object;
    }

    public void setObject(Expression object) {
        this.object = object;
    }

    public Expression getProperty() {
        return property;
    }

    public void setProperty(Expression property) {
        this.property = property;
    }
}
