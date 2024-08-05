package expression;

public class Variable extends Expression {
    private String idName;

    public Variable(String idName) {
        super(ExpressionType.VARIABLE_EXPRESSION);
        this.idName = idName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }
}
