package org.hcm.pcn.formula_validator.service.expression;

import org.hcm.pcn.formula_validator.enums.ExpressionType;

public class VariableDeclaratorExpression extends Expression {
    private Expression variable;
    private Expression initiateValue;

    public VariableDeclaratorExpression() {
        super(ExpressionType.VARIABLE_DECLARATOR_EXPRESSION);
    }

    public VariableDeclaratorExpression(Expression variable, Expression initiateValue) {
        super(ExpressionType.VARIABLE_DECLARATOR_EXPRESSION);
        this.variable = variable;
        this.initiateValue = initiateValue;
    }

    public Expression getVariable() {
        return variable;
    }

    public void setVariable(Expression variable) {
        this.variable = variable;
    }

    public Expression getInitiateValue() {
        return initiateValue;
    }

    public void setInitiateValue(Expression initiateValue) {
        this.initiateValue = initiateValue;
    }
}
