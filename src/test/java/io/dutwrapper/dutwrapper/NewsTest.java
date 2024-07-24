package io.dutwrapper.dutwrapper;

import java.util.List;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

@SuppressWarnings("CallToPrintStackTrace")
class NewsTest {
    @Test
    void getNewsGlobal() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            try {
                System.out.printf("[News] [News Global] Page %d%n", page);
                List<News.NewsItem> newsList = News.getNewsGlobal(page, null, null);
                System.out.printf("%d item(s))%n", newsList.size());
                System.out.println(gson.toJson(newsList));
                System.out.println("\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            page += 1;                    
        }
    }

    @Test
    void getNewsSubject() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        int page = 1;
        int pageMax = 3;

        while (page <= pageMax) {
            try {
                System.out.printf("[News] [News Subject] Page %d%n", page);
                List<News.NewsSubjectItem> newsList = News.getNewsSubject(page, null, null);
                System.out.printf("%d item(s))%n", newsList.size());
                System.out.println(gson.toJson(newsList));
                System.out.println("\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            page += 1;
        }
    }
}