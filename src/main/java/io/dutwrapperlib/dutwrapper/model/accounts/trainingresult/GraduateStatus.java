package io.dutwrapperlib.dutwrapper.model.accounts.trainingresult;

public class GraduateStatus {
    private Boolean hasSigGDTC;
    private Boolean hasSigGDQP;
    private Boolean hasSigEnglish;
    private Boolean hasSigIT;
    private Boolean hasQualifiedGraduate;

    // Khen thuong
    private String info1;

    // Ky luat
    private String info2;

    // DATN
    private String info3;

    // Thong tin xet tn
    private String approveGraduateProcessInfo;

    public GraduateStatus() {
    }

    public GraduateStatus(Boolean hasSigGDTC, Boolean hasSigGDQP, Boolean hasSigEnglish, Boolean hasSigIT,
            Boolean hasQualifiedGraduate, String info1, String info2, String info3, String approveGraduateProcessInfo) {
        this.hasSigGDTC = hasSigGDTC;
        this.hasSigGDQP = hasSigGDQP;
        this.hasSigEnglish = hasSigEnglish;
        this.hasSigIT = hasSigIT;
        this.hasQualifiedGraduate = hasQualifiedGraduate;
        this.info1 = info1;
        this.info2 = info2;
        this.info3 = info3;
        this.approveGraduateProcessInfo = approveGraduateProcessInfo;
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

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public String getInfo3() {
        return info3;
    }

    public void setInfo3(String info3) {
        this.info3 = info3;
    }

    public String getApproveGraduateProcessInfo() {
        return approveGraduateProcessInfo;
    }

    public void setApproveGraduateProcessInfo(String approveGraduateProcessInfo) {
        this.approveGraduateProcessInfo = approveGraduateProcessInfo;
    }

}
