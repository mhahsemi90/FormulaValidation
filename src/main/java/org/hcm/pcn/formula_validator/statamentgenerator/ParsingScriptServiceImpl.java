package org.hcm.pcn.formula_validator.statamentgenerator;

import org.hcm.pcn.formula_validator.interfaces.ParsingScriptService;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.Statement;

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
