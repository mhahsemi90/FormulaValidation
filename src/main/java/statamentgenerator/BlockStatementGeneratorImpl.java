package statamentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import statement.BlockStatement;
import statement.Statement;
import token.Token;

import java.util.List;

import static statement.StatementType.HANDLED_ERROR;

public class BlockStatementGeneratorImpl implements StatementGenerator {
    @Override
    public Statement generate(List<Token> selectedTokenList, List<Token> tokenList) {
        Statement result = new Statement();
        if (selectedTokenList.size() > 1 &&
                selectedTokenList.get(0).getValue().equals("{") &&
                selectedTokenList.get(selectedTokenList.size() - 1).getValue().equals("}")) {
            StatementGenerator generator = new MainStatementGeneratorImpl();
            List<Statement> statementList = generator.getAllStatementFromTokenList(
                    selectedTokenList.subList(1, selectedTokenList.size() - 1)
            );
            if (CollectionUtils.isNotEmpty(statementList) && statementList.get(0).getType() == HANDLED_ERROR)
                result = statementList.get(1);
            else
                result = new BlockStatement(statementList);
        } else {
            this.throwHandledError(selectedTokenList, "{");
        }
        return result;
    }
}
