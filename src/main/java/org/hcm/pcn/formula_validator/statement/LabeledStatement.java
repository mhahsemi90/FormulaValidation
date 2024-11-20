package org.hcm.pcn.formula_validator.statement;

import org.hcm.pcn.formula_validator.expression.Expression;

public class LabeledStatement extends Statement {
    private Expression label;
    private Statement body;

    public LabeledStatement(Expression label, Statement body) {
        super(StatementType.LABEL);
        this.label = label;
        this.body = body;
    }

    public LabeledStatement() {
        super(StatementType.LABEL);
    }

    public Expression getLabel() {
        return label;
    }

    public void setLabel(Expression label) {
        this.label = label;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }
}
