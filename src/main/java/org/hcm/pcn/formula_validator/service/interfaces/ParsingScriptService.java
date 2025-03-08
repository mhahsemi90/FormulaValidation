package org.hcm.pcn.formula_validator.service.interfaces;

import org.hcm.pcn.formula_validator.dto.Block;
import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.service.statement.Statement;

import java.util.List;

public interface ParsingScriptService extends BaseFormulaConcept {
    List<Line> generateLineOfBlocksListFromStatementList(List<Statement> statementList);

    ValidationResult generateFormulaFromLineOfBlocksList(List<Line> lineList);

    ReWritingResult formulaRewritingBaseOnBasicStructure(List<Line> lineList);

    List<Block> loadOperandForTest();
}
