package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.service.statement.Statement;

import java.util.List;
import java.util.Map;

public interface ParsingScriptService extends BaseFormulaConcept {
    List<LineDto> generateLineOfBlocksListFromStatementList(List<Statement> statementList,Map<String, BlockDto> allOperandMap);

    ValidationResult generateFormulaFromLineOfBlocksList(List<LineDto> lineDtoList, String lang);

    ReWritingResult formulaRewritingBaseOnBasicStructure(List<LineDto> lineDtoList,Map<String, BlockDto> allOperandMap,String lang);
}
