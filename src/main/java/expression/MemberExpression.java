package expression;

public class MemberExpression extends Expression {
    Expression object;
    Expression property;

    public MemberExpression() {
        super(ExpressionType.MEMBER_EXPRESSION);
    }

    public Expression getObject() {
        return object;
    }

    public void setObject(Expression object) {
        this.object = object;
    }

    public Expression getProperty() {
        return property;
    }

    public void setProperty(Expression property) {
        this.property = property;
    }
}
