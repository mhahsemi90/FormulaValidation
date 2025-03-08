package org.hcm.pcn.formula_validator.dto;

import org.hcm.pcn.formula_validator.enums.LineType;

import java.util.List;

public class ChangeValueLine extends Line {
    private Block assignmentOperator;
    private Block resultVar;

    public ChangeValueLine() {
    }

    public ChangeValueLine(Integer row, Integer lineLevel, List<Block> blockList, LineType lineType) {
        super(row, lineLevel, blockList, lineType);
    }

    public ChangeValueLine(Integer id, Integer parentId, Integer row, Integer lineLevel, List<Block> blockList, LineType lineType) {
        super(id, parentId, row, lineLevel, blockList, lineType);
    }

    public Block getAssignmentOperator() {
        return assignmentOperator;
    }

    public void setAssignmentOperator(Block assignmentOperator) {
        this.assignmentOperator = assignmentOperator;
    }

    public Block getResultVar() {
        return resultVar;
    }

    public void setResultVar(Block resultVar) {
        this.resultVar = resultVar;
    }
}
