package io.dutwrapper.dutwrapper.model.accounts.trainingresult;

public class TrainingSummary {
    private String schoolYearStart;
    private String schoolYearCurrent;
    private Double creditCollected;
    private Double avgTrainingScore4;
    private Double avgSocial;

    public TrainingSummary() {
    }

    public TrainingSummary(String schoolYearStart, String schoolYearCurrent, Double creditCollected,
            Double avgTrainingScore4, Double avgSocial) {
        this.schoolYearStart = schoolYearStart;
        this.schoolYearCurrent = schoolYearCurrent;
        this.creditCollected = creditCollected;
        this.avgTrainingScore4 = avgTrainingScore4;
        this.avgSocial = avgSocial;
    }

    public String getSchoolYearStart() {
        return schoolYearStart;
    }

    public void setSchoolYearStart(String schoolYearStart) {
        this.schoolYearStart = schoolYearStart;
    }

    public String getSchoolYearCurrent() {
        return schoolYearCurrent;
    }

    public void setSchoolYearCurrent(String schoolYearCurrent) {
        this.schoolYearCurrent = schoolYearCurrent;
    }

    public Double getCreditCollected() {
        return creditCollected;
    }

    public void setCreditCollected(Double creditCollected) {
        this.creditCollected = creditCollected;
    }

    public Double getAvgTrainingScore4() {
        return avgTrainingScore4;
    }

    public void setAvgTrainingScore4(Double avgTrainingScore4) {
        this.avgTrainingScore4 = avgTrainingScore4;
    }

    public Double getAvgSocial() {
        return avgSocial;
    }

    public void setAvgSocial(Double avgSocial) {
        this.avgSocial = avgSocial;
    }
}
