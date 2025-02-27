package org.hcm.pcn.formula_validator.dto;

import java.util.List;

public class ReWritingResult {
    private List<Line> reWritingLineList;
    private String validationMessage;

    public List<Line> getReWritingLineList() {
        return reWritingLineList;
    }

    public void setReWritingLineList(List<Line> reWritingLineList) {
        this.reWritingLineList = reWritingLineList;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
