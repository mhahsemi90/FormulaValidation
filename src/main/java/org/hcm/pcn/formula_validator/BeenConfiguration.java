package org.hcm.pcn.formula_validator;

import graphql.schema.GraphQLScalarType;
import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.service.scalar.TimeStampScalar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.transaction.annotation.Transactional;

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

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(
                        GraphQLScalarType
                                .newScalar()
                                .name("TimeStamp")
                                .description("TimeStamp Extended Scalar Class")
                                .coercing(new TimeStampScalar())
                                .build()
                );
    }

    @Bean
    @Transactional
    public String test() {
        return "TRUE";
    }
}
