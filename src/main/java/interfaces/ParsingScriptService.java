package interfaces;

import statement.Statement;

import java.util.List;

public interface ParsingScriptService {
    List<Statement> parsing(String script);
}
