package org.hcm.pcn.formula_validator.expression;

import java.util.ArrayList;
import java.util.List;

public class SequenceExpression extends Expression {
    List<Expression> expressionList;

    public SequenceExpression() {
        super(ExpressionType.SEQUENCE_EXPRESSION);
        setExpressionList(new ArrayList<>());
    }

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }
}
