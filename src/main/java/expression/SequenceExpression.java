package expression;

import java.util.ArrayList;
import java.util.List;

public class SequenceExpression extends Expression {
    List<Expression> expressions;

    public SequenceExpression() {
        super(ExpressionType.SEQUENCE_EXPRESSION);
        setExpressions(new ArrayList<>());
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }
}
