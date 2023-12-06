package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;

public class NewsGlobalGroupByDate {
    ArrayList<NewsGlobalItem> data;
    long date;

    public NewsGlobalGroupByDate(long date) {
        this.date = date;
        this.data = new ArrayList<NewsGlobalItem>();
    }

    public ArrayList<NewsGlobalItem> getData() {
        return data;
    }

    public long getDateInUnixTimeMilliseconds() {
        return date;
    }

    public void addData(NewsGlobalItem item) {
        if (item.getDate() != date) {
            return;
        }

        if (!(data.stream().anyMatch(p -> p.getDate() == item.getDate() &&
                p.getTitle() == item.getTitle() &&
                p.getContent() == item.getContent()))) {
            data.add(item);
        }
    }

    public void addData(ArrayList<NewsGlobalItem> itemList) {
        for (NewsGlobalItem item : itemList) {
            addData(item);
        }
    }
}
