package org.hcm.pcn.formula_validator.dto;

public enum GroupClassFroTest {
    PERSONNEL_INFO("Personnel Info", "اطلاعات پرسنلی"),
    PAYROLL_INFO("Payroll Info", "اطلاعات حقوقی"),
    FINANCE_INFO("Finance Info", "اطلاعات مالی"),
    TIMESHEET_INFO("TimeSheet Info", "اطلاعات تردد"),
    EDUCATION_INFO("Education Info", "اطلاعات آموزشی"),
    FUNCTIONAL_INFO("Functional Info", "اطلاعات کارکردی"),
    FUNCTIONS("Functions", "توابع"),
    ;
    private final String enTitle;
    private final String title;

    GroupClassFroTest(String enTitle, String title) {
        this.enTitle = enTitle;
        this.title = title;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public String getTitle() {
        return title;
    }
}