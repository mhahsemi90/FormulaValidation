package org.hcm.pcn.formula_validator.statement;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    private List<Statement> bodyList;

    public BlockStatement() {
        super(StatementType.BLOCK);
        setBodyList(new ArrayList<>());
    }

    public BlockStatement(List<Statement> bodyList) {
        super(StatementType.BLOCK);
        setBodyList(bodyList);
    }

    public List<Statement> getBodyList() {
        return bodyList;
    }

    public void setBodyList(List<Statement> bodyList) {
        this.bodyList = bodyList;
    }
}
