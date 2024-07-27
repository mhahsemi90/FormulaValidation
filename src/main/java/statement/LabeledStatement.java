package statement;

import expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class LabeledStatement extends Statement{
    private Expression label;
    private Statement body;

    public LabeledStatement(Expression label,Statement body) {
        setType(StatementType.LABEL);
        this.label = label;
        this.body = body;
    }
    public LabeledStatement() {
        setType(StatementType.LABEL);
    }

    public Expression getLabel() {
        return label;
    }

    public void setLabel(Expression label) {
        this.label = label;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }
}
