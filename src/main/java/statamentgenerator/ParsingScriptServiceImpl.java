package statamentgenerator;

import interfaces.ParsingScriptService;
import interfaces.StatementGenerator;
import statement.Statement;

import java.util.List;

public class ParsingScriptServiceImpl implements ParsingScriptService {
    private final StatementGenerator statementGenerator;

    public ParsingScriptServiceImpl() {
        this.statementGenerator = new MainStatementGeneratorImpl();
    }

    @Override
    public List<Statement> parsing(String script) {
        return statementGenerator.parsing(script);
    }
}
