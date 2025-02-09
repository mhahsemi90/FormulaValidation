package org.hcm.pcn.formula_validator.dto;

public class Block {
    private BlockType type;
    private String code;
    private String title;
    private String enTitle;

    public Block() {
    }

    public Block(BlockType type, String code, String title, String enTitle) {
        this.type = type;
        this.code = code;
        this.title = title;
        this.enTitle = enTitle;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }
}
