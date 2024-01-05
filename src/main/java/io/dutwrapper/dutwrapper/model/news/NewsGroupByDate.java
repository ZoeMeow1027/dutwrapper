package io.dutwrapper.dutwrapper.model.news;

import java.util.ArrayList;

public interface NewsGroupByDate<T> {
    public ArrayList<T> getData();

    public long getDateInUnixMilliseconds();

    public void addData(T item);

    public void addData(T item, Boolean addToTop);

    public void addData(ArrayList<T> list);

    public void addData(ArrayList<T> list, Boolean addToTop);
}
