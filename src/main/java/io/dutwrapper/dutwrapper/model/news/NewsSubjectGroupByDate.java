package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;
import javax.annotation.Nullable;

public class NewsSubjectGroupByDate {
    ArrayList<NewsGlobalItem> data;
    long date;

    public NewsSubjectGroupByDate(long date) {
        this.date = date;
        this.data = new ArrayList<NewsGlobalItem>();
    }

    public ArrayList<NewsGlobalItem> getData() {
        return data;
    }

    public long getDateInUnixTimeMilliseconds() {
        return date;
    }

    public void addData(NewsGlobalItem item, @Nullable Boolean addAtFirst) {
        if (item.getDate() != date) {
            return;
        }

        if (!(data.stream().anyMatch(p -> p.getDate() == item.getDate() &&
                p.getTitle() == item.getTitle() &&
                p.getContent() == item.getContent()))) {
            Boolean first = false;
            if (addAtFirst == null)
                first = false;
            else if (addAtFirst == false)
                first = false;

            if (first) {
                data.add(item);
            } else {
                data.add(0, item);
            }
        }
    }

    public void addData(ArrayList<NewsGlobalItem> itemList, @Nullable Boolean addAtFirst) {
        Boolean first = false;
        if (addAtFirst == null)
            first = false;
        if (addAtFirst == false)
            first = false;

        if (first) {
            for (int i = itemList.size() - 1; i >= 0; i--) {
                addData(itemList.get(i), addAtFirst);
            }
        } else {
            for (NewsGlobalItem item : itemList) {
                addData(item, addAtFirst);
            }
        }
    }
}
