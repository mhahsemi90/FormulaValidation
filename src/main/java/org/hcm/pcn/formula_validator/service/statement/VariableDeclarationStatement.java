package org.hcm.pcn.formula_validator.service.statement;


import org.hcm.pcn.formula_validator.enums.StatementType;
import org.hcm.pcn.formula_validator.service.expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclarationStatement extends Statement {
    private String kind;
    private List<Expression> declaratorExpressionList;

    public VariableDeclarationStatement() {
        super(StatementType.VARIABLE_DECLARATION);
        setDeclaratorExpressionList(new ArrayList<>());
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<Expression> getDeclaratorExpressionList() {
        return declaratorExpressionList;
    }

    public void setDeclaratorExpressionList(List<Expression> declaratorExpressionList) {
        this.declaratorExpressionList = declaratorExpressionList;
    }
}
