package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.service.statement.Statement;

import java.util.List;

public interface ParsingScriptService extends BaseFormulaConcept {
    List<LineDto> generateLineOfBlocksListFromStatementList(List<Statement> statementList);

    ValidationResult generateFormulaFromLineOfBlocksList(List<LineDto> lineDtoList);

    ReWritingResult formulaRewritingBaseOnBasicStructure(List<LineDto> lineDtoList);

    List<BlockDto> loadOperandForTest();
}
