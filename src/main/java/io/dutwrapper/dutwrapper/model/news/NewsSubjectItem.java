package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import io.dutwrapper.dutwrapper.model.accounts.LessonItem;
import io.dutwrapper.dutwrapper.model.enums.LessonStatus;

public class NewsSubjectItem extends NewsGlobalItem {
    @SerializedName("affected_class")
    private ArrayList<NewsSubjectAffectedItem> affectedClass = new ArrayList<>();
    @SerializedName("affected_date")
    private Long affectedDate = 0L;
    @SerializedName("status")
    private LessonStatus lessonStatus = LessonStatus.Unknown;
    @SerializedName("affected_lessons")
    private LessonItem affectedLesson;
    @SerializedName("makeup_room")
    private String affectedRoom;
    @SerializedName("lecturer_name")
    private String lecturerName = "";
    @SerializedName("lecturer_gender")
    private Boolean lecturerGender = false;

    public Long getAffectedDate() {
        return affectedDate;
    }

    public void setAffectedDate(Long affectedDate) {
        this.affectedDate = affectedDate;
    }

    public LessonStatus getLessonStatus() {
        return lessonStatus;
    }

    public void setLessonStatus(LessonStatus lessonStatus) {
        this.lessonStatus = lessonStatus;
    }

    public LessonItem getAffectedLesson() {
        return affectedLesson;
    }

    public void setAffectedLesson(LessonItem affectedLesson) {
        this.affectedLesson = affectedLesson;
    }

    public String getAffectedRoom() {
        return affectedRoom;
    }

    public void setAffectedRoom(String affectedRoom) {
        this.affectedRoom = affectedRoom;
    }

    public ArrayList<NewsSubjectAffectedItem> getAffectedClass() {
        return affectedClass;
    }

    public void setAffectedClass(ArrayList<NewsSubjectAffectedItem> affectedClass) {
        this.affectedClass = affectedClass;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public Boolean getLecturerGender() {
        return lecturerGender;
    }

    public void setLecturerGender(Boolean lecturerGender) {
        this.lecturerGender = lecturerGender;
    }

    public NewsSubjectItem(ArrayList<NewsSubjectAffectedItem> affectedClass, Long affectedDate, LessonStatus lessonStatus, LessonItem affectedLesson, String affectedRoom) {
        this.affectedClass = affectedClass;
        this.affectedDate = affectedDate;
        this.lessonStatus = lessonStatus;
        this.affectedLesson = affectedLesson;
        this.affectedRoom = affectedRoom;
    }

    public NewsSubjectItem() { }
}
