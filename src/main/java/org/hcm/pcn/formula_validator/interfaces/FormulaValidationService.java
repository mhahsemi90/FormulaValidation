package org.hcm.pcn.formula_validator.interfaces;

import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ValidationResult;

import java.util.List;

public interface FormulaValidationService {
    List<Line> parsing(String script);

    ValidationResult generateFormula(List<Line> lineList);

    String formulaValidation(String script);

    List<Line> formulaRewritingBaseOnBasicStructure(List<Line> lineList);
}
