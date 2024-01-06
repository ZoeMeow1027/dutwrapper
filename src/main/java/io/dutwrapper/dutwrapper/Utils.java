package io.dutwrapper.dutwrapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.dutwrapper.dutwrapper.customrequest2.WrapperRequest;
import io.dutwrapper.dutwrapper.customrequest2.WrapperResponse;
import io.dutwrapper.dutwrapper.model.utils.DutSchoolYearItem;

public class Utils {
    public static Long getCurrentTimeInUnix() {
        return System.currentTimeMillis();
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

    public static byte[] toUrlEncode(Map<String, String> map, String charsetName) throws UnsupportedEncodingException {
        String request = "";

        Boolean first = true;

        for (String key : map.keySet()) {
            request += String.format(
                    "%s%s=%s",
                    first ? "&" : "",
                    URLEncoder.encode(key, "UTF-8"),
                    URLEncoder.encode(map.get(key), "UTF-8"));
        }

        return request.getBytes(charsetName);
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
        WrapperResponse response = WrapperRequest.get(Variables.URL_SCHOOLCURRENTWEEK, null, 60);
        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300)
            throw new Exception(
                    "Server was return with code " + response.getStatusCode() + ". May be you haven't logged in?");

        if (response.getContent() == null) {
            throw new Exception("WrapperResponse was returned content is null.");
        }
        Document webData = Jsoup.parse(response.getContent());
        DutSchoolYearItem result = new DutSchoolYearItem();

        // Get all and set 'selected' tag in school year list.
        Elements yearList = webData.getElementById("dnn_ctr442_View_cboNamhoc").getElementsByTag("option");
        yearList.sort((s1, s2) -> {
            Integer v1 = Integer.parseInt(s1.val()), v2 = Integer.parseInt(s2.val());

            return (v1 < v2) ? 1 : (v1 > v2) ? -1 : 0;
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
            Integer v1 = Integer.parseInt(s1.val()), v2 = Integer.parseInt(s2.val());

            return (v1 < v2) ? 1 : (v1 > v2) ? -1 : 0;
        });
        for (Element weekItem : weekList) {
            if (weekItem.hasAttr("selected")) {
                Pattern pattern = Pattern.compile(
                        "Tuần thứ (\\d{1,2}): (\\d{1,2}\\/\\d{1,2}\\/\\d{4})",
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
}
