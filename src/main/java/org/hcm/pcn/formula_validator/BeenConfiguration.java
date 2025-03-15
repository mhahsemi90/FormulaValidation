package org.hcm.pcn.formula_validator;

import org.hcm.pcn.formula_validator.dto.BlockDto;
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
    public Map<String, BlockDto> getAllOperatorMap() {
        return statementGenerator.getAllOperatorMap();
    }

    @Bean(name = "Keyword")
    public Map<String, BlockDto> allKeyword() {
        return statementGenerator.getAllKeywordMap();
    }
}
