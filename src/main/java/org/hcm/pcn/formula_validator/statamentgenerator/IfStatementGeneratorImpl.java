package org.hcm.pcn.formula_validator.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.EmptyStatement;
import org.hcm.pcn.formula_validator.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.statement.IfStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IfStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList) {
        IfStatement result = new IfStatement();
        StatementGenerator blockStatementGenerator = new BlockStatementGeneratorImpl();
        StatementGenerator expressionStatementGenerator = new ExpressionStatementGeneratorImpl();
        List<Token> tempTokenList = tokenList
                .stream()
                .map(Token::clone)
                .collect(Collectors.toList());
        Token token = removeFirstTokenThatNotNewLine(selectedTokenList);
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                token.getValue().equals("if")) {
            List<Token> testTokenList = getSameLevelTokenListBetweenTwoParenthesis(
                    token.getLevel(),
                    selectedTokenList);
            result.setTest(
                    ((ExpressionStatement) expressionStatementGenerator
                            .generate(testTokenList, new ArrayList<>())
                            .orElseThrow(() -> unexpectedEndError(tempTokenList)))
                            .getExpression()
            );
            tempTokenList.addAll(0, selectedTokenList);
            token = removeFirstTokenThatNotNewLine(tempTokenList);
            if (CollectionUtils.isNotEmpty(tempTokenList)) {
                if (token.getValue().equals("{")) {
                    tempTokenList.add(0, token);
                    List<Token> conquestTokenList = getSameLevelTokenListBetweenTwoBrace(
                            token.getLevel(),
                            tempTokenList);
                    result.setConsequent(
                            blockStatementGenerator.generate(
                                    conquestTokenList, new ArrayList<>()
                            ).orElse(null)
                    );
                } else if (token.getValue().equals(";")) {
                    result.setConsequent(
                            new EmptyStatement()
                    );
                } else {
                    tempTokenList.add(0, token);
                    result.setConsequent(
                            getFirstStatementFromTokenList(tempTokenList)
                    );
                }
            }
            token = removeFirstTokenThatNotNewLine(tempTokenList);
            if (CollectionUtils.isNotEmpty(tempTokenList)
                    && token.getValue().equals("else")) {
                token = removeFirstTokenThatNotNewLine(tempTokenList);
                if (CollectionUtils.isNotEmpty(tempTokenList)) {
                    if (token.getValue().equals("{")) {
                        tempTokenList.add(0, token);
                        List<Token> alternateTokenList = getSameLevelTokenListBetweenTwoBrace(
                                token.getLevel(),
                                tempTokenList);
                        result.setAlternate(
                                blockStatementGenerator.generate(
                                        alternateTokenList, new ArrayList<>()
                                ).orElse(null)
                        );
                    } else if (token.getValue().equals(";")) {
                        result.setAlternate(
                                new EmptyStatement()
                        );
                    } else {
                        tempTokenList.add(0, token);
                        result.setAlternate(
                                getFirstStatementFromTokenList(tempTokenList)
                        );
                    }
                } else {
                    throwUnexpectedEnd(tempTokenList);
                }
            }
        }
        if (result.getTest() == null || result.getConsequent() == null)
            throwUnexpectedEnd(tempTokenList);
        tokenList.clear();
        tokenList.addAll(tempTokenList);
        return Optional.of(result);
    }

}
