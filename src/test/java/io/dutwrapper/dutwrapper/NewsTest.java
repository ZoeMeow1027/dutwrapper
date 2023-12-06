package io.dutwrapper.dutwrapper;

import org.junit.jupiter.api.Test;

import io.dutwrapper.dutwrapper.model.enums.LessonStatus;
import io.dutwrapper.dutwrapper.model.enums.NewsSearchType;
import io.dutwrapper.dutwrapper.model.news.LinkItem;
import io.dutwrapper.dutwrapper.model.news.NewsGlobalGroupByDate;
import io.dutwrapper.dutwrapper.model.news.NewsGlobalItem;
import io.dutwrapper.dutwrapper.model.news.NewsSubjectGroupByDate;
import io.dutwrapper.dutwrapper.model.news.NewsSubjectItem;

import java.util.ArrayList;
import java.util.List;

class NewsTest {
    @Test
    void getNewsGlobal() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            System.out.println("===========================================");
            List<NewsGlobalItem> newsList = News.getNewsGlobal(page, null, null);
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

    @Test
    void getNewsGlobalGroupByDate() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            System.out.println("===========================================");
            ArrayList<NewsGlobalGroupByDate> newsList = News.getNewsGlobalGroupByDate(page, null, null);
            System.out.println(String.format("Page %d (%d item(s))", page, newsList.size()));

            for (NewsGlobalGroupByDate newsGroup : newsList) {
                System.out.println(String.format(
                    "Index %d - date: %d - count: %d",
                    newsList.indexOf(newsGroup) + 1,
                    newsGroup.getDateInUnixTimeMilliseconds(),
                    newsGroup.getData().size()
                    ));
                for (NewsGlobalItem newsItem: newsGroup.getData()) {
                    System.out.println(String.format(
                        "- Index %d - date: %d - title: %s",
                        newsGroup.getData().indexOf(newsItem),
                        newsItem.getDate(),
                        newsItem.getTitle()
                    ));
                }
            }
            page += 1;
        }
    }

    @Test
    void getNewsSubject() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            System.out.println("==================================");
            System.out.println("Page " + page);

            List<NewsSubjectItem> newsList = News.getNewsSubject(page, NewsSearchType.ByContent, "Thứ");
            System.out.println("Item count: " + newsList.size());

            for (NewsSubjectItem newsItem : newsList) {
                System.out.println("============================");
                System.out.println(newsItem.getTitle());
                System.out.println(newsItem.getDate());
                System.out.printf("%s|%s%n", newsItem.getLecturerName(),
                        newsItem.getLecturerGender() ? "true" : "false");
                if (newsItem.getLessonStatus() == LessonStatus.Leaving) {
                    System.out.println("Leaving");
                    System.out.println("Date: " + newsItem.getAffectedDate());
                    System.out.println("Lesson: " + newsItem.getAffectedLesson().toString());
                } else if (newsItem.getLessonStatus() == LessonStatus.MakeUp) {
                    System.out.println("MakeUp");
                    System.out.println("Date: " + newsItem.getAffectedDate());
                    System.out.println("Lesson: " + newsItem.getAffectedLesson().toString());
                    System.out.println("Room: " + newsItem.getAffectedRoom());
                } else {
                    System.out.println(newsItem.getContentString());
                }
            }
            page += 1;
        }
    }

    @Test
    void getNewsSubjectGroupByDate() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            System.out.println("===========================================");
            ArrayList<NewsSubjectGroupByDate> newsList = News.getNewsSubjectGroupByDate(page, null, null);
            System.out.println(String.format("Page %d (%d item(s))", page, newsList.size()));

            for (NewsSubjectGroupByDate newsGroup : newsList) {
                System.out.println(String.format(
                    "Index %d - date: %d - count: %d",
                    newsList.indexOf(newsGroup) + 1,
                    newsGroup.getDateInUnixTimeMilliseconds(),
                    newsGroup.getData().size()
                    ));
                for (NewsSubjectItem newsItem: newsGroup.getData()) {
                    System.out.println(String.format(
                        "- Index %d - date: %d - title: %s",
                        newsGroup.getData().indexOf(newsItem),
                        newsItem.getDate(),
                        newsItem.getTitle()
                    ));
                }
            }
            page += 1;
        }
    }
}