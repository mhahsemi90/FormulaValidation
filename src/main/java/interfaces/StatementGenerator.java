package interfaces;

import exception.HandledError;
import expression.ExpressionType;
import expression.OneHandOperatorExpression;
import expression.TwoHandOperatorExpression;
import expression.Variable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import statamentgenerator.MainStatementGeneratorImpl;
import statement.Statement;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public interface StatementGenerator {
    Statement generate(List<Token> selectedTokenList, List<Token> tokenList);

    default List<Statement> parsing(String script) {
        return null;
    }

    default List<Statement> getAllStatementFromTokenList(List<Token> tokenList) {
        List<Statement> allStatementList = new ArrayList<>();
        StatementGenerator generator = new MainStatementGeneratorImpl();
        tokenList = validateAllParenthesisAndBrace(tokenList);
        while (CollectionUtils.isNotEmpty(tokenList)) {
            Statement result;
            List<Token> selectedTokenList = selectFirstTokenListToSemicolon(tokenList);
            List<Token> tempTokenList = selectedTokenList
                    .stream()
                    .map(Token::clone)
                    .collect(Collectors.toList());
            try {
                result = generator.generate(selectedTokenList, tokenList);
            } catch (Exception e) {
                selectedTokenList = selectFirstTokenListToLastNewLine(tempTokenList, tokenList);
                if (CollectionUtils.isNotEmpty(selectedTokenList)) {
                    result = generator.generate(selectedTokenList, tokenList);
                } else {
                    throw e;
                }
            }
            allStatementList.add(result);
        }
        return allStatementList;
    }

    default List<Token> validateAllParenthesisAndBrace(List<Token> tokenList) {
        Stack<String> parenthesisStack = new Stack<>();
        Stack<String> braceStack = new Stack<>();
        Stack<String> advancedBraceStack = new Stack<>();
        List<Token> tempTokenList = tokenList
                .stream()
                .map(Token::clone)
                .collect(Collectors.toList());
        while (CollectionUtils.isNotEmpty(tokenList)) {
            Token token = tokenList.remove(0);
            if (token.getValue().equals("{"))
                advancedBraceStack.push("{");
            if (token.getValue().equals("["))
                braceStack.push("[");
            if (token.getValue().equals("("))
                parenthesisStack.push("(");
            if (token.getValue().equals("}")) {
                if (advancedBraceStack.isEmpty()) {
                    throwHandledError(tokenList, token.getValue());
                }
                advancedBraceStack.pop();
            }
            if (token.getValue().equals("]")) {
                if (braceStack.isEmpty()) {
                    throwHandledError(tokenList, token.getValue());
                }
                braceStack.pop();
            }
            if (token.getValue().equals(")")) {
                if (parenthesisStack.isEmpty()) {
                    throwHandledError(tokenList, token.getValue());
                }
                parenthesisStack.pop();
            }
        }
        if (!advancedBraceStack.isEmpty())
            throwHandledError(tokenList, "{");
        if (!braceStack.isEmpty())
            throwHandledError(tokenList, "[");
        if (!parenthesisStack.isEmpty())
            throwHandledError(tokenList, "(");
        return tempTokenList;
    }

    default Statement getFirstStatementFromTokenList(List<Token> tokenList) {
        List<Token> selectedTokenList = selectFirstTokenListToSemicolon(tokenList);
        List<Token> tempTokenList = selectedTokenList
                .stream()
                .map(Token::clone)
                .collect(Collectors.toList());
        List<Statement> allStatementList = getAllStatementFromTokenList(selectedTokenList);
        if (CollectionUtils.isNotEmpty(allStatementList)
                && allStatementList.size() == 1) {
            return allStatementList.get(0);
        } else {
            selectedTokenList = selectFirstTokenListToLastNewLine(tempTokenList, tokenList);
            return getFirstStatementFromTokenList(selectedTokenList);
        }
    }

    default Integer getLineNumber(List<Token> selectedTokenList) {
        for (Token token : selectedTokenList) {
            if (token.getTokenType() == TokenType.NEW_LINE)
                return Integer.valueOf(token.getValue());
        }
        return 1;
    }

    default List<Token> selectFirstTokenListToSemicolon(List<Token> tokenList) {
        List<Token> selectedTokenList = new ArrayList<>();
        int size = tokenList.size();
        for (int i = 0; i < size; i++) {
            if (tokenList.get(0).getValue().equals(";")) {
                selectedTokenList.add(tokenList.remove(0));
                break;
            }
            selectedTokenList.add(tokenList.remove(0));
        }
        return selectedTokenList;
    }

    default List<Token> selectFirstTokenListToLastNewLine(List<Token> tempTokenList, List<Token> tokenList) {
        if (CollectionUtils.isNotEmpty(tempTokenList)) {
            if (tempTokenList.get(tempTokenList.size() - 1).getTokenType() == TokenType.NEW_LINE
                    || tempTokenList.get(tempTokenList.size() - 1).getValue().equals("}"))
                tokenList.add(0, tempTokenList.remove(tempTokenList.size() - 1));
            for (int i = tempTokenList.size() - 1; i >= 0; i--) {
                if (tempTokenList.get(i).getTokenType() == TokenType.NEW_LINE
                        || tempTokenList.get(i).getValue().equals("}"))
                    break;
                else
                    tokenList.add(0, tempTokenList.remove(i));
            }
        }
        return tempTokenList;
    }

    default Token getFirstTokenThatNotNewLine(List<Token> selectedTokenList) {
        Token token = null;
        Token newLineToken = null;
        while (CollectionUtils.isNotEmpty(selectedTokenList)) {
            if (selectedTokenList.get(0).getTokenType() != TokenType.NEW_LINE) {
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

    default List<String> getAssignList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("=");
        stringList.add("*=");
        stringList.add("**=");
        stringList.add("/=");
        stringList.add("%=");
        stringList.add("+=");
        stringList.add("-=");
        stringList.add("<<=");
        stringList.add(">>=");
        stringList.add(">>>=");
        stringList.add("&=");
        stringList.add("^=");
        stringList.add("|=");
        return stringList;
    }

    default List<String> getBinaryList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("+");
        stringList.add("-");
        stringList.add("*");
        stringList.add("/");
        stringList.add("%");
        stringList.add("<<");
        stringList.add(">>");
        stringList.add(">>>");
        stringList.add(">");
        stringList.add("<");
        stringList.add(">=");
        stringList.add("<=");
        stringList.add("==");
        stringList.add("===");
        stringList.add("!=");
        stringList.add("!==");
        stringList.add("&");
        stringList.add("|");
        stringList.add("^");
        return stringList;
    }

    default List<String> getUpdateUnaryList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("--");
        stringList.add("++");
        return stringList;
    }

    default List<String> getUnaryList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("-");
        stringList.add("+");
        stringList.add("!");
        return stringList;
    }

    default List<String> getLogicalList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("&&");
        stringList.add("||");
        return stringList;
    }

    default TwoHandOperatorExpression getOperatorAndTypeOfTwoHandOperatorExpression(Token token) {
        TwoHandOperatorExpression operatorExpression = new TwoHandOperatorExpression();
        operatorExpression.setOperator(token.getValue());
        if (token.getValue().equals("("))
            operatorExpression.setType(ExpressionType.PARENTHESIS_EXPRESSION);
        else if (getBinaryList().contains(token.getValue()))
            operatorExpression.setType(ExpressionType.BINARY_EXPRESSION);
        else if (getAssignList().contains(token.getValue()))
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

    default void throwHandledError(List<Token> tokenList, String value) {
        Integer lineNumber = getLineNumber(tokenList);
        throw new HandledError("Token not valid " + value + " line:" + lineNumber);
    }

    default Variable getVariableExpression(List<Token> selectedTokenList, String value) {
        boolean result = false;
        if (StringUtils.isNotBlank(value))
            result = value.matches("^[a-zA-Z_$]\\S*")
                    && value.matches("([a-zA-Z_$0-9])\\w*");
        if (!result)
            this.throwHandledError(selectedTokenList, value);
        return new Variable(value);
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
