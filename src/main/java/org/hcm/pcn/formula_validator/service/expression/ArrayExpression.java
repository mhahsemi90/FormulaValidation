package org.hcm.pcn.formula_validator.service.expression;

import org.hcm.pcn.formula_validator.enums.ExpressionType;

import java.util.ArrayList;
import java.util.List;

public class ArrayExpression extends Expression {
    private List<Expression> elementList;

    public ArrayExpression() {
        super(ExpressionType.ARRAY_EXPRESSION);
        setElementList(new ArrayList<>());
    }

    public List<Expression> getElementList() {
        return elementList;
    }

    public void setElementList(List<Expression> elementList) {
        this.elementList = elementList;
    }
}
