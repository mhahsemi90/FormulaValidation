package org.hcm.pcn.formula_validator;

import org.hcm.pcn.formula_validator.dto.Keyword;
import org.hcm.pcn.formula_validator.dto.Operator;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class BeenConfiguration {
    private final StatementGenerator statementGenerator;

    public BeenConfiguration(StatementGenerator statementGenerator) {
        this.statementGenerator = statementGenerator;
    }

    @Bean
    public Map<String, Operator> getAllOperatorMap() {
        return statementGenerator.getAllOperatorMap();
    }

    @Bean
    public Map<String, Keyword> allKeyword() {
        return statementGenerator.getAllKeywordMap();
    }
}
