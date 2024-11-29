package org.hcm.pcn.formula_validator.statamentgenerator;

import org.hcm.pcn.formula_validator.expression.Expression;
import org.hcm.pcn.formula_validator.expression.TwoHandOperatorExpression;
import org.hcm.pcn.formula_validator.expression.Variable;
import org.hcm.pcn.formula_validator.expression.VariableDeclaratorExpression;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.statement.Statement;
import org.hcm.pcn.formula_validator.statement.VariableDeclarationStatement;
import org.hcm.pcn.formula_validator.token.Token;
import org.hcm.pcn.formula_validator.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hcm.pcn.formula_validator.expression.ExpressionType.ASSIGNMENT_EXPRESSION;

public class VariableDeclarationStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList) {
        Token token = removeFirstTokenThatNotNewLine(selectedTokenList);
        VariableDeclarationStatement result = new VariableDeclarationStatement();
        StatementGenerator generator = new ExpressionStatementGeneratorImpl();
        if (token.getTokenType() == TokenType.KEYWORD && isVariableKeyword(token.getValue())) {
            result.setKind(token.getValue());
        } else {
            throwTokenNotValid(selectedTokenList, token.getValue());
        }
        List<List<Token>> listTokens = getSameLevelTokenListSeparateByComma(
                token.getLevel() - 1,
                selectedTokenList
        );
        for (List<Token> listToken : listTokens) {
            Expression expression = (
                    (ExpressionStatement) generator
                            .generate(listToken, new ArrayList<>())
                            .orElseThrow(() -> unexpectedEndError(tokenList)))
                    .getExpression();
            if (expression instanceof Variable)
                result.getDeclaratorExpressionList().add(
                        new VariableDeclaratorExpression(
                                expression,
                                null
                        )
                );
            else if (
                    expression instanceof TwoHandOperatorExpression
                            && expression.getType() == ASSIGNMENT_EXPRESSION
            )
                result.getDeclaratorExpressionList().add(
                        new VariableDeclaratorExpression(
                                ((TwoHandOperatorExpression) expression).getLeftChild(),
                                ((TwoHandOperatorExpression) expression).getRightChild()
                        )
                );
            else
                throwTokenNotValid(selectedTokenList, token.getValue());
        }
        return Optional.of(result);
    }
}
