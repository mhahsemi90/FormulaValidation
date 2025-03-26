package org.hcm.pcn.formula_validator.controller;

import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.service.interfaces.FormulaValidationService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FormulaValidationController {

    private final FormulaValidationService formulaValidationService;

    public FormulaValidationController(FormulaValidationService formulaValidationService) {
        this.formulaValidationService = formulaValidationService;
    }

    @QueryMapping
    public List<LineDto> formulaParsing(@Argument String productCode, @Argument String formula, @Argument String lang) {
        return formulaValidationService.parsing(productCode, formula, lang);
    }

    @QueryMapping
    public ValidationResult generateFormula(@Argument List<LineDto> lineList, @Argument String lang) {
        return formulaValidationService.generateFormula(lineList, lang);
    }

    @QueryMapping
    public String formulaValidation(@Argument String productCode, @Argument String formula, @Argument String lang) {
        return formulaValidationService.formulaValidation(productCode, formula, lang);
    }

    @QueryMapping
    public ReWritingResult formulaRewritingBaseOnBasicStructure(@Argument List<LineDto> lineList, @Argument String lang) {
        return formulaValidationService.formulaRewritingBaseOnBasicStructure(lineList, lang);
    }
}
