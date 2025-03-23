package org.hcm.pcn.formula_validator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcm.pcn.formula_validator.enums.BlockType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BlockDto {
    private Long id;
    private String code;
    private String title;
    private String enTitle;
    private BlockType type;
    private BlockDto result;
    private String productCode;
    private FormulaDto formula;
    private List<BlockDto> blockList;

    public BlockDto(BlockType type, String code, String title, String enTitle) {
        this.type = type;
        this.code = code;
        this.title = title;
        this.enTitle = enTitle;
    }
}
