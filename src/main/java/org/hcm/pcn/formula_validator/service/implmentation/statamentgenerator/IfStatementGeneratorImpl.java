package org.hcm.pcn.formula_validator.service.implmentation.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.*;
import org.hcm.pcn.formula_validator.service.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IfStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList, String lang) {
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
                            .generate(testTokenList, new ArrayList<>(), lang)
                            .orElseThrow(() -> unexpectedEndError(testTokenList, lang)))
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
                            conquestTokenList, new ArrayList<>(), lang
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
                            getFirstStatementFromTokenList(tempTokenList, false, lang)
                                    .orElseThrow(() ->
                                            getTokenNotValid(
                                                    ")", tempTokenList.get(0).getLineNumber(), lang
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
                                        alternateTokenList, new ArrayList<>(), lang
                                ).orElse(null)
                        );
                    } else if (token.getValue().equals(";")) {
                        result.setAlternate(
                                new EmptyStatement()
                        );
                    } else {
                        tempTokenList.add(0, token);
                        result.setAlternate(
                                getFirstStatementFromTokenList(tempTokenList, false, lang)
                                        .orElseThrow(() ->
                                                getUnexpectedEnd(
                                                        tempTokenList.get(0)
                                                        , lang
                                                )
                                        )
                        );
                    }
                } else {
                    throwUnexpectedEnd(token, lang);
                }
            } else {
                if (token != null)
                    tempTokenList.add(0, token);
            }
        }
        if (result.getTest() == null || result.getConsequent() == null) {
            if (token != null)
                throwUnexpectedEnd(token, lang);
            else if (CollectionUtils.isNotEmpty(tempTokenList))
                throwUnexpectedEnd(tempTokenList.get(0), lang);
            else if (CollectionUtils.isNotEmpty(selectedTokenList))
                throwUnexpectedEnd(selectedTokenList.get(0), lang);
            throwUnexpectedEnd(token, lang);
        }
        tokenList.clear();
        tokenList.addAll(tempTokenList);
        return Optional.of(result);
    }

}
