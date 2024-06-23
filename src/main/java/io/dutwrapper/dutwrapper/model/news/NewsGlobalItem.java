package io.dutwrapper.dutwrapper.model.news;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class NewsGlobalItem implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("content_string")
    private String contentString;
    @SerializedName("date")
    private Long date;
    @SerializedName("links")
    private ArrayList<LinkItem> links;

    public NewsGlobalItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public ArrayList<LinkItem> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkItem> links) {
        this.links = links;
    }
}
