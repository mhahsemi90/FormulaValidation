package org.hcm.pcn.formula_validator.dto;

import org.hcm.pcn.formula_validator.enums.LineType;

import java.util.List;

public class Line {
    private Integer id;
    private Integer parentId;
    private Integer row;
    private Integer lineLevel;
    private List<Block> blockList;
    private LineType lineType;

    public Line() {
    }

    public Line(Integer row, Integer lineLevel, List<Block> blockList, LineType lineType) {
        this.row = row;
        this.lineLevel = lineLevel;
        this.blockList = blockList;
        this.lineType = lineType;
    }

    public Line(Integer id, Integer parentId, Integer row, Integer lineLevel, List<Block> blockList, LineType lineType) {
        this.id = id;
        this.parentId = parentId;
        this.row = row;
        this.lineLevel = lineLevel;
        this.blockList = blockList;
        this.lineType = lineType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getLineLevel() {
        return lineLevel;
    }

    public void setLineLevel(Integer lineLevel) {
        this.lineLevel = lineLevel;
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }
}
