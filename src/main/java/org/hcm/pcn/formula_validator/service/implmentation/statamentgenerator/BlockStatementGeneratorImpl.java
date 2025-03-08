package org.hcm.pcn.formula_validator.service.implmentation.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.BlockStatement;
import org.hcm.pcn.formula_validator.service.statement.Statement;
import org.hcm.pcn.formula_validator.service.token.Token;

import java.util.List;
import java.util.Optional;

public class BlockStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList) {
        Statement result = new Statement();
        Token firstToken = removeFirstTokenThatNotNewLine(selectedTokenList);
        Token lastToken = removeLastTokenThatNotNewLine(selectedTokenList);
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                firstToken.getValue().equals("{") &&
                lastToken.getValue().equals("}")) {
            StatementGenerator generator = new MainStatementGeneratorImpl();
            List<Statement> statementList = generator.getAllStatementFromTokenList(selectedTokenList, true);
            result = new BlockStatement(statementList);
        } else {
            this.throwTokenNotValid(firstToken);
        }
        return Optional.of(result);
    }
}
