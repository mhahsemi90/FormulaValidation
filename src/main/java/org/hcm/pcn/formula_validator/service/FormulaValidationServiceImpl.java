package org.hcm.pcn.formula_validator.service;

import org.hcm.pcn.formula_validator.dto.Line;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.interfaces.FormulaValidationService;
import org.hcm.pcn.formula_validator.interfaces.ParsingScriptService;
import org.hcm.pcn.formula_validator.interfaces.StatementGenerator;
import org.hcm.pcn.formula_validator.statement.Statement;
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
                statementGenerator.parsingToListOfStatement(script)
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
    public List<Line> formulaRewritingBaseOnBasicStructure(List<Line> lineList) {
        return parsingScriptService.formulaRewritingBaseOnBasicStructure(lineList);
    }
}
