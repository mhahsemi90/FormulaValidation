package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;

import java.util.List;

public interface FormulaValidationService {
    List<LineDto> parsing(String productCode,String script,String lang);

    ValidationResult generateFormula(List<LineDto> lineDtoList,String lang);

    String formulaValidation(String productCode,String script,String lang);

    ReWritingResult formulaRewritingBaseOnBasicStructure(List<LineDto> lineDtoList,String lang);
}
