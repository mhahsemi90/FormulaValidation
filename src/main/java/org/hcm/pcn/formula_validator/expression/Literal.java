package org.hcm.pcn.formula_validator.expression;

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
