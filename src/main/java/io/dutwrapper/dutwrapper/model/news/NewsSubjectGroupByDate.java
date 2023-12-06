package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;

public class NewsSubjectGroupByDate {
    ArrayList<NewsSubjectItem> data;
    long date;

    public NewsSubjectGroupByDate(long date) {
        this.date = date;
        this.data = new ArrayList<NewsSubjectItem>();
    }

    public ArrayList<NewsSubjectItem> getData() {
        return data;
    }

    public long getDateInUnixTimeMilliseconds() {
        return date;
    }

    public void addData(NewsSubjectItem item) {
        if (item.getDate() != date) {
            return;
        }

        if (!(data.stream().anyMatch(p -> p.getDate() == item.getDate() &&
                p.getTitle() == item.getTitle() &&
                p.getContent() == item.getContent()))) {
            data.add(item);
        }
    }

    public void addData(ArrayList<NewsSubjectItem> itemList) {
        for (NewsSubjectItem item : itemList) {
            addData(item);
        }
    }
}
