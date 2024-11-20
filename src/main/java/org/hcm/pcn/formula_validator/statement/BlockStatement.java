package org.hcm.pcn.formula_validator.statement;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    private List<Statement> body;

    public BlockStatement() {
        super(StatementType.BLOCK);
        setBody(new ArrayList<>());
    }

    public BlockStatement(List<Statement> body) {
        super(StatementType.BLOCK);
        setBody(body);
    }

    public List<Statement> getBody() {
        return body;
    }

    public void setBody(List<Statement> body) {
        this.body = body;
    }
}
