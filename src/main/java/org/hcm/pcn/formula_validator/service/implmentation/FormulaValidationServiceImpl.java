package org.hcm.pcn.formula_validator.service.implmentation;

import org.hcm.pcn.formula_validator.dto.LineDto;
import org.hcm.pcn.formula_validator.dto.ReWritingResult;
import org.hcm.pcn.formula_validator.dto.ValidationResult;
import org.hcm.pcn.formula_validator.exception.HandledError;
import org.hcm.pcn.formula_validator.service.interfaces.BlockManagementService;
import org.hcm.pcn.formula_validator.service.interfaces.FormulaValidationService;
import org.hcm.pcn.formula_validator.service.interfaces.ParsingScriptService;
import org.hcm.pcn.formula_validator.service.interfaces.StatementGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@Service
public class FormulaValidationServiceImpl implements FormulaValidationService {
    private final StatementGenerator statementGenerator;
    private final ParsingScriptService parsingScriptService;
    private final BlockManagementService blockManagementService;
    private final MessageSource messageSource;

    public FormulaValidationServiceImpl(
            StatementGenerator statementGenerator,
            ParsingScriptService parsingScriptService,
            BlockManagementService blockManagementService,
            @Qualifier("MessageBundleSource") MessageSource messageSource) {
        this.statementGenerator = statementGenerator;
        this.parsingScriptService = parsingScriptService;
        this.blockManagementService = blockManagementService;
        this.messageSource = messageSource;
    }

    @Override
    public List<LineDto> parsing(String productCode, String script, String lang) {
        return parsingScriptService.generateLineOfBlocksListFromStatementList(
                statementGenerator.parsingToListOfStatement(script, false, lang),
                blockManagementService.getAllOperandMapByProductCode(productCode)
        );
    }

    @Override
    public ValidationResult generateFormula(List<LineDto> lineDtoList, String lang) {
        return parsingScriptService.generateFormulaFromLineOfBlocksList(lineDtoList, lang);
    }

    @Override
    public String formulaValidation(String productCode, String script, String lang) {
        String result = "OK";
        try {
            parsing(productCode, script, lang);
        } catch (HandledError e) {
            result = messageSource.getMessage(
                    e.getMessage(),
                    e.getArgs(),
                    e.getLang() != null ? new Locale(e.getLang()) : LocaleContextHolder.getLocale()
            );
        }
        return result;
    }

    @Override
    public ReWritingResult formulaRewritingBaseOnBasicStructure(List<LineDto> lineDtoList, String lang) {
        return parsingScriptService.formulaRewritingBaseOnBasicStructure(lineDtoList, new LinkedHashMap<>(), lang);
    }
}
