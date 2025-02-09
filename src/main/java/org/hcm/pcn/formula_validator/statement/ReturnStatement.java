package org.hcm.pcn.formula_validator.statement;

import org.hcm.pcn.formula_validator.expression.Expression;

public class ReturnStatement extends Statement {
    private Expression argument;

    public ReturnStatement() {
        super(StatementType.RETURN);
    }

    public Expression getArgument() {
        return argument;
    }

    public void setArgument(Expression argument) {
        this.argument = argument;
    }
}
