package org.hcm.pcn.formula_validator.service.statement;


import org.hcm.pcn.formula_validator.enums.StatementType;

public class EmptyStatement extends Statement {
    public EmptyStatement() {
        super(StatementType.EMPTY_STATEMENT);
    }
}
