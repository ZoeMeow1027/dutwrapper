package io.dutwrapper.dutwrapper.model.accounts.trainingresult;

public class GraduateStatus {
    private Boolean hasSigGDTC;
    private Boolean hasSigGDQP;
    private Boolean hasSigEnglish;
    private Boolean hasSigIT;
    private Boolean hasQualifiedGraduate;

    // Khen thuong
    private String rewardsInfo;

    // Ky luat
    private String discipline;

    // DATN
    private String eligibleGraduationThesisStatus;

    // Thong tin xet tn
    private String eligibleGraduationStatus;

    public GraduateStatus() {
    }

    public GraduateStatus(Boolean hasSigGDTC, Boolean hasSigGDQP, Boolean hasSigEnglish, Boolean hasSigIT,
            Boolean hasQualifiedGraduate, String rewardsInfo, String discipline, String eligibleGraduationThesisStatus, String eligibleGraduationStatus) {
        this.hasSigGDTC = hasSigGDTC;
        this.hasSigGDQP = hasSigGDQP;
        this.hasSigEnglish = hasSigEnglish;
        this.hasSigIT = hasSigIT;
        this.hasQualifiedGraduate = hasQualifiedGraduate;
        this.rewardsInfo = rewardsInfo;
        this.discipline = discipline;
        this.eligibleGraduationThesisStatus = eligibleGraduationThesisStatus;
        this.eligibleGraduationStatus = eligibleGraduationStatus;
    }

    public Boolean getHasSigGDTC() {
        return hasSigGDTC;
    }

    public void setHasSigGDTC(Boolean hasSigGDTC) {
        this.hasSigGDTC = hasSigGDTC;
    }

    public Boolean getHasSigGDQP() {
        return hasSigGDQP;
    }

    public void setHasSigGDQP(Boolean hasSigGDQP) {
        this.hasSigGDQP = hasSigGDQP;
    }

    public Boolean getHasSigEnglish() {
        return hasSigEnglish;
    }

    public void setHasSigEnglish(Boolean hasSigEnglish) {
        this.hasSigEnglish = hasSigEnglish;
    }

    public Boolean getHasSigIT() {
        return hasSigIT;
    }

    public void setHasSigIT(Boolean hasSigIT) {
        this.hasSigIT = hasSigIT;
    }

    public Boolean getHasQualifiedGraduate() {
        return hasQualifiedGraduate;
    }

    public void setHasQualifiedGraduate(Boolean hasQualifiedGraduate) {
        this.hasQualifiedGraduate = hasQualifiedGraduate;
    }

    public String getRewardsInfo() {
        return rewardsInfo;
    }

    public void setRewardsInfo(String info1) {
        this.rewardsInfo = info1;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String info2) {
        this.discipline = info2;
    }

    public String getEligibleGraduationThesisStatus() {
        return eligibleGraduationThesisStatus;
    }

    public void setEligibleGraduationThesisStatus(String info3) {
        this.eligibleGraduationThesisStatus = info3;
    }

    public String getEligibleGraduationStatus() {
        return eligibleGraduationStatus;
    }

    public void setEligibleGraduationStatus(String approveGraduateProcessInfo) {
        this.eligibleGraduationStatus = approveGraduateProcessInfo;
    }

}
