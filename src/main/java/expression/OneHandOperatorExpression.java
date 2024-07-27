package expression;

public class OneHandOperatorExpression extends OperatorExpression {
    private Expression argument;

    public Expression getArgument() {
        return argument;
    }

    public void setArgument(Expression argument) {
        this.argument = argument;
    }
}
