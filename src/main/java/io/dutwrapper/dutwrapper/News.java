package io.dutwrapper.dutwrapper;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class News {
    public enum LessonStatus {
        Unknown(-1),
        Notify(0),
        Leaving(1),
        MakeUpLesson(2);

        private final int value;

        LessonStatus(int s) {
            this.value = s;
        }

        public int getValue() { return value; }

        public static class Serializer implements JsonSerializer<LessonStatus> {
            @Override
            public JsonElement serialize(LessonStatus lessonStatus, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(lessonStatus.getValue());
            }
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public enum NewsSearchType {
        ByTitle("TieuDe"),
        ByContent("NoiDung");

        private final String value;

        NewsSearchType(String s) {
            this.value = s;
        }

        public String toString() {
            return value;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public enum NewsType {
        Global("CTRTBSV"),
        Subject("CTRTBGV");

        private final String value;

        NewsType(String s) {
            this.value = s;
        }

        public String toString() {
            return value;
        }
    }

    public static class NewsResource implements Serializable {
        @SerializedName("text")
        private String text;
        @SerializedName("content")
        private String content;
        @SerializedName("type")
        // Available: Link
        private String type;
        @SerializedName("position")
        private Integer position;

        public NewsResource(String text, String type, String content, Integer position) {
            this.text = text;
            this.type = type;
            this.content = content;
            this.position = position;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }
    }

    public static class NewsItem implements Serializable {
        @SerializedName("title")
        private String title;
        @SerializedName("content_html")
        private String contentHtml;
        @SerializedName("content")
        private String content;
        @SerializedName("date")
        private Long date;
        @SerializedName("resources")
        private ArrayList<NewsResource> resources;

        public NewsItem() {
            this.resources = new ArrayList<>();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentHtml() {
            return contentHtml;
        }

        public void setContentHtml(String contentHtml) {
            this.contentHtml = contentHtml;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public ArrayList<NewsResource> getResources() {
            return resources;
        }

        public void setResources(ArrayList<NewsResource> resources) {
            this.resources = resources;
        }
    }

    public enum LecturerGender {
        Unknown(-1),
        Male(0),
        Female(1);

        private final int value;

        LecturerGender(int s) {
            this.value = s;
        }

        public int getValue() { return value; }

        public static class Serializer implements JsonSerializer<LecturerGender> {
            @Override
            public JsonElement serialize(LecturerGender lecturerGender, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(lecturerGender.getValue());
            }
        }
    }

    public static class NewsSubjectItem extends NewsItem {
        @SerializedName("affected_class")
        private ArrayList<AffectedSubject> affectedClass = new ArrayList<>();
        @SerializedName("affected_date")
        private Long affectedDate = 0L;
        @SerializedName("status")
        private LessonStatus lessonStatus = LessonStatus.Unknown;
        @SerializedName("affected_lessons")
        private AccountInformation.LessonAffected affectedLesson;
        @SerializedName("makeup_room")
        private String affectedRoom;
        @SerializedName("lecturer_name")
        private String lecturerName = "";
        @SerializedName("lecturer_gender")
        private LecturerGender lecturerGender = LecturerGender.Unknown;

        public Long getAffectedDate() {
            return affectedDate;
        }

        public void setAffectedDate(Long affectedDate) {
            this.affectedDate = affectedDate;
        }

        public LessonStatus getLessonStatus() {
            return lessonStatus;
        }

        public void setLessonStatus(LessonStatus lessonStatus) {
            this.lessonStatus = lessonStatus;
        }

        public AccountInformation.LessonAffected getAffectedLesson() {
            return affectedLesson;
        }

        public void setAffectedLesson(AccountInformation.LessonAffected affectedLesson) {
            this.affectedLesson = affectedLesson;
        }

        public String getAffectedRoom() {
            return affectedRoom;
        }

        public void setAffectedRoom(String affectedRoom) {
            this.affectedRoom = affectedRoom;
        }

        public ArrayList<AffectedSubject> getAffectedClass() {
            return affectedClass;
        }

        public void setAffectedClass(ArrayList<AffectedSubject> affectedClass) {
            this.affectedClass = affectedClass;
        }

        public String getLecturerName() {
            return lecturerName;
        }

        public void setLecturerName(String lecturerName) {
            this.lecturerName = lecturerName;
        }

        public LecturerGender getLecturerGender() {
            return lecturerGender;
        }

        public void setLecturerGender(LecturerGender lecturerGender) {
            this.lecturerGender = lecturerGender;
        }

        public NewsSubjectItem(ArrayList<AffectedSubject> affectedClass, Long affectedDate, LessonStatus lessonStatus, AccountInformation.LessonAffected affectedLesson, String affectedRoom) {
            this.affectedClass = affectedClass;
            this.affectedDate = affectedDate;
            this.lessonStatus = lessonStatus;
            this.affectedLesson = affectedLesson;
            this.affectedRoom = affectedRoom;
        }

        public NewsSubjectItem(NewsItem newsGlobal) {
            this.setTitle(newsGlobal.getTitle());
            this.setContent(newsGlobal.getContent());
            this.setContentHtml(newsGlobal.getContentHtml());
            this.setDate(newsGlobal.getDate());
            this.getResources().clear();
            this.getResources().addAll(newsGlobal.getResources());
        }
    }

    public static class AffectedSubject implements Serializable {
        @SerializedName("code_list")
        private ArrayList<AccountInformation.SubjectCode> codeList = new ArrayList<>();
        @SerializedName("name")
        private String subjectName;

        public AffectedSubject(String subjectName) {
            this.subjectName = subjectName;
        }

        public ArrayList<AccountInformation.SubjectCode> getCodeList() {
            return codeList;
        }

        public void setCodeList(ArrayList<AccountInformation.SubjectCode> codeList) {
            this.codeList = codeList;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
    }

    private static ArrayList<NewsItem> getNews(
            @Nullable NewsType newsType,
            @Nullable Integer page,
            @Nullable NewsSearchType searchType,
            @Nullable String searchQuery) throws Exception {
        String url = String.format(
                Variables.URL_SV_NEWS,
                newsType == null ? NewsType.Global.toString() : newsType.toString(),
                page == null ? 1 : page,
                searchType == null ? NewsSearchType.ByTitle.toString() : searchType.toString(),
                URLEncoder.encode(searchQuery == null ? "" : searchQuery, "UTF-8"));

        HttpClientWrapper.Response response = HttpClientWrapper.get(url, null, 60);
        response.throwExceptionIfExist();

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new Exception("Server was returned with code " + response.getStatusCode() + ".");
        }

        // https://www.baeldung.com/java-with-jsoup
        if (response.getContent() == null) {
            return new ArrayList<>();
        }

        Document webData = Jsoup.parse(response.getContent());
        webData.outputSettings().charset("UTF-8");
        for (Element el : webData.getElementsByTag("br")) {
            el.remove();
        }

        // Get available news in that page (max in server is 30 per page).
        Elements tbBox = webData.getElementsByClass("tbBox");

        // This will be returned if done here.
        ArrayList<NewsItem> newsList = new ArrayList<>();
        // Processing with every single item.
        for (Element tbBoxItem : tbBox) {
            NewsItem newsItem = new NewsItem();

            // Get news title.
            Element title = tbBoxItem.getElementsByClass("tbBoxCaption").get(0);
            // Split title to 2 strings (if possible, to get date and title).
            String[] titleTemp = title.text().split(":", 2);
            // Get news content.
            Element content = tbBoxItem.getElementsByClass("tbBoxContent").get(0);

            // If separated to 2 string, parse date and title here.
            if (titleTemp.length == 2) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(titleTemp[0], formatter);
                LocalTime time = LocalTime.parse("00:00:00");
                LocalDateTime dateTime = date.atTime(time);
                newsItem.setDate(dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
                newsItem.setTitle(titleTemp[1].trim());
            }
            // Otherwise, simply return title without date.
            else {
                newsItem.setTitle(title.text().trim());
            }

            // Set content and contentHtml here.
            // contentHtml for full HTML code news while content is text-only news.
            newsItem.setContentHtml(content.html());
            newsItem.setContent(content.wholeText());

            // Task: Find links and set to item
            ArrayList<NewsResource> links = new ArrayList<>();
            // Set begin position.
            int position = 0;
            // Get text-only news.
            String tmpString = content.wholeText();
            // Get all elements in this news with tag <a>.
            Elements tmpLink = content.getElementsByTag("a");
            // Processing with every single tag <a>.
            for (Element item : tmpLink) {
                // Check if tmpString contains link string.
                if (tmpString.contains(item.wholeText())) {
                    NewsResource item1 = new NewsResource(
                            item.wholeText(),
                            "link",
                            item.attr("abs:href"),
                            // Position here from root position and indexOf() link string.
                            position + tmpString.indexOf(item.wholeText())
                    );
                    // Add this resource to NewsResource.
                    links.add(item1);
                    // Position here will plus (indexOf() link string) and (length() link string).
                    position += tmpString.indexOf(item.wholeText()) + item.wholeText().length();

                    // https://stackoverflow.com/questions/24220509/exception-when-replacing-brackets
                    String[] temp3 = tmpString.split(Pattern.quote(item.wholeText()), 2);
                    if (temp3.length > 1) {
                        tmpString = temp3[1];
                    }
                }
            }
            // Add resource list to news item.
            newsItem.setResources(links);

            // Add to news list.
            newsList.add(newsItem);
        }

        // Return news list.
        return newsList;
    }

    public static ArrayList<NewsItem> getNewsGlobal(
            @Nullable Integer page,
            @Nullable NewsSearchType searchType,
            @Nullable String searchQuery) throws Exception {
        return getNews(
                NewsType.Global,
                page == null ? 1 : page,
                searchType,
                searchQuery);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static ArrayList<NewsSubjectItem> getNewsSubject(
            @Nullable Integer page,
            @Nullable NewsSearchType searchType,
            @Nullable String searchQuery) throws Exception {
        ArrayList<NewsSubjectItem> result = new ArrayList<>();
        ArrayList<NewsItem> listTemp = getNews(
                NewsType.Subject,
                page == null ? 1 : page,
                searchType,
                searchQuery);

        for (NewsItem item : listTemp) {
            // Initialize news subject item from news global
            NewsSubjectItem subjectItem = new NewsSubjectItem(item);

            // For title
            try {
                String lecturerProcessing = item.getTitle().split(" thông báo đến lớp:")[0].trim();
                String[] splitText = lecturerProcessing.split(" ", 2);
                String lecturerGender = splitText[0].toLowerCase(Locale.ROOT);
                if (lecturerGender.equals("thầy")) {
                    subjectItem.setLecturerGender(LecturerGender.Male);
                } else if (lecturerGender.equals("cô")) {
                    subjectItem.setLecturerGender(LecturerGender.Female);
                } else {
                    subjectItem.setLecturerGender(LecturerGender.Unknown);
                }
                subjectItem.setLecturerName(splitText[1]);

                subjectItem.getAffectedClass().addAll(getAffectedClass(item.getTitle()));
            } catch (Exception ex) {
                if (Variables.getShowDebugLogStatus()) {
                    ex.printStackTrace();
                }
            }

            // For content. If found something, do work. If not, just ignore.
            if (subjectItem.getContentHtml().contains("HỌC BÙ")) {
                subjectItem.setLessonStatus(LessonStatus.MakeUpLesson);
                subjectItem.setAffectedDate(Utils.date2UnixTimestamp(
                        Utils.findFirstString(subjectItem.getContent(), "\\d{2}[-|/]\\d{2}[-|/]\\d{4}")));
                try {
                    subjectItem.setAffectedLesson(getLessonAffected(
                            Objects.requireNonNull(Utils.findFirstString(subjectItem.getContent().toLowerCase(),
                                    "tiết: .*[0-9],")).replace("tiết:", "").replace(",", "").trim()));
                } catch (Exception ignored) {
                }
                try {
                    subjectItem.setAffectedRoom(Objects
                            .requireNonNull(
                                    Utils.findFirstString(subjectItem.getContent().toLowerCase(), "phòng:.*"))
                            .replace("phòng:", "").replace(",", "").trim().toUpperCase());
                } catch (Exception ignored) {
                }
            } else if (subjectItem.getContentHtml().contains("NGHỈ HỌC")) {
                subjectItem.setLessonStatus(LessonStatus.Leaving);
                subjectItem.setAffectedDate(Utils.date2UnixTimestamp(
                        Utils.findFirstString(subjectItem.getContent(), "\\d{2}[-|/]\\d{2}[-|/]\\d{4}")));
                try {
                    subjectItem.setAffectedLesson(getLessonAffected(
                            Objects.requireNonNull(Utils.findFirstString(subjectItem.getContent().toLowerCase(),
                                    "\\(tiết:.*[0-9]\\)")).replace("(tiết:", "").replace(")", "").trim()));
                } catch (Exception ignored) {
                }
            } else {
                subjectItem.setLessonStatus(LessonStatus.Notify);
            }

            // Add to item.
            result.add(subjectItem);
        }

        return result;
    }

    private static AccountInformation.LessonAffected getLessonAffected(String input) {
        if (input.contains("-")) {
            return new AccountInformation.LessonAffected(
                    Integer.parseInt(input.split("-")[0]),
                    Integer.parseInt(input.split("-")[1])
            );
        } else return null;
    }

    private static ArrayList<AffectedSubject> getAffectedClass(String input) {
        String subjectProcessing = input.split(" thông báo đến lớp:")[1].trim();
        ArrayList<String> data1 = Arrays.stream(subjectProcessing.split(" , "))
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<AffectedSubject> data2 = new ArrayList<>();

        for (String item : data1) {
            String itemSubjectName = item.substring(0, item.indexOf("[")).trim();
            String itemClass = item.substring(item.indexOf("[") + 1, item.indexOf("]")).toLowerCase();
            AccountInformation.SubjectCode codeItem;
            if (itemClass.contains(".nh")) {
                String[] data = itemClass.split(".nh");
                codeItem = new AccountInformation.SubjectCode(
                        data[0],
                        data[1]);
            } else {
                String[] data = itemClass.split("nh");
                codeItem = new AccountInformation.SubjectCode(
                        data[0],
                        data[1]);
            }

            if (data2.stream().noneMatch(p -> Objects.equals(p.getSubjectName(), itemSubjectName))) {
                AffectedSubject item2 = new AffectedSubject(itemSubjectName);
                item2.getCodeList().add(codeItem);
                data2.add(item2);
            } else {
                Optional<AffectedSubject> tempData = data2.stream()
                        .filter(p -> Objects.equals(p.getSubjectName(), itemSubjectName)).findFirst();
                if (tempData.isPresent()) {
                    AffectedSubject temp = tempData.get();
                    temp.getCodeList().add(codeItem);
                }
            }
        }

        return data2;
    }
}