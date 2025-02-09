package org.hcm.pcn.formula_validator.dto;

import java.util.List;

public class ValidationResult {
    private List<String> generatedFormula;
    private String validationMessage;

    public List<String> getGeneratedFormula() {
        return generatedFormula;
    }

    public void setGeneratedFormula(List<String> generatedFormula) {
        this.generatedFormula = generatedFormula;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
