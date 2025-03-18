package org.hcm.pcn.formula_validator.dto;

import java.util.List;

public class ReWritingResult {
    private List<LineDto> reWritingLineListDto;
    private String validationMessage;

    public List<LineDto> getReWritingLineList() {
        return reWritingLineListDto;
    }

    public void setReWritingLineList(List<LineDto> reWritingLineListDto) {
        this.reWritingLineListDto = reWritingLineListDto;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
