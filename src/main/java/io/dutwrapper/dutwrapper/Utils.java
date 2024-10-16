package io.dutwrapper.dutwrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Utils {
    public static class DutSchoolYearItem {
        Integer week;
        String schoolYear;
        Integer schoolYearVal;

        public DutSchoolYearItem() {
        }

        public DutSchoolYearItem(Integer week, String schoolYear, Integer schoolYearVal) {
            this.week = week;
            this.schoolYear = schoolYear;
            this.schoolYearVal = schoolYearVal;
        }

        public Integer getWeek() {
            return week;
        }

        public void setWeek(Integer week) {
            this.week = week;
        }

        public String getSchoolYear() {
            return schoolYear;
        }

        public void setSchoolYear(String schoolYear) {
            this.schoolYear = schoolYear;
        }

        public Integer getSchoolYearVal() {
            return schoolYearVal;
        }

        public void setSchoolYearVal(Integer schoolYearVal) {
            this.schoolYearVal = schoolYearVal;
        }

        @Override
        public String toString() {
            return String.format("School year: %s\nSchool year value: %d\nWeek %s", getSchoolYear(), getSchoolYearVal(), getWeek());
        }
    }

    public static String findFirstString(String test, String regex) {
        final Pattern patternDate = Pattern.compile(regex);
        final Matcher matcher = patternDate.matcher(test);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null;
        }
    }

    public static Long date2UnixTimestamp(String input) {
        // In format dd/MM/yyyy
        long date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDateTime.of(
                    LocalDate.parse(input, formatter),
                    LocalTime.of(0, 0, 0)).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        } catch (Exception ignored) {
            date = -1L;
        }
        return date;
    }

    public static DutSchoolYearItem getCurrentSchoolWeek() throws Exception {
        HttpClientWrapper.Response response = HttpClientWrapper.get(Variables.URL_MAIN_SCHOOLCURRENTWEEK, null, 60);
        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300)
            throw new Exception(
                    "Server was return with code " + response.getStatusCode() + ".");

        if (response.getContent() == null) {
            throw new Exception("WrapperResponse was returned content is null.");
        }
        Document webData = Jsoup.parse(response.getContent());
        DutSchoolYearItem result = new DutSchoolYearItem();

        // Get all and set 'selected' tag in school year list.
        Elements yearList = webData.getElementById("dnn_ctr442_View_cboNamhoc").getElementsByTag("option");
        yearList.sort((s1, s2) -> {
            int v1 = Integer.parseInt(s1.val()), v2 = Integer.parseInt(s2.val());

            return Integer.compare(v2, v1);
        });
        for (Element yearItem : yearList) {
            if (yearItem.hasAttr("selected")) {
                result.setSchoolYear(yearItem.text());
                result.setSchoolYearVal(Integer.parseInt(yearItem.val()));
            }
            break;
        }

        // Get all and set 'selected' tag in week list.
        Elements weekList = webData.getElementById("dnn_ctr442_View_cboTuan").getElementsByTag("option");
        weekList.sort((s1, s2) -> {
            int v1 = Integer.parseInt(s1.val()), v2 = Integer.parseInt(s2.val());

            return Integer.compare(v2, v1);
        });
        for (Element weekItem : weekList) {
            if (weekItem.hasAttr("selected")) {
                Pattern pattern = Pattern.compile(
                        "Tuần thứ (\\d{1,2}): (\\d{1,2}/\\d{1,2}/\\d{4})",
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(weekItem.text());
                if (matcher.find()) {
                    if (matcher.groupCount() == 2) {
                        result.setWeek(Integer.parseInt(matcher.group(1)));
                    }
                    break;
                }
            }
            break;
        }

        return result;
    }

    public interface JsoupExtraUtils {
        @Nullable
        String getValue(Document document);

        @Nullable
        String getText(Document document);

        @Nullable
        String getValue(Element element);

        @Nullable
        String getText(Element document);

        @Nullable
        Document elementToDocument(Element element);

        @Nullable
        Integer elementToIntegerOrNull(Element element);

        @Nullable
        Double elementToDoubleOrNull(Element element);

        @Nullable
        Element getSelectedValueFromComboBox(Element element);

        @Nullable
        Element getSelectedValueFromComboBox(Document document);

        String sessionIdToSubCookie(String sessionId);
    }

    public static final JsoupExtraUtils jsoupExtraUtils = new JsoupExtraUtils() {
        @Override
        @Nullable
        public String getValue(Element element) {
            try {
                if (element == null)
                    return null;
                return !element.val().isEmpty() ? element.val() : null;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        @Nullable
        public String getValue(Document document) {
            if (document == null)
                return null;
            return getValue(document.body());
        }

        @Override
        @Nullable
        public String getText(Element element) {
            try {
                if (element == null)
                    return null;
                return !element.text().isEmpty() ? element.text() : null;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        @Nullable
        public String getText(Document document) {
            if (document == null)
                return null;
            return getText(document.body());
        }

        @Override
        @Nullable
        public Document elementToDocument(Element element) {
            if (element == null)
                return null;
            return Jsoup.parse(element.html());
        }

        @Override
        @Nullable
        public Integer elementToIntegerOrNull(Element element) {
            String data = element.text();
            if (data.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(data);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        @Nullable
        public Double elementToDoubleOrNull(Element element) {
            String data = element.text();
            if (data.isEmpty()) {
                return null;
            }
            try {
                return Double.parseDouble(data);
            } catch (Exception ex) {
                return null;
            }
        }

        // https://stackoverflow.com/a/22929670
        @Override
        @Nullable
        public Element getSelectedValueFromComboBox(Element element) {
            Element result = null;
            if (element != null) {
                for (Element option : element.children()) {
                    if (option.hasAttr("selected")) {
                        result = option;
                        break;
                    }
                }
            }

            return result;
        }

        @Override
        @Nullable
        public Element getSelectedValueFromComboBox(Document document) {
            if (document == null)
                return null;
            return getSelectedValueFromComboBox(document.body());
        }

        @Override
        public String sessionIdToSubCookie(String sessionId) {
            if (sessionId == null) {
                return "";
            }
            return String.format(
                Locale.ROOT,
                "ASP.NET_SessionId=%s;",
                sessionId
            );
        }
    };
}
