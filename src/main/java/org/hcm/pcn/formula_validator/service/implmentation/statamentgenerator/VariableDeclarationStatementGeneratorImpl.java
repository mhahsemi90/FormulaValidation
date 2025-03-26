package org.hcm.pcn.formula_validator.service.implmentation.statamentgenerator;

import org.hcm.pcn.formula_validator.enums.ExpressionType;
import org.hcm.pcn.formula_validator.service.expression.Expression;
import org.hcm.pcn.formula_validator.service.expression.TwoHandOperatorExpression;
import org.hcm.pcn.formula_validator.service.expression.Variable;
import org.hcm.pcn.formula_validator.service.expression.VariableDeclaratorExpression;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.statement.ExpressionStatement;
import org.hcm.pcn.formula_validator.service.statement.Statement;
import org.hcm.pcn.formula_validator.service.statement.VariableDeclarationStatement;
import org.hcm.pcn.formula_validator.service.token.Token;
import org.hcm.pcn.formula_validator.service.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VariableDeclarationStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Optional<Statement> generate(List<Token> selectedTokenList, List<Token> tokenList, String lang) {
        Token token = removeFirstTokenThatNotNewLine(selectedTokenList);
        VariableDeclarationStatement result = new VariableDeclarationStatement();
        StatementGenerator generator = new ExpressionStatementGeneratorImpl();
        if (token.getTokenType() == TokenType.KEYWORD && isVariableKeyword(token.getValue())) {
            result.setKind(token.getValue());
        } else {
            throwTokenNotValid(token, lang);
        }
        List<List<Token>> listTokens = getSameLevelTokenListSeparateByComma(
                token.getLevel() - 1,
                selectedTokenList
        );
        for (List<Token> listToken : listTokens) {
            Expression expression = (
                    (ExpressionStatement) generator
                            .generate(listToken, new ArrayList<>(), lang)
                            .orElseThrow(() -> unexpectedEndError(listToken, lang)))
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
                            && expression.getType() == ExpressionType.ASSIGNMENT_EXPRESSION
            )
                result.getDeclaratorExpressionList().add(
                        new VariableDeclaratorExpression(
                                ((TwoHandOperatorExpression) expression).getLeftChild(),
                                ((TwoHandOperatorExpression) expression).getRightChild()
                        )
                );
            else
                throwTokenNotValid(token, lang);
        }
        return Optional.of(result);
    }
}
