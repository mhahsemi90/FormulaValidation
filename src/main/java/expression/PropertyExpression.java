package expression;

public class PropertyExpression extends Expression {
    private Variable key;
    private Expression value;

    public PropertyExpression() {
        super(ExpressionType.PROPERTY_EXPRESSION);
    }

    public Variable getKey() {
        return key;
    }

    public void setKey(Variable key) {
        this.key = key;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }
}
