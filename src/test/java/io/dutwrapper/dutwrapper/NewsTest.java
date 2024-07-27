package io.dutwrapper.dutwrapper;

import java.util.List;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

@SuppressWarnings("CallToPrintStackTrace")
class NewsTest {
    @Test
    void getNewsGlobal() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();

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
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(News.LessonStatus.class, new News.LessonStatus.Serializer())
                .registerTypeAdapter(News.LecturerGender.class, new News.LecturerGender.Serializer())
                .setPrettyPrinting();
        Gson gson = gsonBuilder.create();

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