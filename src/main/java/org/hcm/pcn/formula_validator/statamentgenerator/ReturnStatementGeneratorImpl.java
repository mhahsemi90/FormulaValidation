package org.hcm.pcn.formula_validator.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.statement.IfStatement;
import org.hcm.pcn.formula_validator.statement.ReturnStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReturnStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList) {
        ReturnStatement result = new ReturnStatement();
        StatementGenerator expressionStatementGenerator = new ExpressionStatementGeneratorImpl();
        Token token = removeFirstTokenThatNotNewLine(selectedTokenList);
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                token.getValue().equals("return")) {
            result.setArgument(
                    ((ExpressionStatement) expressionStatementGenerator
                            .generate(selectedTokenList, new ArrayList<>())
                            .orElseThrow(() -> unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
        }
        return Optional.of(result);
    }
}
