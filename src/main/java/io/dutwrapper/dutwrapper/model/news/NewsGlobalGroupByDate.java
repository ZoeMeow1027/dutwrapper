package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;

public class NewsGlobalGroupByDate implements NewsGroupByDate<NewsGlobalItem> {
    ArrayList<NewsGlobalItem> data;
    long date;

    public NewsGlobalGroupByDate(long date) {
        this.date = date;
        this.data = new ArrayList<NewsGlobalItem>();
    }

    @Override
    public ArrayList<NewsGlobalItem> getData() {
        return data;
    }

    @Override
    public long getDateInUnixMilliseconds() {
        return date;
    }

    @Override
    public void addData(NewsGlobalItem item, Boolean addToTop) {
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
    public void addData(NewsGlobalItem item) {
        addData(item, false);
    }

    @Override
    public void addData(ArrayList<NewsGlobalItem> list, Boolean addToTop) {
        if (addToTop) {
            for (int i = list.size() - 1; i >= 0; i--) {
                addData(list.get(i), true);
            }
        } else {
            for (NewsGlobalItem item : list) {
                addData(item);
            }
        }
    }

    @Override
    public void addData(ArrayList<NewsGlobalItem> list) {
        addData(list, false);
    }

}
