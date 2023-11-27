package io.dutwrapper.dutwrapper.model.utils;

public class DutSchoolYearItem {
    Integer week;
    String schoolYear;
    Integer schoolYearVal;

    public DutSchoolYearItem() {
    }

    public DutSchoolYearItem(Integer week, String schoolYear, Integer schoolYearVal) {
        this.week = week;
        this.schoolYear = schoolYear;
        this.schoolYearVal = schoolYearVal;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Integer getSchoolYearVal() {
        return schoolYearVal;
    }

    public void setSchoolYearVal(Integer schoolYearVal) {
        this.schoolYearVal = schoolYearVal;
    }

    @Override
    public String toString() {
        return String.format("School year: %s\nSchool year value: %d\nWeek %s", getSchoolYear(), getSchoolYearVal(), getWeek());
    }
}
