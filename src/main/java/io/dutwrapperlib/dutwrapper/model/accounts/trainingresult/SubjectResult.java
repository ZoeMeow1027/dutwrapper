package io.dutwrapperlib.dutwrapper.model.accounts.trainingresult;

import javax.annotation.Nullable;

public class SubjectResult {
    Integer index;
    String schoolYear;
    Boolean isExtendedSemester;
    String id;
    String name;
    Double credit;
    @Nullable
    String pointFormula;
    @Nullable
    Double pointBT, pointBV, pointCC, pointCK, pointGK, pointQT, pointTH;
    @Nullable
    Double resultT4, resultT10;
    @Nullable
    String resultByCharacter;
    Boolean isReStudy;

    public SubjectResult() {
    }

    public SubjectResult(Integer index, String schoolYear, Boolean isExtendedSemester, String id, String name,
            Double credit, String pointFormula, Double pointBT, Double pointBV, Double pointCC, Double pointCK,
            Double pointGK, Double pointQT, Double pointTH, Double resultT4, Double resultT10, String resultByCharacter,
            Boolean isReStudy) {
        this.index = index;
        this.schoolYear = schoolYear;
        this.isExtendedSemester = isExtendedSemester;
        this.id = id;
        this.name = name;
        this.credit = credit;
        this.pointFormula = pointFormula;
        this.pointBT = pointBT;
        this.pointBV = pointBV;
        this.pointCC = pointCC;
        this.pointCK = pointCK;
        this.pointGK = pointGK;
        this.pointQT = pointQT;
        this.pointTH = pointTH;
        this.resultT4 = resultT4;
        this.resultT10 = resultT10;
        this.resultByCharacter = resultByCharacter;
        this.isReStudy = isReStudy != null ? isReStudy : false;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Boolean getIsExtendedSemester() {
        return isExtendedSemester;
    }

    public void setIsExtendedSemester(Boolean isExtendedSemester) {
        this.isExtendedSemester = isExtendedSemester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public @Nullable String getPointFormula() {
        return pointFormula;
    }

    public void setPointFormula(String pointFormula) {
        this.pointFormula = pointFormula;
    }

    public @Nullable Double getPointBT() {
        return pointBT;
    }

    public void setPointBT(Double pointBT) {
        this.pointBT = pointBT;
    }

    public @Nullable Double getPointBV() {
        return pointBV;
    }

    public void setPointBV(Double pointBV) {
        this.pointBV = pointBV;
    }

    public @Nullable Double getPointCC() {
        return pointCC;
    }

    public void setPointCC(Double pointCC) {
        this.pointCC = pointCC;
    }

    public @Nullable Double getPointCK() {
        return pointCK;
    }

    public void setPointCK(Double pointCK) {
        this.pointCK = pointCK;
    }

    public @Nullable Double getPointGK() {
        return pointGK;
    }

    public void setPointGK(Double pointGK) {
        this.pointGK = pointGK;
    }

    public @Nullable Double getPointQT() {
        return pointQT;
    }

    public void setPointQT(Double pointQT) {
        this.pointQT = pointQT;
    }

    public @Nullable Double getPointTH() {
        return pointTH;
    }

    public void setPointTH(Double pointTH) {
        this.pointTH = pointTH;
    }

    public @Nullable Double getResultT4() {
        return resultT4;
    }

    public void setResultT4(Double resultT4) {
        this.resultT4 = resultT4;
    }

    public @Nullable Double getResultT10() {
        return resultT10;
    }

    public void setResultT10(Double resultT10) {
        this.resultT10 = resultT10;
    }

    public @Nullable String getResultByCharacter() {
        return resultByCharacter;
    }

    public void setResultByCharacter(String resultByCharacter) {
        this.resultByCharacter = resultByCharacter;
    }

    public Boolean getIsReStudy() {
        return isReStudy;
    }

    public void setIsReStudy(Boolean isReStudy) {
        this.isReStudy = isReStudy;
    }

}
