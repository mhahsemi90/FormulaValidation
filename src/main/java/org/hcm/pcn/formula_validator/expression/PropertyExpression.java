package org.hcm.pcn.formula_validator.expression;

public class PropertyExpression extends Expression {
    private Expression key;
    private Expression value;

    public PropertyExpression() {
        super(ExpressionType.PROPERTY_EXPRESSION);
    }

    public Expression getKey() {
        return key;
    }

    public void setKey(Expression key) {
        this.key = key;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }
}
