package org.hcm.pcn.formula_validator.service.interfaces;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hcm.pcn.formula_validator.enums.ExceptionMessage;
import org.hcm.pcn.formula_validator.enums.ExpressionType;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.expression.OneHandOperatorExpression;
import org.hcm.pcn.formula_validator.service.expression.TwoHandOperatorExpression;
import org.hcm.pcn.formula_validator.service.expression.Variable;
import org.hcm.pcn.formula_validator.service.implmentation.statamentgenerator.MainStatementGeneratorImpl;
import org.hcm.pcn.formula_validator.service.statement.Statement;
import org.hcm.pcn.formula_validator.service.token.Token;

import java.util.*;
import java.util.stream.Collectors;

import static org.hcm.pcn.formula_validator.service.token.TokenType.NEW_LINE;

public interface StatementGenerator extends BaseFormulaConcept {
    Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList, String lang);

    default List<Statement> parsingToListOfStatement(String script, Boolean blockGeneration, String lang) {
        return null;
    }

    default List<Token> parsingToListOfTokenList(String script) {
        return null;
    }

    default List<Statement> getAllStatementFromTokenList(List<Token> tokenList, Boolean blockGeneration, String lang) {
        List<Statement> allStatementList = new ArrayList<>();
        tokenList = validateAllParenthesisAndBrace(tokenList, lang);
        while (CollectionUtils.isNotEmpty(tokenList)) {
            getStatement(tokenList, new MainStatementGeneratorImpl(), blockGeneration, lang)
                    .ifPresent(allStatementList::add);
        }
        return allStatementList;
    }

    default Optional<Statement> getFirstStatementFromTokenList(List<Token> tokenList, Boolean blockGeneration, String lang) {
        Optional<Statement> result;
        result = getStatement(tokenList, new MainStatementGeneratorImpl(), blockGeneration, lang);
        return result;
    }

    private Optional<Statement> getStatement(List<Token> tokenList, StatementGenerator generator, Boolean blockGeneration, String lang) {
        Optional<Statement> result;
        List<Token> selectedTokenList = selectTokenListToFirstSemicolon(tokenList);
        List<Token> tempTokenList = selectedTokenList
                .stream()
                .map(Token::clone)
                .collect(Collectors.toList());
        try {
            result = generator.generate(selectedTokenList, tokenList, lang);
        } catch (Exception e) {
            selectedTokenList = selectTokenListToFirstNewLine(tempTokenList, tokenList);
            if (CollectionUtils.isNotEmpty(selectedTokenList) && !blockGeneration) {
                result = generator.generate(selectedTokenList, tokenList, lang);
            } else {
                throw e;
            }
        }
        return result;
    }

    default List<Token> validateAllParenthesisAndBrace(List<Token> tokenList, String lang) {
        Stack<Token> parenthesisStack = new Stack<>();
        Stack<Token> braceStack = new Stack<>();
        Stack<Token> advancedBraceStack = new Stack<>();
        List<Token> tempTokenList = tokenList
                .stream()
                .map(Token::clone)
                .collect(Collectors.toList());
        while (CollectionUtils.isNotEmpty(tokenList)) {
            Token token = tokenList.remove(0);
            if (token.getValue().equals("{"))
                advancedBraceStack.push(token);
            if (token.getValue().equals("["))
                braceStack.push(token);
            if (token.getValue().equals("("))
                parenthesisStack.push(token);
            if (token.getValue().equals("}")) {
                if (advancedBraceStack.isEmpty()) {
                    throwTokenNotValid(token, lang);
                }
                advancedBraceStack.pop();
            }
            if (token.getValue().equals("]")) {
                if (braceStack.isEmpty()) {
                    throwTokenNotValid(token, lang);
                }
                braceStack.pop();
            }
            if (token.getValue().equals(")")) {
                if (parenthesisStack.isEmpty()) {
                    throwTokenNotValid(token, lang);
                }
                parenthesisStack.pop();
            }
        }
        if (!advancedBraceStack.isEmpty())
            throwTokenNotValid(advancedBraceStack.pop(), lang);
        if (!braceStack.isEmpty())
            throwTokenNotValid(braceStack.pop(), lang);
        if (!parenthesisStack.isEmpty())
            throwTokenNotValid(parenthesisStack.pop(), lang);
        return tempTokenList;
    }


    default Integer getLineNumber(List<Token> selectedTokenList) {
        return CollectionUtils.isNotEmpty(selectedTokenList) ? selectedTokenList.get(0).getLineNumber() : 1;
    }

    default List<Token> selectTokenListToFirstSemicolon(List<Token> tokenList) {
        List<Token> selectedTokenList = new ArrayList<>();
        int size = tokenList.size();
        int sameLevel = 0;
        boolean isBrace = false;
        for (int i = 0; i < size; i++) {
            if (!isBrace && tokenList.get(0).getValue().equals("{")) {
                isBrace = true;
                sameLevel = tokenList.get(0).getLevel();
            }
            if (!isBrace && tokenList.get(0).getValue().equals(";")) {
                selectedTokenList.add(tokenList.remove(0));
                break;
            }
            if (isBrace && tokenList.get(0).getValue().equals("}")
                    && tokenList.get(0).getLevel().equals(sameLevel)) {
                selectedTokenList.add(tokenList.remove(0));
                break;
            }
            selectedTokenList.add(tokenList.remove(0));
        }
        return selectedTokenList;
    }

    default List<Token> selectTokenListToFirstNewLine(List<Token> selectedTokenList, List<Token> tokenList) {
        if (CollectionUtils.isNotEmpty(selectedTokenList)) {
            List<Token> newSelectedTokenList = new ArrayList<>();
            Iterator<Token> tokenIterator = selectedTokenList.listIterator();
            while (tokenIterator.hasNext()) {
                Token token = tokenIterator.next();
                tokenIterator.remove();
                newSelectedTokenList.add(token);
                if (token.getTokenType() == NEW_LINE
                        || token.getValue().equals("{"))
                    break;
            }
            tokenList.addAll(0, selectedTokenList);
            selectedTokenList = newSelectedTokenList;
        }
        return selectedTokenList;
    }

    default Token removeFirstTokenThatNotNewLine(List<Token> selectedTokenList) {
        Token token = null;
        Token newLineToken = null;
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            if (selectedTokenList.get(0).getTokenType() != NEW_LINE) {
                token = selectedTokenList.remove(0);
                break;
            } else {
                newLineToken = selectedTokenList.remove(0);
            }
        }
        if (token == null) {
            return newLineToken;
        }
        return token;
    }

    default Token getFirstTokenThatNotNewLine(List<Token> selectedTokenList) {
        Token token = null;
        Token newLineToken = null;
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            if (selectedTokenList.get(0).getTokenType() != NEW_LINE) {
                token = selectedTokenList.get(0);
                break;
            } else {
                newLineToken = selectedTokenList.remove(0);
            }
        }
        if (token == null) {
            return newLineToken;
        }
        return token;
    }

    default Token removeLastTokenThatNotNewLine(List<Token> selectedTokenList) {
        Token token = null;
        Token newLineToken = null;
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            if (selectedTokenList.get(selectedTokenList.size() - 1).getTokenType() != NEW_LINE) {
                token = selectedTokenList.remove(selectedTokenList.size() - 1);
                break;
            } else {
                newLineToken = selectedTokenList.remove(selectedTokenList.size() - 1);
            }
        }
        if (token == null) {
            return newLineToken;
        }
        return token;
    }

    default boolean isVariableKeyword(String value) {
        return value.equals("var") || value.equals("let") || value.equals("const");
    }

    default Integer getOperatorPrecedence(String operator) {
        int result = 0;
        if (StringUtils.isBlank(operator))
            return result;
        switch (operator) {
            case ")":
            case ";":
            case ",":
            case "=":
            case "]":
                break;
            case "??":
                result = 5;
                break;
            case "||":
                result = 6;
                break;
            case "&&":
                result = 7;
                break;
            case "|":
                result = 8;
                break;
            case "^":
                result = 9;
                break;
            case "&":
                result = 10;
                break;
            case "==":
            case "!=":
            case "!==":
            case "===":
                result = 11;
                break;
            case "<":
            case ">":
            case ">=":
            case "<=":
                result = 12;
                break;
            case "<<":
            case ">>":
            case ">>>":
                result = 13;
                break;
            case "+":
            case "-":
                result = 14;
                break;
            case "*":
            case "/":
            case "%":
                result = 15;
                break;
        }
        return result;
    }

    default TwoHandOperatorExpression getOperatorAndTypeOfTwoHandOperatorExpression(Token token) {
        TwoHandOperatorExpression operatorExpression = new TwoHandOperatorExpression();
        operatorExpression.setOperator(token.getValue());
        if (token.getValue().equals("("))
            operatorExpression.setType(ExpressionType.PARENTHESIS_EXPRESSION);
        else if (getBinaryList().contains(token.getValue()))
            operatorExpression.setType(ExpressionType.BINARY_EXPRESSION);
        else if (getAssignmentOperatorList().contains(token.getValue()))
            operatorExpression.setType(ExpressionType.ASSIGNMENT_EXPRESSION);
        else if (getLogicalList().contains(token.getValue()))
            operatorExpression.setType(ExpressionType.LOGICAL_EXPRESSION);
        return operatorExpression;
    }

    default OneHandOperatorExpression getOperatorAndTypeOfOneHandOperatorExpression(Token token) {
        OneHandOperatorExpression operatorExpression = new OneHandOperatorExpression();
        operatorExpression.setOperator(token.getValue());
        if (getUpdateUnaryList().contains(token.getValue()))
            operatorExpression.setType(ExpressionType.UPDATE_EXPRESSION);
        else if (getUnaryList().contains(token.getValue()))
            operatorExpression.setType(ExpressionType.UNARY_EXPRESSION);
        return operatorExpression;
    }

    default HandledError tokenNotValidError(Token token, String lang) {
        return ExceptionMessage.TOKEN_NOT_VALID_IN_LINE.getExceptionWithParam(lang, token.getValue(), token.getLineNumber());
    }

    default void throwTokenNotValid(Token token, String lang) {
        throw tokenNotValidError(token, lang);
    }

    default HandledError getTokenNotValid(String value, Integer lineNumber, String lang) {
        return tokenNotValidError(new Token(value, lineNumber), lang);
    }

    default void throwTokenNotValid(String value, Integer lineNumber, String lang) {
        throw getTokenNotValid(value, lineNumber, lang);
    }

    default HandledError unexpectedEndError(List<Token> tokenList, String lang) {
        return ExceptionMessage.UNEXPECTED_END_OF_INPUT_LINE.getExceptionWithParam(lang, getLineNumber(tokenList));
    }

    default HandledError getUnexpectedEnd(Token token, String lang) {
        if (token != null)
            return ExceptionMessage.UNEXPECTED_END_OF_INPUT_LINE.getExceptionWithParam(lang, token.getLineNumber());
        else
            return ExceptionMessage.UNEXPECTED_END_OF_INPUT_LINE.getException(lang);
    }

    default void throwUnexpectedEnd(Token token, String lang) {
        throw getUnexpectedEnd(token, lang);
    }

    default Variable getVariableExpression(Token token, String lang) {
        boolean result = false;
        if (StringUtils.isNotBlank(token.getValue()))
            result = token.getValue().matches("^[a-zA-Z_$]\\S*")
                    && token.getValue().matches("([a-zA-Z_$0-9])\\w*");
        if (!result)
            this.throwTokenNotValid(token, lang);
        return new Variable(token.getValue());
    }

    default List<List<Token>> getSameLevelTokenListSeparateByComma(Integer level, List<Token> selectedTokenList) {
        List<Token> allElementTokenList = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            Token token = selectedTokenList.remove(0);
            if (token.getLevel().equals(level))
                break;
            else
                allElementTokenList.add(token);
        }
        List<List<Token>> listTokens = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();
        for (Token token : allElementTokenList) {
            if (token.getValue().equals(",") && token.getLevel().equals(level + 1)) {
                listTokens.add(tokens);
                tokens = new ArrayList<>();
            } else {
                tokens.add(token);
            }
        }
        if (CollectionUtils.isNotEmpty(tokens))
            listTokens.add(tokens);
        return listTokens;
    }

    default List<Token> getSameLevelTokenListBetweenTwoBrace(Integer level, List<Token> tokenList) {
        List<Token> allElementTokenList = new ArrayList<>();
        Stack<String> braceStack = new Stack<>();
        while (CollectionUtils.isNotEmpty(tokenList)) {
            Token token = tokenList.remove(0);
            if (token.getValue().equals("{") && token.getLevel().equals(level))
                braceStack.push("{");
            if (token.getValue().equals("}") && token.getLevel().equals(level))
                braceStack.pop();
            if (token.getLevel().compareTo(level) < 0) {
                break;
            } else {
                allElementTokenList.add(token);
            }
            if (CollectionUtils.isEmpty(braceStack))
                break;
        }
        return allElementTokenList;
    }

    default List<Token> getSameLevelTokenListBetweenTwoParenthesis(Integer level, List<Token> tokenList) {
        List<Token> allElementTokenList = new ArrayList<>();
        Stack<String> parenthesisStack = new Stack<>();
        while (CollectionUtils.isNotEmpty(tokenList)) {
            Token token = tokenList.remove(0);
            if (token.getValue().equals("(") && token.getLevel().equals(level))
                parenthesisStack.push("(");
            else if (token.getValue().equals(")") && token.getLevel().equals(level))
                parenthesisStack.pop();
            else if (token.getLevel().compareTo(level) < 0) {
                break;
            } else {
                allElementTokenList.add(token);
            }
            if (CollectionUtils.isEmpty(parenthesisStack))
                break;
        }
        return allElementTokenList;
    }
}
