package org.hcm.pcn.formula_validator.service.implmentation.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.service.statement.ReturnStatement;
import org.hcm.pcn.formula_validator.service.statement.Statement;
import org.hcm.pcn.formula_validator.service.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReturnStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList, String lang) {
        ReturnStatement result = new ReturnStatement();
        StatementGenerator expressionStatementGenerator = new ExpressionStatementGeneratorImpl();
        Token token = removeFirstTokenThatNotNewLine(selectedTokenList);
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                token.getValue().equals("return")) {
            result.setArgument(
                    ((ExpressionStatement) expressionStatementGenerator
                            .generate(selectedTokenList, new ArrayList<>(), lang)
                            .orElseThrow(() -> unexpectedEndError(selectedTokenList, lang)))
                            .getExpression()
            );
        }
        return Optional.of(result);
    }
}
