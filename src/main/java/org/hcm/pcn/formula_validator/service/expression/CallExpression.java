package org.hcm.pcn.formula_validator.service.expression;

import org.hcm.pcn.formula_validator.enums.ExpressionType;

import java.util.ArrayList;
import java.util.List;

public class CallExpression extends Expression {
    private Expression callVariable;
    private List<Expression> argumentList;

    public CallExpression() {
        super(ExpressionType.CALL_EXPRESSION);
        setArgumentList(new ArrayList<>());
    }

    public Expression getCallVariable() {
        return callVariable;
    }

    public void setCallVariable(Expression callVariable) {
        this.callVariable = callVariable;
    }

    public List<Expression> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List<Expression> argumentList) {
        this.argumentList = argumentList;
    }
}
