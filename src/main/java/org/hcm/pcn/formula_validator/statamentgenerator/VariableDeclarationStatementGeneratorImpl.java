package org.hcm.pcn.formula_validator.statamentgenerator;

import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.expression.TwoHandOperatorExpression;
import org.hcm.pcn.formula_validator.expression.VariableDeclaratorExpression;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.statement.VariableDeclarationStatement;
import org.hcm.pcn.formula_validator.token.Token;
import org.hcm.pcn.formula_validator.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class VariableDeclarationStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Statement generate(List<Token> selectedTokenList, List<Token> tokenList) {
        Token token = getFirstTokenThatNotNewLine(selectedTokenList);
        VariableDeclarationStatement result = new VariableDeclarationStatement();
        StatementGenerator generator = new ExpressionStatementGeneratorImpl();
        if (token.getTokenType() == TokenType.KEYWORD && isVariableKeyword(token.getValue())) {
            result.setKind(token.getValue());
        } else {
            throw new HandledError(
                    "Token \"" + token.getValue() + "\" not valid Line:" + getLineNumber(selectedTokenList)
            );
        }
        List<List<Token>> listTokens = getSameLevelTokenListSeparateByComma(
                token.getLevel() - 1,
                selectedTokenList
        );
        for (List<Token> listToken : listTokens) {
            TwoHandOperatorExpression expression = (TwoHandOperatorExpression) (
                    (ExpressionStatement) generator
                            .generate(listToken, new ArrayList<>()))
                    .getExpression();
            result.getDeclaratorExpressionList().add(
                    new VariableDeclaratorExpression(expression.getLeftChild(), expression.getRightChild())
            );
        }
        return result;
    }
}