package org.hcm.pcn.formula_validator.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.*;
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
                            .orElseThrow(() -> unexpectedEndError(testTokenList)))
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
                    blockStatementGenerator.generate(
                            conquestTokenList, new ArrayList<>()
                    ).ifPresent(statement -> {
                        if (((BlockStatement) statement).getBodyList().size() > 1)
                            result.setConsequent(statement);
                        else if (((BlockStatement) statement).getBodyList().size() == 1)
                            result.setConsequent(((BlockStatement) statement).getBodyList().get(0));
                    });
                } else if (token.getValue().equals(";")) {
                    result.setConsequent(
                            new EmptyStatement()
                    );
                } else {
                    tempTokenList.add(0, token);
                    result.setConsequent(
                            getFirstStatementFromTokenList(tempTokenList)
                                    .orElseThrow(() ->
                                            getTokenNotValid(
                                                    ")", tempTokenList.get(0).getLineNumber()
                                            )
                                    )
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
                                        .orElseThrow(() ->
                                                getUnexpectedEnd(
                                                        tempTokenList.get(0)
                                                )
                                        )
                        );
                    }
                } else {
                    throwUnexpectedEnd(token);
                }
            } else {
                if (token != null)
                    tempTokenList.add(0, token);
            }
        }
        if (result.getTest() == null || result.getConsequent() == null) {
            throwUnexpectedEnd(token);
        }
        tokenList.clear();
        tokenList.addAll(tempTokenList);
        return Optional.of(result);
    }

}
