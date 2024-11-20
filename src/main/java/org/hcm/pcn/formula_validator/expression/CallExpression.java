package org.hcm.pcn.formula_validator.expression;

import java.util.ArrayList;
import java.util.List;

public class CallExpression extends Expression {
    private Expression callVariableName;
    private List<Expression> argumentList;

    public CallExpression() {
        super(ExpressionType.CALL_EXPRESSION);
        setArgumentList(new ArrayList<>());
    }

    public Expression getCallVariableName() {
        return callVariableName;
    }

    public void setCallVariableName(Expression callVariableName) {
        this.callVariableName = callVariableName;
    }

    public List<Expression> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List<Expression> argumentList) {
        this.argumentList = argumentList;
    }
}
