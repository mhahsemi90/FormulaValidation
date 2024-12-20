package org.hcm.pcn.formula_validator.token;

public class Token implements Cloneable {
    private TokenType tokenType;
    private String value;
    private Integer level;

    public Token() {
    }

    public Token(TokenType tokenType, String value, Integer level) {
        this.tokenType = tokenType;
        this.value = value;
        this.level = level;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public Token clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Token) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
