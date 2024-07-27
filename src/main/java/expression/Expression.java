package expression;

public class Expression {
    private ExpressionType type;

    private Expression parent;

    public ExpressionType getType() {
        return type;
    }

    public void setType(ExpressionType type) {
        this.type = type;
    }

    public Expression getParent() {
        return parent;
    }

    public void setParent(Expression parent) {
        this.parent = parent;
    }
}
