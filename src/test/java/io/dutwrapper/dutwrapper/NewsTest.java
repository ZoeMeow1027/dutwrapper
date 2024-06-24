package io.dutwrapper.dutwrapper;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import io.dutwrapper.dutwrapper.model.news.NewsGlobalItem;
import io.dutwrapper.dutwrapper.model.news.NewsSubjectItem;

class NewsTest {
    @Test
    void getNewsGlobal() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            try {
                System.out.println(String.format("[News] [News Global] Page %d", page));    
                List<NewsGlobalItem> newsList = News.getNewsGlobal(page, null, null);
                System.out.println(String.format("%d item(s))", newsList.size()));
                System.out.println(new Gson().toJson(newsList));
                System.out.println("\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            page += 1;                    
        }
    }

    @Test
    void getNewsSubject() throws Exception {
        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            try {
                System.out.println(String.format("[News] [News Subject] Page %d", page));    
                List<NewsSubjectItem> newsList = News.getNewsSubject(page, null, null);
                System.out.println(String.format("%d item(s))", newsList.size()));
                System.out.println(new Gson().toJson(newsList));
                System.out.println("\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            page += 1;
        }
    }
}