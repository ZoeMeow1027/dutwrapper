package io.dutwrapper.dutwrapper.model.accounts.trainingresult;

import java.util.ArrayList;

public class AccountTrainingStatus {
    private TrainingSummary trainingSummary;
    private GraduateStatus graduateStatus;
    private ArrayList<SubjectResult> subjectResultList;

    public AccountTrainingStatus() {
    }

    public AccountTrainingStatus(TrainingSummary trainingSummary, GraduateStatus graduateStatus,
            ArrayList<SubjectResult> subjectResultList) {
        this.trainingSummary = trainingSummary;
        this.graduateStatus = graduateStatus;
        this.subjectResultList = subjectResultList;
    }

    public TrainingSummary getTrainingSummary() {
        return trainingSummary;
    }

    public void setTrainingSummary(TrainingSummary trainingSummary) {
        this.trainingSummary = trainingSummary;
    }

    public GraduateStatus getGraduateStatus() {
        return graduateStatus;
    }

    public void setGraduateStatus(GraduateStatus graduateStatus) {
        this.graduateStatus = graduateStatus;
    }

    public ArrayList<SubjectResult> getSubjectResultList() {
        return subjectResultList;
    }

    public void setSubjectResultList(ArrayList<SubjectResult> subjectResultList) {
        this.subjectResultList = subjectResultList;
    }
}
