package statamentgenerator;

import expression.TwoHandOperatorExpression;
import expression.VariableDeclaratorExpression;
import statement.ExpressionStatement;
import statement.HandledError;
import statement.Statement;
import statement.VariableDeclarationStatement;
import token.Token;

import java.util.ArrayList;
import java.util.List;

import static token.TokenType.KEYWORD;

public class VariableDeclarationStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Statement generate(List<Token> selectedTokenList, List<Token> tokenList) {
        Token token = getFirstTokenThatNotNewLine(selectedTokenList);
        VariableDeclarationStatement result = new VariableDeclarationStatement();
        StatementGenerator generator = new ExpressionStatementGeneratorImpl();
        if (token.getTokenType() == KEYWORD && isVariableKeyword(token.getValue())) {
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