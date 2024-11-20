package org.hcm.pcn.formula_validator.expression;

public class VariableDeclaratorExpression extends Expression {
    private Expression variableName;
    private Expression initiateValue;

    public VariableDeclaratorExpression() {
        super(ExpressionType.VARIABLE_DECLARATOR_EXPRESSION);
    }

    public VariableDeclaratorExpression(Expression variableName, Expression initiateValue) {
        super(ExpressionType.VARIABLE_DECLARATOR_EXPRESSION);
        this.variableName = variableName;
        this.initiateValue = initiateValue;
    }

    public Expression getVariableName() {
        return variableName;
    }

    public void setVariableName(Expression variableName) {
        this.variableName = variableName;
    }

    public Expression getInitiateValue() {
        return initiateValue;
    }

    public void setInitiateValue(Expression initiateValue) {
        this.initiateValue = initiateValue;
    }
}
