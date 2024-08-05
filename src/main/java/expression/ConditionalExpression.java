package expression;

public class ConditionalExpression extends Expression {
    private Expression test;
    private Expression consequent;
    private Expression alternate;

    public ConditionalExpression() {
        super(ExpressionType.CONDITIONAL_EXPRESSION);
    }

    public Expression getTest() {
        return test;
    }

    public void setTest(Expression test) {
        this.test = test;
    }

    public Expression getConsequent() {
        return consequent;
    }

    public void setConsequent(Expression consequent) {
        this.consequent = consequent;
    }

    public Expression getAlternate() {
        return alternate;
    }

    public void setAlternate(Expression alternate) {
        this.alternate = alternate;
    }
}
