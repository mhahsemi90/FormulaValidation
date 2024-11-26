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
        Statement result = new Statement();
        Token firstToken = removeFirstTokenThatNotNewLine(selectedTokenList);
        Token secondToken = removeFirstTokenThatNotNewLine(selectedTokenList);
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                firstToken.getTokenType() == TokenType.VARIABLE &&
                secondToken.getValue().equals(":")) {
            Variable variable = getVariableExpression(selectedTokenList, firstToken.getValue());
            firstToken = getFirstTokenThatNotNewLine(selectedTokenList);
            if (firstToken.getValue().equals("{")) {
                StatementGenerator blockStatementGenerator = new BlockStatementGeneratorImpl();
                Statement blockStatement = blockStatementGenerator.generate(selectedTokenList, tokenList);
                result = new LabeledStatement(variable, blockStatement);
            } else {
                StatementGenerator statementGenerator = new MainStatementGeneratorImpl();
                Statement statement = statementGenerator.getFirstStatementFromTokenList(selectedTokenList);
                result = new LabeledStatement(variable, statement);
            }
        } else {
            throwTokenNotValid(selectedTokenList, ":");
        }
        return result;
    }
}
