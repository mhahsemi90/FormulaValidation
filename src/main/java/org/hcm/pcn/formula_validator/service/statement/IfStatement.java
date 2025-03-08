package org.hcm.pcn.formula_validator.service.statement;

import org.hcm.pcn.formula_validator.enums.StatementType;
import org.hcm.pcn.formula_validator.service.expression.Expression;

public class IfStatement extends Statement {
    private Expression test;
    private Statement consequent;
    private Statement alternate;

    public IfStatement() {
        super(StatementType.IF);
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
