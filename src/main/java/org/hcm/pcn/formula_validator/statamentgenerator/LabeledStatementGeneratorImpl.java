package org.hcm.pcn.formula_validator.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.LabeledStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.token.Token;
import org.hcm.pcn.formula_validator.token.TokenType;

import java.util.List;
import java.util.Optional;

public class LabeledStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList) {
        Statement result = new Statement();
        Token firstToken = removeFirstTokenThatNotNewLine(selectedTokenList);
        Token secondToken = removeFirstTokenThatNotNewLine(selectedTokenList);
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                firstToken.getTokenType() == TokenType.VARIABLE &&
                secondToken.getValue().equals(":")) {
            String label = firstToken.getValue();
            firstToken = getFirstTokenThatNotNewLine(selectedTokenList);
            if (firstToken.getValue().equals("{")) {
                StatementGenerator blockStatementGenerator = new BlockStatementGeneratorImpl();
                Statement blockStatement = blockStatementGenerator
                        .generate(selectedTokenList, tokenList)
                        .orElseThrow(() -> unexpectedEndError(selectedTokenList));
                result = new LabeledStatement(label, blockStatement);
            } else {
                StatementGenerator statementGenerator = new MainStatementGeneratorImpl();
                result = new LabeledStatement(
                        label,
                        statementGenerator.getFirstStatementFromTokenList(selectedTokenList, false)
                                .orElseThrow(() ->
                                        getUnexpectedEnd(
                                                secondToken
                                        )
                                )
                );
            }
        } else {
            throwTokenNotValid(secondToken);
        }
        return Optional.of(result);
    }
}
