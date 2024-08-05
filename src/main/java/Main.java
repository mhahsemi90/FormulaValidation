import statamentgenerator.MainStatementGeneratorImpl;
import statement.Statement;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String s = "a + b";
        MainStatementGeneratorImpl generator = new MainStatementGeneratorImpl();
        List<Statement> statementList = generator.parsing(s);
        System.out.println(statementList);
    }
}
