package org.hcm.pcn.formula_validator.controller;

import graphql.GraphQLError;
import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.interfaces.FormulaValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FormulaValidationController {

    private final FormulaValidationService formulaValidationService;

    @Autowired
    public FormulaValidationController(FormulaValidationService formulaValidationService) {
        this.formulaValidationService = formulaValidationService;
    }

    @QueryMapping
    public List<Line> formulaParsing(@Argument String formula) {
        return formulaValidationService.parsing(formula);
    }

    @QueryMapping
    public ValidationResult generateFormula(@Argument List<Line> lineList) {
        return formulaValidationService.generateFormula(lineList);
    }

    @QueryMapping
    public String formulaValidation(@Argument String formula) {
        return formulaValidationService.formulaValidation(formula);
    }

    @QueryMapping
    public List<Line> formulaRewritingBaseOnBasicStructure(@Argument List<Line> lineList) {
        return formulaValidationService.formulaRewritingBaseOnBasicStructure(lineList);
    }

    @GraphQlExceptionHandler(HandledError.class)
    public GraphQLError handelParsingError(Exception e) {
        return GraphQLError.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }
}
