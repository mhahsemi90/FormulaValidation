package statement;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    private List<Statement> body;

    public BlockStatement() {
        setType(StatementType.BLOCK);
        setBody(new ArrayList<>());
    }

    public BlockStatement(List<Statement> body) {
        setType(StatementType.BLOCK);
        setBody(body);
    }

    public List<Statement> getBody() {
        return body;
    }

    public void setBody(List<Statement> body) {
        this.body = body;
    }
}
