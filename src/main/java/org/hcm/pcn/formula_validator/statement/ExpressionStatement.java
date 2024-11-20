package org.hcm.pcn.formula_validator.statement;


import org.hcm.pcn.formula_validator.expression.Expression;

public class ExpressionStatement extends Statement {
    private Expression expression;

    public ExpressionStatement() {
        super(StatementType.EXPRESSION);
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
