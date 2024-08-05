import interfaces.ParsingScriptService;
import statamentgenerator.ParsingScriptServiceImpl;
import statement.Statement;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String s = "a + b";
        ParsingScriptService parsingScriptService = new ParsingScriptServiceImpl();
        List<Statement> statementList = parsingScriptService.parsing(s);
        System.out.println(statementList);
    }
}
