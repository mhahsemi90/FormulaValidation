package org.hcm.pcn.formula_validator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormulaDto {
    private Long id;
    private String formula;
    private Long version;
    private Timestamp createdAt;
    private List<LocalVariableDto> localVariableList;
}
