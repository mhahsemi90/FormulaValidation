package org.hcm.pcn.formula_validator.dto;

public enum OperandClassForTest {
    O0(GroupClassFroTest.PERSONNEL_INFO, BlockType.VARIABLE, "childCount", "تعداد فرزند", "Children Count"),
    O1(GroupClassFroTest.PERSONNEL_INFO, BlockType.VARIABLE, "educationCount", "تعداد دوره آموزشی", "Education Count"),
    O2(GroupClassFroTest.PERSONNEL_INFO, BlockType.VARIABLE, "educationTimeTotal", "ساعات دوره آموزشی", "Education Time Total"),
    O4(GroupClassFroTest.PAYROLL_INFO, BlockType.VARIABLE, "childAmount", "حق اولاد", "Children Amount"),
    O5(GroupClassFroTest.PAYROLL_INFO, BlockType.VARIABLE, "housingAmount", "حق مسکن", "Housing Amount"),
    O6(GroupClassFroTest.PAYROLL_INFO, BlockType.VARIABLE, "baseAmount", "حقوق پایه", "Base Amount"),
    O7(GroupClassFroTest.PAYROLL_INFO, BlockType.VARIABLE, "yearsAmount", "سنوات", "Years Amount"),
    O8(GroupClassFroTest.FINANCE_INFO, BlockType.VARIABLE, "bankAccount", "شماره حساب", "Bank Account"),
    O9(GroupClassFroTest.FINANCE_INFO, BlockType.VARIABLE, "insuranceNumber", "شماره بیمه", "Insurance Number"),
    O10(GroupClassFroTest.FINANCE_INFO, BlockType.VARIABLE, "salaryDegree", "درجه مالیاتی", "Salary Degree"),
    O11(GroupClassFroTest.TIMESHEET_INFO, BlockType.VARIABLE, "workedTimeTotal", "کل ساعات کاری", "WorkedTime Total"),
    O12(GroupClassFroTest.TIMESHEET_INFO, BlockType.VARIABLE, "addedWorkedTimeTotal", "کل اضافه کاری", "Added WorkedTime Total"),
    O13(GroupClassFroTest.TIMESHEET_INFO, BlockType.VARIABLE, "extraTime", "اضافه کاری", "Extra Time"),
    O14(GroupClassFroTest.TIMESHEET_INFO, BlockType.VARIABLE, "baseDegree", "درجه پایه", "Base Degree"),
    O15(GroupClassFroTest.TIMESHEET_INFO, BlockType.VARIABLE, "weekendWorkTime", "ساعات اضافه کاری آخر هفته", "Weekend Work Time"),
    O16(GroupClassFroTest.TIMESHEET_INFO, BlockType.VARIABLE, "weekendWorkTime2", "ساعات اضافه کاری آخر هفته2", "Weekend Work Time2"),
    O17(GroupClassFroTest.EDUCATION_INFO, BlockType.VARIABLE, "degreeLevel", "سطح آموزشی", "Degree Level"),
    O18(GroupClassFroTest.EDUCATION_INFO, BlockType.OBJECT, "university", "دانشگاه", "University"),
    O19(GroupClassFroTest.EDUCATION_INFO, BlockType.OBJECT, "school", "مدرسه", "School"),
    O20(GroupClassFroTest.EDUCATION_INFO, BlockType.OBJECT, "lastEducationDegree", "آخرین مدرک تحصیلی", "Last Education Degree"),
    O21(GroupClassFroTest.FUNCTIONAL_INFO, BlockType.OBJECT, "job", "عنوان شغل", "Job"),
    O22(GroupClassFroTest.FUNCTIONAL_INFO, BlockType.OBJECT, "post", "عنوان پست", "Post"),
    O23(GroupClassFroTest.FUNCTIONAL_INFO, BlockType.OBJECT, "organization", "واحد سازمانی", "Organization"),
    O24(GroupClassFroTest.FUNCTIONS, BlockType.FUNCTION, "round", "رند", "Round"),
    O25(GroupClassFroTest.FUNCTIONS, BlockType.FUNCTION, "maxValue", "مقدار حداکثر", "Max Value"),
    O26(GroupClassFroTest.FUNCTIONS, BlockType.FUNCTION, "minValue", "مقدار حداقل", "Min Value"),
    O27(GroupClassFroTest.FUNCTIONS, BlockType.FUNCTION, "averageValue", "مقدار متوسط", "Average Value"),
    ;
    private final GroupClassFroTest groupClassFroTest;
    private final BlockType type;
    private final String code;
    private final String title;
    private final String enTitle;

    OperandClassForTest(GroupClassFroTest groupClassFroTest, BlockType type, String code, String title, String enTitle) {
        this.groupClassFroTest = groupClassFroTest;
        this.type = type;
        this.code = code;
        this.title = title;
        this.enTitle = enTitle;
    }

    public GroupClassFroTest getGroupClassFroTest() {
        return groupClassFroTest;
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
