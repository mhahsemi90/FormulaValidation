package statement;

import expression.Expression;

import java.util.ArrayList;

public class IfStatement extends Statement {
    private Expression test;
    private Statement consequent;
    private Statement alternate;
    public IfStatement() {
        setType(StatementType.IF);
    }
    public Expression getTest() {
        return test;
    }

    public void setTest(Expression test) {
        this.test = test;
    }

    public Statement getConsequent() {
        return consequent;
    }

    public void setConsequent(Statement consequent) {
        this.consequent = consequent;
    }

    public Statement getAlternate() {
        return alternate;
    }

    public void setAlternate(Statement alternate) {
        this.alternate = alternate;
    }
}