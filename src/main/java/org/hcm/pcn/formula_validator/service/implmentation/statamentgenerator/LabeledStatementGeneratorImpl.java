package org.hcm.pcn.formula_validator.service.implmentation.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.LabeledStatement;
import org.hcm.pcn.formula_validator.service.statement.Statement;
import org.hcm.pcn.formula_validator.service.token.Token;
import org.hcm.pcn.formula_validator.service.token.TokenType;

import java.util.List;
import java.util.Optional;

public class LabeledStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList, String lang) {
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
                        .generate(selectedTokenList, tokenList, lang)
                        .orElseThrow(() -> unexpectedEndError(selectedTokenList, lang));
                result = new LabeledStatement(label, blockStatement);
            } else {
                StatementGenerator statementGenerator = new MainStatementGeneratorImpl();
                result = new LabeledStatement(
                        label,
                        statementGenerator.getFirstStatementFromTokenList(selectedTokenList, false, lang)
                                .orElseThrow(() ->
                                        getUnexpectedEnd(
                                                secondToken
                                                , lang
                                        )
                                )
                );
            }
        } else {
            throwTokenNotValid(secondToken, lang);
        }
        return Optional.of(result);
    }
}
