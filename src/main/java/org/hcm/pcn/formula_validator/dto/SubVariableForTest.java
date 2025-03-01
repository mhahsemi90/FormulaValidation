package org.hcm.pcn.formula_validator.dto;

public enum SubVariableForTest {
    S01(OperandClassForTest.O25, BlockType.LABEL, "firstValue", "مقدار اول", "First Value"),
    S02(OperandClassForTest.O25, BlockType.LABEL, "secondValue", "مقدار دوم", "Second Value"),
    S03(OperandClassForTest.O26, BlockType.LABEL, "firstValue", "مقدار اول", "First Value"),
    S04(OperandClassForTest.O26, BlockType.LABEL, "secondValue", "مقدار دوم", "Second Value"),
    S05(OperandClassForTest.O27, BlockType.LABEL, "valueList", "لیست مقادیر", "Value List"),
    S06(OperandClassForTest.O18, BlockType.LABEL, "valueList", "نام دانشگاه", "Value List"),
    S07(OperandClassForTest.O18, BlockType.LABEL, "valueList", "آدرس", "Value List"),
    S08(OperandClassForTest.O18, BlockType.LABEL, "valueList", "سطح", "Value List"),
    S09(OperandClassForTest.O19, BlockType.LABEL, "valueList", "نام مدرسه", "Value List"),
    S10(OperandClassForTest.O19, BlockType.LABEL, "valueList", "آدرس", "Value List"),
    S11(OperandClassForTest.O19, BlockType.LABEL, "valueList", "سطح", "Value List"),
    S12(OperandClassForTest.O20, BlockType.LABEL, "valueList", "معدل", "Value List"),
    S13(OperandClassForTest.O20, BlockType.LABEL, "valueList", "درجه", "Value List"),
    S14(OperandClassForTest.O21, BlockType.LABEL, "valueList", "عنوان", "Value List"),
    S15(OperandClassForTest.O21, BlockType.LABEL, "valueList", "درجه", "Value List"),
    S16(OperandClassForTest.O22, BlockType.LABEL, "valueList", "عنوان", "Value List"),
    S17(OperandClassForTest.O22, BlockType.LABEL, "valueList", "سطح", "Value List"),
    S18(OperandClassForTest.O23, BlockType.LABEL, "valueList", "عنوان", "Value List"),
    S19(OperandClassForTest.O23, BlockType.LABEL, "valueList", "کد", "Value List"),
    ;
    private final OperandClassForTest operandClassForTest;
    private final BlockType type;
    private final String code;
    private final String title;
    private final String enTitle;

    SubVariableForTest(OperandClassForTest operandClassForTest, BlockType type, String code, String title, String enTitle) {
        this.operandClassForTest = operandClassForTest;
        this.type = type;
        this.code = code;
        this.title = title;
        this.enTitle = enTitle;
    }

    public OperandClassForTest getOperandClassForTest() {
        return operandClassForTest;
    }

    public BlockType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getEnTitle() {
        return enTitle;
    }
}
