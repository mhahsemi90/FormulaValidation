package org.hcm.pcn.formula_validator.service.statement;

import org.hcm.pcn.formula_validator.enums.StatementType;

public class Statement {
    private StatementType type;

    public Statement() {
    }

    public Statement(StatementType type) {
        this.type = type;
    }

    public StatementType getType() {
        return type;
    }

    public void setType(StatementType type) {
        this.type = type;
    }
}
