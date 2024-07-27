package statamentgenerator;

import expression.Variable;
import org.apache.commons.collections4.CollectionUtils;
import statement.LabeledStatement;
import statement.Statement;
import token.Token;

import java.util.List;

import static token.TokenType.VARIABLE;

public class LabeledStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Statement generate(List<Token> selectedTokenList, List<Token> tokenList) {
        LabeledStatement labeledStatement = new LabeledStatement();
        if (CollectionUtils.isNotEmpty(selectedTokenList) &&
                selectedTokenList.size() > 2 &&
                selectedTokenList.get(0).getTokenType() == VARIABLE &&
                selectedTokenList.get(1).getValue().equals(":")) {
            Variable variable = getVariableExpression(selectedTokenList, selectedTokenList.get(0).getValue());
            selectedTokenList.remove(0);
            selectedTokenList.remove(0);
            if (selectedTokenList.get(0).getValue().equals("{")) {
                StatementGenerator blockStatementGenerator = new BlockStatementGeneratorImpl();
                Statement blockStatement = blockStatementGenerator.generate(selectedTokenList, tokenList);
                labeledStatement = new LabeledStatement(variable, blockStatement);
            } else {
                StatementGenerator statementGenerator = new MainStatementGeneratorImpl();
                Statement statement = statementGenerator.getFirstStatementFromTokenList(selectedTokenList);
                labeledStatement = new LabeledStatement(variable, statement);
            }
        } else {
            throwHandledError(selectedTokenList, ":");
        }
        return labeledStatement;
    }
}
