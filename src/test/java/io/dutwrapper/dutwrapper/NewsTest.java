package io.dutwrapper.dutwrapper;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.dutwrapper.dutwrapper.model.enums.LessonStatus;
import io.dutwrapper.dutwrapper.model.news.LinkItem;
import io.dutwrapper.dutwrapper.model.news.NewsGlobalItem;
import io.dutwrapper.dutwrapper.model.news.NewsSubjectItem;

class NewsTest {
    @Test
    void getNewsGlobal() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            try {
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
            } catch (Exception ex) {
                // ex.printStackTrace();
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

            List<NewsSubjectItem> newsList = News.getNewsSubject(page, null, null);
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
}