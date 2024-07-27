package expression;

public class Literal extends Expression {
    private String value;

    public Literal() {
        setType(ExpressionType.LITERAL_EXPRESSION);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
