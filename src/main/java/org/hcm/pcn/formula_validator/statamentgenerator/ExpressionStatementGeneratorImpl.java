package org.hcm.pcn.formula_validator.statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.hcm.pcn.formula_validator.expression.*;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.token.Token;
import org.hcm.pcn.formula_validator.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class ExpressionStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList) {
        ExpressionStatement result = new ExpressionStatement();
        Expression currentExpression = null;
        Expression lastExpression = null;
        List<String> assignList = getAssignList();
        List<String> binaryList = getBinaryList();
        List<String> updateUnaryList = getUpdateUnaryList();
        List<String> logicalList = getLogicalList();
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            Token token = removeFirstTokenThatNotNewLine(selectedTokenList);
            if (token.getValue().equals("]")
                    || token.getValue().equals(",")
                    || token.getValue().equals("}")) {
                throwTokenNotValid(selectedTokenList, token.getValue());
                break;
            } else if (token.getTokenType() == TokenType.PUNCTUATOR &&
                    currentExpression == null) {
                switch (token.getValue()) {
                    case "{" -> {
                        StatementGenerator statementGenerator = new BlockStatementGeneratorImpl();
                        selectedTokenList.add(0, token);
                        tokenList.addAll(0, selectedTokenList);
                        selectedTokenList = getSameLevelTokenListBetweenTwoBrace(token.getLevel(), tokenList);
                        return statementGenerator.generate(
                                selectedTokenList
                                , tokenList
                        );
                    }
                    case "[" -> {
                        lastExpression = getArrayExpression(token.getLevel(), selectedTokenList);
                        currentExpression = lastExpression;
                    }
                    case "(" -> {
                        lastExpression = getOperatorAndTypeOfTwoHandOperatorExpression(token);
                        currentExpression = lastExpression;
                    }
                    case "++", "--" -> {
                        Token nextToken = removeFirstTokenThatNotNewLine(selectedTokenList);
                        lastExpression = getUpdateExpression(token, nextToken, selectedTokenList);
                        currentExpression = lastExpression;
                    }
                    case "!", "+", "-" -> {
                        lastExpression = getUnaryExpression(token, selectedTokenList);
                        currentExpression = lastExpression;
                    }
                    default -> throwTokenNotValid(selectedTokenList, token.getValue());
                }
            } else if (token.getTokenType() == TokenType.PUNCTUATOR &&
                    currentExpression != null &&
                    lastExpression != null) {
                if (token.getValue().equals("[")) {
                    switch (lastExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            lastExpression = getArrayExpression(token.getLevel(), selectedTokenList);
                            if (!setInAvailableChild(
                                    (TwoHandOperatorExpression) currentExpression,
                                    lastExpression
                            )) {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        case ARRAY_EXPRESSION, CALL_EXPRESSION, LITERAL_EXPRESSION
                                , MEMBER_EXPRESSION, VARIABLE_EXPRESSION -> {
                            if (currentExpression == lastExpression) {
                                lastExpression = replaceNewExpressionToCurrentExpression(
                                        lastExpression,
                                        getMemberExpression(token.getLevel(), lastExpression, selectedTokenList)
                                );
                                currentExpression = lastExpression;
                            } else {
                                lastExpression = replaceNewExpressionToCurrentExpression(
                                        lastExpression,
                                        getMemberExpression(token.getLevel(), lastExpression, selectedTokenList)
                                );
                            }
                        }
                        case UNARY_EXPRESSION, UPDATE_EXPRESSION -> {
                            OneHandOperatorExpression operatorExpression = (OneHandOperatorExpression) lastExpression;
                            operatorExpression.setArgument(
                                    getMemberExpression(token.getLevel(), operatorExpression.getArgument(), selectedTokenList)
                            );
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (token.getValue().equals("{")) {
                    switch (lastExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            lastExpression = getObjectExpression(token.getLevel(), selectedTokenList);
                            if (!this.setInAvailableChild(
                                    (TwoHandOperatorExpression) currentExpression,
                                    lastExpression)
                            ) {
                                this.throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        case UNARY_EXPRESSION -> {
                            OneHandOperatorExpression operatorExpression = (OneHandOperatorExpression) lastExpression;
                            if (operatorExpression.getArgument() == null ||
                                    operatorExpression.getArgument().getType() == ExpressionType.UNARY_EXPRESSION)
                                operatorExpression.setArgument(
                                        getObjectExpression(token.getLevel(), selectedTokenList)
                                );
                            else
                                this.throwTokenNotValid(selectedTokenList, token.getValue());
                        }
                        default -> this.throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (token.getValue().equals(":")) {
                    if (lastExpression.getType() == ExpressionType.VARIABLE_EXPRESSION &&
                            lastExpression.getParent() == null &&
                            lastExpression == currentExpression) {
                        StatementGenerator labeledStatementGenerator = new LabeledStatementGeneratorImpl();
                        Variable variable = (Variable) lastExpression;
                        if (CollectionUtils.isNotEmpty(selectedTokenList)) {
                            if (selectedTokenList.get(0).getValue().equals("{")) {
                                List<Token> sameLevelTokenBetweenTwoBrace = getSameLevelTokenListBetweenTwoBrace(
                                        token.getLevel(),
                                        selectedTokenList
                                );
                                tokenList.addAll(0, selectedTokenList);
                                sameLevelTokenBetweenTwoBrace.add(0, token);
                                sameLevelTokenBetweenTwoBrace.add(0, new Token(TokenType.VARIABLE, variable.getIdName(), 0));
                                return labeledStatementGenerator.generate(sameLevelTokenBetweenTwoBrace, tokenList);
                            } else {
                                selectedTokenList.add(0, token);
                                selectedTokenList.add(0, new Token(TokenType.VARIABLE, variable.getIdName(), 0));
                                return labeledStatementGenerator.generate(selectedTokenList, tokenList);
                            }
                        } else {
                            throwTokenNotValid(selectedTokenList, token.getValue());
                        }
                    } else {
                        throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (token.getValue().equals("(")) {
                    switch (lastExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            lastExpression = getOperatorAndTypeOfTwoHandOperatorExpression(token);
                            if (!setInAvailableChild(
                                    (TwoHandOperatorExpression) currentExpression,
                                    lastExpression
                            )) {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                            currentExpression = lastExpression;
                        }
                        case ARRAY_EXPRESSION, CALL_EXPRESSION,
                                LITERAL_EXPRESSION, MEMBER_EXPRESSION,
                                VARIABLE_EXPRESSION -> lastExpression = replaceNewExpressionToCurrentExpression(
                                lastExpression,
                                getCallExpression(token.getLevel(), lastExpression, selectedTokenList)
                        );
                        case UNARY_EXPRESSION, UPDATE_EXPRESSION -> {
                            OneHandOperatorExpression operatorExpression = (OneHandOperatorExpression) lastExpression;
                            operatorExpression.setArgument(
                                    getCallExpression(token.getLevel(), operatorExpression.getArgument(), selectedTokenList)
                            );
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (token.getValue().equals(")")) {
                    switch (currentExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION -> {
                            TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) currentExpression;
                            if (operatorExpression.getParent() == null) {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            } else {
                                if (operatorExpression.getLeftChild() == null
                                        || operatorExpression.getRightChild() == null) {
                                    throwTokenNotValid(selectedTokenList, token.getValue());
                                }
                            }
                            currentExpression = operatorExpression.getParent();
                        }
                        case PARENTHESIS_EXPRESSION -> {
                            Expression expression = removeParenthesis((TwoHandOperatorExpression) currentExpression);
                            if (expression == null)
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            else
                                currentExpression = expression;
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (token.getValue().equals("?")) {
                    switch (currentExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION,
                                ARRAY_EXPRESSION, CALL_EXPRESSION,
                                LITERAL_EXPRESSION, MEMBER_EXPRESSION,
                                VARIABLE_EXPRESSION, UNARY_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            ConditionalExpression conditionalExpression = creatConditionExpression(
                                    token.getLevel(),
                                    currentExpression,
                                    selectedTokenList
                            );
                            if (conditionalExpression != null) {
                                lastExpression = conditionalExpression;
                                if (lastExpression.getParent() != null)
                                    currentExpression = lastExpression.getParent();
                                else
                                    currentExpression = lastExpression;
                            } else {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (updateUnaryList.contains(token.getValue())) {
                    switch (lastExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            Token nextToken = removeFirstTokenThatNotNewLine(selectedTokenList);
                            if (nextToken.getTokenType() == TokenType.VARIABLE) {
                                lastExpression = getUpdateExpression(token, nextToken, selectedTokenList);
                                if (!setInAvailableChild(
                                        (TwoHandOperatorExpression) currentExpression,
                                        lastExpression)) {
                                    throwTokenNotValid(selectedTokenList, token.getValue());
                                }
                            } else {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        case MEMBER_EXPRESSION, VARIABLE_EXPRESSION, UNARY_EXPRESSION ->
                                lastExpression = getUpdateExpression(token, lastExpression);
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (binaryList.contains(token.getValue()) ||
                        logicalList.contains(token.getValue())) {
                    switch (currentExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION -> {
                            TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) currentExpression;
                            if (operatorExpression.getLeftChild() != null &&
                                    operatorExpression.getRightChild() != null) {
                                if (getOperatorPrecedence(token.getValue())
                                        > getOperatorPrecedence(operatorExpression.getOperator())) {
                                    lastExpression = setCurrentTokenAsRightChildAndAddRightChildToLeft(
                                            (TwoHandOperatorExpression) currentExpression,
                                            token);
                                } else {
                                    lastExpression = setCurrentTokenAsParentOfCurrentExpressionAndSetLeftChild(
                                            currentExpression,
                                            token);
                                }
                                currentExpression = lastExpression;
                            } else {
                                if (getUnaryList().contains(token.getValue())) {
                                    lastExpression = getUnaryExpression(token, selectedTokenList);
                                    if (!setInAvailableChild(
                                            (TwoHandOperatorExpression) currentExpression,
                                            lastExpression)) {
                                        throwTokenNotValid(selectedTokenList, token.getValue());
                                    }
                                } else {
                                    throwTokenNotValid(selectedTokenList, token.getValue());
                                }
                            }
                        }
                        case PARENTHESIS_EXPRESSION -> {
                            TwoHandOperatorExpression operatorExpression1 = (TwoHandOperatorExpression) currentExpression;
                            if (operatorExpression1.getLeftChild() == null) {
                                if (getUnaryList().contains(token.getValue())) {
                                    lastExpression = getUnaryExpression(token, selectedTokenList);
                                    if (!setInAvailableChild(
                                            (TwoHandOperatorExpression) currentExpression,
                                            lastExpression)) {
                                        throwTokenNotValid(selectedTokenList, token.getValue());
                                    }
                                } else {
                                    throwTokenNotValid(selectedTokenList, token.getValue());
                                }
                            } else if (operatorExpression1.getRightChild() == null) {
                                lastExpression = replaceCurrentTokenToCurrentExpressionAndSetLeftChild(
                                        (TwoHandOperatorExpression) currentExpression,
                                        token);
                                currentExpression = lastExpression;
                            } else {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        case ARRAY_EXPRESSION, CALL_EXPRESSION,
                                LITERAL_EXPRESSION, MEMBER_EXPRESSION,
                                VARIABLE_EXPRESSION, UNARY_EXPRESSION,
                                UPDATE_EXPRESSION, CONDITIONAL_EXPRESSION -> {
                            lastExpression = setCurrentTokenAsParentOfCurrentExpressionAndSetLeftChild(
                                    currentExpression,
                                    token);
                            currentExpression = lastExpression;
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                } else if (assignList.contains(token.getValue())) {
                    switch (currentExpression.getType()) {
                        case PARENTHESIS_EXPRESSION -> {
                            TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) currentExpression;
                            if (operatorExpression.getLeftChild() != null
                                    && operatorExpression.getRightChild() == null) {
                                if (operatorExpression.getLeftChild().getType() == ExpressionType.ARRAY_EXPRESSION ||
                                        operatorExpression.getLeftChild().getType() == ExpressionType.MEMBER_EXPRESSION ||
                                        operatorExpression.getLeftChild().getType() == ExpressionType.VARIABLE_EXPRESSION) {
                                    lastExpression = getAssignmentExpression(
                                            token,
                                            operatorExpression,
                                            selectedTokenList);
                                    currentExpression = lastExpression;
                                } else {
                                    throwTokenNotValid(selectedTokenList, token.getValue());
                                }
                            } else {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        case ARRAY_EXPRESSION, MEMBER_EXPRESSION, VARIABLE_EXPRESSION -> {
                            lastExpression = getAssignmentExpression(
                                    token,
                                    currentExpression,
                                    selectedTokenList);
                            currentExpression = lastExpression;
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                }
            } else if (token.getTokenType() == TokenType.VARIABLE) {
                if (currentExpression == null) {
                    lastExpression = getVariableExpression(selectedTokenList, token.getValue());
                    currentExpression = lastExpression;
                } else {
                    switch (currentExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            lastExpression = getVariableExpression(selectedTokenList, token.getValue());
                            if (!setInAvailableChild(
                                    (TwoHandOperatorExpression) currentExpression,
                                    lastExpression)) {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                }
            } else if (token.getTokenType() == TokenType.LITERAL) {
                if (currentExpression == null) {
                    lastExpression = new Literal(token.getValue());
                    currentExpression = lastExpression;
                } else {
                    switch (currentExpression.getType()) {
                        case BINARY_EXPRESSION, LOGICAL_EXPRESSION, PARENTHESIS_EXPRESSION -> {
                            lastExpression = new Literal(token.getValue());
                            if (!setInAvailableChild(
                                    (TwoHandOperatorExpression) currentExpression,
                                    lastExpression)) {
                                throwTokenNotValid(selectedTokenList, token.getValue());
                            }
                        }
                        default -> throwTokenNotValid(selectedTokenList, token.getValue());
                    }
                }
            } else if (token.getTokenType() != TokenType.NEW_LINE) {
                throwTokenNotValid(selectedTokenList, token.getValue());
            }
        }
        verifyExpression(true, currentExpression, selectedTokenList);
        result.setExpression(currentExpression);
        return currentExpression != null ? Optional.of(result) : Optional.empty();
    }

    private Expression getAssignmentExpression(Token currentToken, Expression currentExpression, List<Token> selectedTokenList) {
        TwoHandOperatorExpression assignmentExpression;
        Expression leftChild;
        if (currentExpression.getType() == ExpressionType.PARENTHESIS_EXPRESSION)
            leftChild = ((TwoHandOperatorExpression) currentExpression).getLeftChild();
        else
            leftChild = currentExpression;
        if (leftChild.getType() == ExpressionType.ARRAY_EXPRESSION) {
            boolean allIsVariable = true;
            for (Expression expression : ((ArrayExpression) leftChild).getElementList()) {
                allIsVariable = allIsVariable && expression instanceof Variable;
            }
            if (allIsVariable)
                leftChild.setType(ExpressionType.ARRAY_PATTERN_EXPRESSION);
            else
                throwTokenNotValid(selectedTokenList, currentToken.getValue());
        }
        List<Token> rightChildTokenList = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            Token token = selectedTokenList.remove(0);
            if (token.getLevel().compareTo(currentToken.getLevel()) < 0) {
                break;
            } else {
                rightChildTokenList.add(token);
            }
        }
        Expression rightChild = ((ExpressionStatement) generate(rightChildTokenList, new ArrayList<>())
                .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                .getExpression();
        assignmentExpression = getOperatorAndTypeOfTwoHandOperatorExpression(currentToken);
        assignmentExpression.setOperator(currentToken.getValue());
        assignmentExpression.setLeftChild(leftChild);
        assignmentExpression.setRightChild(rightChild);
        return assignmentExpression;
    }

    private Expression replaceNewExpressionToCurrentExpression(Expression currentExpression, Expression newExpression) {
        if (currentExpression.getParent() != null) {
            TwoHandOperatorExpression parent = (TwoHandOperatorExpression) currentExpression.getParent();
            if (parent.getLeftChild() == currentExpression) {
                parent.setLeftChild(newExpression);
            } else {
                parent.setRightChild(newExpression);
            }
            newExpression.setParent(parent);
        }
        return newExpression;
    }

    private Expression replaceCurrentTokenToCurrentExpressionAndSetLeftChild(TwoHandOperatorExpression currentExpression, Token token) {
        TwoHandOperatorExpression operatorExpression = getOperatorExpression(currentExpression, token);
        operatorExpression.setLeftChild(currentExpression.getLeftChild());
        currentExpression.getLeftChild().setParent(operatorExpression);
        currentExpression.setLeftChild(null);
        currentExpression.setParent(null);
        return operatorExpression;
    }

    private TwoHandOperatorExpression getOperatorExpression(Expression currentExpression, Token token) {
        TwoHandOperatorExpression operatorExpression = getOperatorAndTypeOfTwoHandOperatorExpression(token);
        replaceNewExpressionToCurrentExpression(currentExpression, operatorExpression);
        return operatorExpression;
    }

    private Expression setCurrentTokenAsParentOfCurrentExpressionAndSetLeftChild(Expression currentExpression, Token token) {
        TwoHandOperatorExpression operatorExpression = getOperatorExpression(currentExpression, token);
        operatorExpression.setLeftChild(currentExpression);
        currentExpression.setParent(operatorExpression);
        return operatorExpression;
    }

    private Expression setCurrentTokenAsRightChildAndAddRightChildToLeft(TwoHandOperatorExpression currentExpression, Token token) {
        TwoHandOperatorExpression operatorExpression = getOperatorAndTypeOfTwoHandOperatorExpression(token);
        Expression rightChild = currentExpression.getRightChild();
        operatorExpression.setLeftChild(rightChild);
        rightChild.setParent(operatorExpression);
        currentExpression.setRightChild(operatorExpression);
        operatorExpression.setParent(currentExpression);
        return operatorExpression;
    }

    private ConditionalExpression creatConditionExpression(Integer level, Expression expression, List<Token> selectedTokenList) {
        ConditionalExpression conditionalExpression = null;
        List<Token> consequentTokenList = new ArrayList<>();
        List<Token> alternateTokenList = new ArrayList<>();
        Stack<String> colonStack = new Stack<>();
        colonStack.add(":");
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            Token token = selectedTokenList.remove(0);
            if (token.getValue().equals("?") && token.getLevel().equals(level))
                colonStack.push(":");
            if (token.getValue().equals(":") && token.getLevel().equals(level))
                colonStack.pop();
            if (token.getLevel().compareTo(level) < 0) {
                break;
            } else {
                if (!colonStack.isEmpty())
                    consequentTokenList.add(token);
                else if (!token.getValue().equals(":"))
                    alternateTokenList.add(token);
            }
        }
        if (CollectionUtils.isNotEmpty(consequentTokenList) &&
                CollectionUtils.isNotEmpty(alternateTokenList)) {
            conditionalExpression = new ConditionalExpression();
            if (expression.getParent() != null) {
                TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) expression.getParent();
                if (operatorExpression.getLeftChild() == expression)
                    operatorExpression.setLeftChild(conditionalExpression);
                else
                    operatorExpression.setRightChild(conditionalExpression);
                conditionalExpression.setParent(operatorExpression);
                expression.setParent(null);
            }
            if (expression.getType() == ExpressionType.PARENTHESIS_EXPRESSION) {
                conditionalExpression.setTest(((TwoHandOperatorExpression) expression).getLeftChild());
                ((TwoHandOperatorExpression) expression).setLeftChild(null);
            } else {
                conditionalExpression.setTest(expression);
            }
            conditionalExpression.setConsequent(
                    ((ExpressionStatement) generate(consequentTokenList, new ArrayList<>())
                            .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
            conditionalExpression.setAlternate(
                    ((ExpressionStatement) generate(alternateTokenList, new ArrayList<>())
                            .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
        }
        return conditionalExpression;
    }

    private Expression removeParenthesis(TwoHandOperatorExpression expression) {
        Expression result = null;
        if (!(expression.getLeftChild() == null || expression.getRightChild() != null)) {
            if (expression.getParent() != null) {
                TwoHandOperatorExpression operatorExpression = (TwoHandOperatorExpression) expression.getParent();
                if (operatorExpression.getLeftChild() == expression)
                    operatorExpression.setLeftChild(expression.getLeftChild());
                else
                    operatorExpression.setRightChild(expression.getLeftChild());
                expression.getLeftChild().setParent(operatorExpression);
                expression.setLeftChild(null);
                expression.setParent(null);
                result = operatorExpression;
            } else {
                result = expression.getLeftChild();
                expression.setLeftChild(null);
            }
        }
        return result;
    }

    private void verifyExpression(Boolean first, Expression currentExpression, List<Token> tokenList) {
        if (CollectionUtils.isNotEmpty(tokenList)) {
            throwUnexpectedEnd(tokenList);
        }
        if (currentExpression != null && currentExpression.getType() == ExpressionType.PARENTHESIS_EXPRESSION) {
            if (currentExpression.getParent() != null || !first)
                throwTokenNotValid(tokenList, "(");
        } else if (currentExpression instanceof TwoHandOperatorExpression operatorExpression) {
            if (operatorExpression.getLeftChild() == null
                    || operatorExpression.getRightChild() == null) {
                throwTokenNotValid(tokenList, operatorExpression.getOperator());
            } else if (operatorExpression.getParent() != null) {
                verifyExpression(false, operatorExpression.getParent(), tokenList);
            }
        }
    }

    private Boolean setInAvailableChild(TwoHandOperatorExpression currentExpression, Expression lastExpression) {
        boolean result = false;
        if (currentExpression.getLeftChild() == null) {
            currentExpression.setLeftChild(lastExpression);
            lastExpression.setParent(currentExpression);
            result = true;
        } else if (currentExpression.getRightChild() == null) {
            currentExpression.setRightChild(lastExpression);
            lastExpression.setParent(currentExpression);
            result = true;
        }
        return result;
    }

    private Expression getUnaryExpression(Token token, List<Token> selectedTokenList) {
        OneHandOperatorExpression unaryExpression = getOperatorAndTypeOfOneHandOperatorExpression(token);
        Token nextToken = removeFirstTokenThatNotNewLine(selectedTokenList);
        if (nextToken.getTokenType() == TokenType.PUNCTUATOR
                && getUnaryList().contains(token.getValue())) {
            unaryExpression.setArgument(getUnaryExpression(nextToken, selectedTokenList));
        } else if (nextToken.getTokenType() == TokenType.VARIABLE) {
            unaryExpression.setArgument(getVariableExpression(selectedTokenList, nextToken.getValue()));
        } else if (nextToken.getTokenType() == TokenType.LITERAL) {
            unaryExpression.setArgument(new Literal(nextToken.getValue()));
        } else if (nextToken.getValue().equals("[")) {
            unaryExpression.setArgument(getArrayExpression(nextToken.getLevel(), selectedTokenList));
        } else if (nextToken.getValue().equals("{")) {
            unaryExpression.setArgument(getObjectExpression(nextToken.getLevel(), selectedTokenList));
        } else if (nextToken.getValue().equals("(")) {
            List<Token> allElementTokenList = new ArrayList<>();
            while (CollectionUtils.isNotEmpty(selectedTokenList)) {
                Token token1 = selectedTokenList.remove(0);
                if (token1.getLevel().compareTo(nextToken.getLevel()) == 0)
                    break;
                else
                    allElementTokenList.add(token1);
            }
            unaryExpression.setArgument(
                    ((ExpressionStatement) generate(allElementTokenList, new ArrayList<>())
                            .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
        } else {
            throwTokenNotValid(selectedTokenList, token.getValue());
        }
        return unaryExpression;
    }

    private Expression getUpdateExpression(Token token, Token nextToken, List<Token> selectedTokenList) {
        OneHandOperatorExpression updateExpression = getOperatorAndTypeOfOneHandOperatorExpression(token);
        if (nextToken.getTokenType() == TokenType.VARIABLE) {
            updateExpression.setArgument(getVariableExpression(selectedTokenList, token.getValue()));
            updateExpression.setOperator(token.getValue());
        } else {
            throwTokenNotValid(selectedTokenList, token.getValue());
        }
        return updateExpression;
    }

    private Expression getUpdateExpression(Token token, Expression expression) {
        OneHandOperatorExpression updateExpression = getOperatorAndTypeOfOneHandOperatorExpression(token);
        updateExpression.setArgument(expression);
        updateExpression.setOperator(token.getValue());
        updateExpression.setParent(expression.getParent());
        expression.setParent(null);
        return updateExpression;
    }

    private Expression getArrayExpression(Integer level, List<Token> selectedTokenList) {
        ArrayExpression arrayExpression = new ArrayExpression();
        for (List<Token> listToken : getSameLevelTokenListSeparateByComma(level, selectedTokenList)) {
            arrayExpression.getElementList().add(
                    ((ExpressionStatement) generate(listToken, new ArrayList<>())
                            .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
        }
        return arrayExpression;
    }

    private Expression getObjectExpression(Integer level, List<Token> selectedTokenList) {
        ObjectExpression objectExpression = new ObjectExpression();
        for (List<Token> listToken : getSameLevelTokenListSeparateByComma(level, selectedTokenList)) {
            PropertyExpression property = new PropertyExpression();
            if (listToken.size() > 2
                    && listToken.get(0).getTokenType() == TokenType.VARIABLE
                    && listToken.get(1).getValue().equals(":")) {
                property.setKey(getVariableExpression(selectedTokenList, listToken.remove(0).getValue()));
                listToken.remove(0);
                property.setValue(((ExpressionStatement) generate(listToken, new ArrayList<>())
                        .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                        .getExpression());
                objectExpression.getPropertyList().add(property);
            } else {
                this.throwTokenNotValid(selectedTokenList, "{");
            }
        }
        return objectExpression;
    }

    private Expression getMemberExpression(Integer level, Expression objectExpression, List<Token> selectedTokenList) {
        MemberExpression memberExpression = new MemberExpression();
        List<Expression> expressionList = new ArrayList<>();
        for (List<Token> listToken : getSameLevelTokenListSeparateByComma(level, selectedTokenList)) {
            expressionList.add(
                    ((ExpressionStatement) generate(listToken, new ArrayList<>())
                            .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
        }
        memberExpression.setObject(objectExpression);
        if (expressionList.size() == 0) {
            throwTokenNotValid(selectedTokenList, "[");
        } else if (expressionList.size() == 1) {
            memberExpression.setProperty(expressionList.get(0));
        } else {
            SequenceExpression sequenceExpression = new SequenceExpression();
            for (Expression expression : expressionList) {
                sequenceExpression.getExpressionList().add(expression);
            }
            memberExpression.setProperty(sequenceExpression);
        }
        return memberExpression;
    }

    private Expression getCallExpression(Integer level, Expression objectExpression, List<Token> selectedTokenList) {
        CallExpression callExpression = new CallExpression();
        List<Expression> argumentExpressionList = new ArrayList<>();
        for (List<Token> listToken : getSameLevelTokenListSeparateByComma(level, selectedTokenList)) {
            argumentExpressionList.add(
                    ((ExpressionStatement) generate(listToken, new ArrayList<>())
                            .orElseThrow(()->unexpectedEndError(selectedTokenList)))
                            .getExpression()
            );
        }
        callExpression.setCallVariableName(objectExpression);
        callExpression.setArgumentList(argumentExpressionList);
        return callExpression;
    }


}
