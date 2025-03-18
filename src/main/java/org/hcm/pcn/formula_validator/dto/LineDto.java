package org.hcm.pcn.formula_validator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hcm.pcn.formula_validator.enums.LineType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LineDto {
    private Integer id;
    private Integer parentId;
    private Integer row;
    private Integer lineLevel;
    private BlockDto assignmentOperator;
    private BlockDto resultVar;
    private List<BlockDto> blockList;
    private LineType lineType;

    public LineDto(Integer row, Integer lineLevel, List<BlockDto> blockList, LineType lineType) {
        this.row = row;
        this.lineLevel = lineLevel;
        this.blockList = blockList;
        this.lineType = lineType;
    }

    public LineDto(Integer id, Integer parentId, Integer row, Integer lineLevel, List<BlockDto> blockList, LineType lineType) {
        this.id = id;
        this.parentId = parentId;
        this.row = row;
        this.lineLevel = lineLevel;
        this.blockList = blockList;
        this.lineType = lineType;
    }
}
