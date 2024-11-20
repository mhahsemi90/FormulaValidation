package org.hcm.pcn.formula_validator.expression;

public class Literal extends Expression {
    private String value;

    public Literal() {
        super(ExpressionType.LITERAL_EXPRESSION);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
