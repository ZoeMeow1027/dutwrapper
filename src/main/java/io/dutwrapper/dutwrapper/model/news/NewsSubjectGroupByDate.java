package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;

public class NewsSubjectGroupByDate implements NewsGroupByDate<NewsSubjectItem> {
    ArrayList<NewsSubjectItem> data;
    long date;

    public NewsSubjectGroupByDate(long date) {
        this.date = date;
        this.data = new ArrayList<NewsSubjectItem>();
    }

    @Override
    public void addData(NewsSubjectItem item) {
        addData(item, false);
    }

    @Override
    public void addData(ArrayList<NewsSubjectItem> list) {
        addData(list, false);
    }

    @Override
    public long getDateInUnixMilliseconds() {
        return date;
    }

    @Override
    public void addData(NewsSubjectItem item, Boolean addToTop) {
        if (item.getDate() != date) {
            return;
        }

        if ((data.stream().anyMatch(p -> p.getDate() == item.getDate() &&
                p.getTitle() == item.getTitle() &&
                p.getContent() == item.getContent()))) {
            return;
        }

        if (addToTop) {
            data.add(0, item);
        } else {
            data.add(item);
        }
    }

    @Override
    public void addData(ArrayList<NewsSubjectItem> list, Boolean addToTop) {
        if (addToTop) {
            for (int i = list.size() - 1; i >= 0; i--) {
                addData(list.get(i), true);
            }
        } else {
            for (NewsSubjectItem item : list) {
                addData(item);
            }
        }
    }

    @Override
    public ArrayList<NewsSubjectItem> getData() {
        return data;
    }
}
