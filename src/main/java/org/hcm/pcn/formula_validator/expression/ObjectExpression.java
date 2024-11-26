package org.hcm.pcn.formula_validator.expression;

import java.util.ArrayList;
import java.util.List;

public class ObjectExpression extends Expression {
    private List<Expression> propertyList;

    public ObjectExpression() {
        super(ExpressionType.OBJECT_EXPRESSION);
        setPropertyList(new ArrayList<>());
    }

    public List<Expression> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Expression> propertyList) {
        this.propertyList = propertyList;
    }
}
