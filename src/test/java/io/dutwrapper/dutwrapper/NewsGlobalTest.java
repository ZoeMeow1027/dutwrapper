package io.dutwrapper.dutwrapper;

import org.junit.jupiter.api.Test;

import io.dutwrapper.dutwrapper.model.enums.NewsSearchType;
import io.dutwrapper.dutwrapper.model.news.LinkItem;
import io.dutwrapper.dutwrapper.model.news.NewsGlobalItem;

import java.util.List;

class NewsGlobalTest {
    @Test
    void getNews() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            System.out.println("===========================================");
            List<NewsGlobalItem> newsList = News.getNewsGlobal(page, NewsSearchType.ByContent, "Thứ");
            System.out.println(String.format("Page %d (%d item(s))", page, newsList.size()));

            for (NewsGlobalItem newsItem : newsList) {
                System.out.println(
                        String.format("Index %d (date: %d)", newsList.indexOf(newsItem) + 1, newsItem.getDate()));
                System.out.println(newsItem.getTitle());
                System.out.println(newsItem.getContentString());
                System.out.println(String.format("%d link(s)", newsItem.getLinks().size()));
                if (newsItem.getLinks().size() > 0) {
                    System.out.println("Position | Text | URL");
                    for (LinkItem linkItem : newsItem.getLinks()) {
                        System.out.println(String.format("%d | %s | %s", linkItem.getPosition(), linkItem.getText(),
                                linkItem.getUrl()));
                    }
                }
            }
            page += 1;
        }
    }
}