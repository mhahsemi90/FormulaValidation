package org.hcm.pcn.formula_validator.service.implmentation;

import org.hcm.pcn.formula_validator.dto.BlockDto;
import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.interfaces.FormulaValidationService;
import org.hcm.pcn.formula_validator.service.interfaces.ParsingScriptService;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormulaValidationServiceImpl implements FormulaValidationService {
    private final StatementGenerator statementGenerator;
    private final ParsingScriptService parsingScriptService;

    public FormulaValidationServiceImpl(
            StatementGenerator statementGenerator,
            ParsingScriptService parsingScriptService
    ) {
        this.statementGenerator = statementGenerator;
        this.parsingScriptService = parsingScriptService;
    }

    @Override
    public List<Line> parsing(String script) {
        return parsingScriptService.generateLineOfBlocksListFromStatementList(
                statementGenerator.parsingToListOfStatement(script, false)
        );
    }

    @Override
    public ValidationResult generateFormula(List<Line> lineList) {
        return parsingScriptService.generateFormulaFromLineOfBlocksList(lineList);
    }

    @Override
    public String formulaValidation(String script) {
        String result = "OK";
        try {
            parsing(script);
        } catch (HandledError handledError) {
            result = handledError.getMessage();
        }
        return result;
    }

    @Override
    public ReWritingResult formulaRewritingBaseOnBasicStructure(List<Line> lineList) {
        return parsingScriptService.formulaRewritingBaseOnBasicStructure(lineList);
    }

    @Override
    public List<BlockDto> loadOperandForTest() {
        return parsingScriptService.loadOperandForTest();
    }
}
