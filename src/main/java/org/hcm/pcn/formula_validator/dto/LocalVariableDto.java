package org.hcm.pcn.formula_validator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcm.pcn.formula_validator.enums.BlockType;

@Getter
@Setter
@NoArgsConstructor
public class LocalVariableDto {
    private Long id;
    private String code;
    private String title;
    private String enTitle;
    private BlockType type;
    private BlockDto result;
}
