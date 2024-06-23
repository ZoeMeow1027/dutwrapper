package io.dutwrapper.dutwrapper.model.news;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import io.dutwrapper.dutwrapper.model.accounts.SubjectCodeItem;

public class NewsSubjectAffectedItem implements Serializable {
    @SerializedName("code_list")
    private ArrayList<SubjectCodeItem> codeList = new ArrayList<>();
    @SerializedName("name")
    private String subjectName = "";

    public NewsSubjectAffectedItem() { }

    public NewsSubjectAffectedItem(String subjectName) {
        this.subjectName = subjectName;
    }

    public ArrayList<SubjectCodeItem> getCodeList() {
        return codeList;
    }

    public void setCodeList(ArrayList<SubjectCodeItem> codeList) {
        this.codeList = codeList;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
