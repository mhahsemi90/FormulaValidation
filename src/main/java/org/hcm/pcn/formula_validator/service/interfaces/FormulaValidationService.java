package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;

import java.util.List;

public interface FormulaValidationService {
    List<Line> parsing(String script);

    ValidationResult generateFormula(List<Line> lineList);

    String formulaValidation(String script);

    ReWritingResult formulaRewritingBaseOnBasicStructure(List<Line> lineList);

    List<BlockDto> loadOperandForTest();
}
