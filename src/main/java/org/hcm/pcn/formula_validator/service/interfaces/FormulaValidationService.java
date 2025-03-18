package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;

import java.util.List;

public interface FormulaValidationService {
    List<LineDto> parsing(String script);

    ValidationResult generateFormula(List<LineDto> lineDtoList);

    String formulaValidation(String script);

    ReWritingResult formulaRewritingBaseOnBasicStructure(List<LineDto> lineDtoList);

    List<BlockDto> loadOperandForTest();
}
