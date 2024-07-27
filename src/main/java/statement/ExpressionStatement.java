package statement;

import expression.Expression;

public class ExpressionStatement extends Statement {
    private Expression expression;

    public ExpressionStatement() {
        setType(StatementType.EXPRESSION);
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
