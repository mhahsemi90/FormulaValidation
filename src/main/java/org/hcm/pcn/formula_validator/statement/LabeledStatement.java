package org.hcm.pcn.formula_validator.statement;

import org.hcm.pcn.formula_validator.expression.Expression;

public class LabeledStatement extends Statement {
    private String label;
    private Statement body;

    public LabeledStatement(String label, Statement body) {
        super(StatementType.LABEL);
        this.label = label;
        this.body = body;
    }

    public LabeledStatement() {
        super(StatementType.LABEL);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }
}
