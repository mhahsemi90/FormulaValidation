package org.hcm.pcn.formula_validator.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.expression.Variable;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.LabeledStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.token.Token;
import org.hcm.pcn.formula_validator.token.TokenType;

import java.util.List;

public class LabeledStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Statement generate(List<Token> selectedTokenList, List<Token> tokenList) {
        LabeledStatement labeledStatement = new LabeledStatement();
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                selectedTokenList.size() > 2 &&
                selectedTokenList.get(0).getTokenType() == TokenType.VARIABLE &&
                selectedTokenList.get(1).getValue().equals(":")) {
            Variable variable = getVariableExpression(selectedTokenList, selectedTokenList.get(0).getValue());
            selectedTokenList.remove(0);
            selectedTokenList.remove(0);
            if (selectedTokenList.get(0).getValue().equals("{")) {
                StatementGenerator blockStatementGenerator = new BlockStatementGeneratorImpl();
                Statement blockStatement = blockStatementGenerator.generate(selectedTokenList, tokenList);
                labeledStatement = new LabeledStatement(variable, blockStatement);
            } else {
                StatementGenerator statementGenerator = new MainStatementGeneratorImpl();
                Statement statement = statementGenerator.getFirstStatementFromTokenList(selectedTokenList);
                labeledStatement = new LabeledStatement(variable, statement);
            }
        } else {
            throwHandledError(selectedTokenList, ":");
        }
        return labeledStatement;
    }
}
