package org.hcm.pcn.formula_validator.interfaces;

import org.hcm.pcn.formula_validator.statement.Statement;

import java.util.List;

public interface ParsingScriptService {
    List<Statement> parsing(String script);
}
