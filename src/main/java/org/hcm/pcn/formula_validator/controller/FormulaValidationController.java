package org.hcm.pcn.formula_validator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.GraphQLError;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

@Controller
public class FormulaValidationController {

    private final StatementGenerator statementGenerator;

    @Autowired
    public FormulaValidationController(StatementGenerator statementGenerator) {
        this.statementGenerator = statementGenerator;
    }

    @QueryMapping
    public String formulaValidation(@Argument String formula) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(statementGenerator.parsing(formula));
    }

    @GraphQlExceptionHandler(HandledError.class)
    public GraphQLError handelParsingError(Exception e){
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }
}
