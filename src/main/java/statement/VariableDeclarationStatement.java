package statement;

import expression.VariableDeclaratorExpression;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclarationStatement extends Statement {
    private String kind;
    private List<VariableDeclaratorExpression> declaratorExpressionList;

    public VariableDeclarationStatement() {
        setType(StatementType.VARIABLE_DECLARATION);
        setDeclaratorExpressionList(new ArrayList<>());
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<VariableDeclaratorExpression> getDeclaratorExpressionList() {
        return declaratorExpressionList;
    }

    public void setDeclaratorExpressionList(List<VariableDeclaratorExpression> declaratorExpressionList) {
        this.declaratorExpressionList = declaratorExpressionList;
    }
}