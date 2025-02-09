package org.hcm.pcn.formula_validator.interfaces;

import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.statement.Statement;

import java.util.List;

public interface ParsingScriptService extends BaseFormulaConcept {
    List<Line> generateLineOfBlocksListFromStatementList(List<Statement> statementList);
    ValidationResult generateFormulaFromLineOfBlocksList(List<Line> lineList);
    List<Line> formulaRewritingBaseOnBasicStructure(List<Line> lineList);
}
