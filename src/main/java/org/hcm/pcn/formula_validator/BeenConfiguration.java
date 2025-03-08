package org.hcm.pcn.formula_validator;

import org.hcm.pcn.formula_validator.dto.Block;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class BeenConfiguration {
    private final StatementGenerator statementGenerator;

    public BeenConfiguration(StatementGenerator statementGenerator) {
        this.statementGenerator = statementGenerator;
    }

    @Bean(name = "Operator")
    public Map<String, Block> getAllOperatorMap() {
        return statementGenerator.getAllOperatorMap();
    }

    @Bean(name = "Keyword")
    public Map<String, Block> allKeyword() {
        return statementGenerator.getAllKeywordMap();
    }
}
