package io.dutwrapper.dutwrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.dutwrapper.dutwrapper.customrequest.CustomRequest;
import io.dutwrapper.dutwrapper.customrequest.CustomRequestItem;
import io.dutwrapper.dutwrapper.customrequest.CustomRequestList;
import io.dutwrapper.dutwrapper.customrequest.CustomResponse;
import io.dutwrapper.dutwrapper.model.accounts.*;
import io.dutwrapper.dutwrapper.model.accounts.trainingresult.AccountTrainingStatus;
import io.dutwrapper.dutwrapper.model.accounts.trainingresult.GraduateStatus;
import io.dutwrapper.dutwrapper.model.accounts.trainingresult.SubjectResult;
import io.dutwrapper.dutwrapper.model.accounts.trainingresult.TrainingSummary;

import javax.annotation.Nullable;

import static io.dutwrapper.dutwrapper.Variables.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@SuppressWarnings({ "SpellCheckingInspection", "UnusedReturnValue" })
public class Account {
    private static final WebDocumentUtils extFunc = new WebDocumentUtils() {
        public @Nullable String getValueByID(Document webData, String elementId) {
            Element element = webData.getElementById(elementId);
            if (element != null) {
                return element.val().length() > 0 ? element.val() : null;
            } else
                return null;
        }

        // https://stackoverflow.com/a/22929670
        public @Nullable String getValueFromComboBoxByID(Document webData, String elementId) {
            String result = null;
            Element options = webData.getElementById(elementId);
            if (options != null) {
                for (Element option : options.children()) {
                    if (option.hasAttr("selected")) {
                        result = option.text();
                        break;
                    }
                }
            }

            return result;
        }
    };

    public static CustomResponse getSessionId() throws IOException {
        return CustomRequest.get(null, URL_MAINPAGE, 60);
    }

    private static String sessionIdToCookie(String sessionId) {
        StringBuilder sb = new StringBuilder();
        if (sessionId != null) {
            sb.append("ASP.NET_SessionId").append("=").append(sessionId).append(";");
        }
        return sb.toString();
    }

    public static CustomResponse login(String sessionId, String user, String pass) throws IOException {
        CustomRequestList requestList = new CustomRequestList();
        requestList.addRequest(new CustomRequestItem("__VIEWSTATE", Variables.__VIEWSTATE));
        requestList.addRequest(new CustomRequestItem("__VIEWSTATEGENERATOR", Variables.__VIEWSTATEGENERATOR));
        requestList.addRequest(new CustomRequestItem("_ctl0:MainContent:DN_txtAcc", user));
        requestList.addRequest(new CustomRequestItem("_ctl0:MainContent:DN_txtPass", pass));
        requestList.addRequest(new CustomRequestItem("_ctl0:MainContent:QLTH_btnLogin", Variables.QLTH_btnLogin));

        return CustomRequest.post(
                sessionIdToCookie(sessionId),
                Variables.URL_LOGIN,
                requestList.toURLEncodeByteArray("UTF-8"),
                60);
    }

    public static Boolean isLoggedIn(String sessionId) throws IOException {
        CustomResponse response = CustomRequest.get(
                sessionIdToCookie(sessionId),
                Variables.URL_CHECKLOGGEDIN,
                60);

        return response.getReturnCode() >= 200 && response.getReturnCode() < 300;
    }

    public static CustomResponse logout(String sessionId) throws IOException {
        return CustomRequest.get(
                sessionIdToCookie(sessionId),
                Variables.URL_LOGOUT,
                60);
    }

    public static ArrayList<SubjectScheduleItem> getSubjectSchedule(String sessionId, Integer year, Integer semester)
            throws Exception {
        if (!isLoggedIn(sessionId)) {
            throw new Exception("No account logged in this session. May be you haven't logged in?");
        }

        String url;
        switch (semester) {
            case 1:
            case 2:
                url = String.format(Variables.URL_SUBJECTSCHEDULE, year, semester, 0);
                break;
            case 3:
                url = String.format(Variables.URL_SUBJECTSCHEDULE, year, semester - 1, 1);
                break;
            default:
                throw new Exception("Invalid semester!");
        }

        CustomResponse response = CustomRequest.get(
                sessionIdToCookie(sessionId),
                url,
                60);

        if (response.getReturnCode() < 200 || response.getReturnCode() >= 300)
            throw new Exception(
                    "Server was return with code " + response.getReturnCode() + ". May be you haven't logged in?");

        Document webData = Jsoup.parse(response.getContentHtmlString());

        ArrayList<SubjectScheduleItem> result = new ArrayList<>();

        // Schedule study
        Element schStudy = webData.getElementById("TTKB_GridInfo");
        if (schStudy != null) {
            Elements schStudyList = schStudy.getElementsByClass("GridRow");
            if (schStudyList.size() > 0) {
                for (Element row : schStudyList) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    if (cellList.size() < 10)
                        continue;

                    SubjectScheduleItem si = new SubjectScheduleItem();
                    si.setId(new SubjectCodeItem(cellList.get(1).text()));
                    si.setName(cellList.get(2).text());
                    try {
                        si.setCredit(Integer.parseInt(cellList.get(3).text()));
                    } catch (Exception ex) {
                        si.setCredit(0);
                    }
                    si.setIsHighQuality(cellList.get(5).attr("class").contains("GridCheck"));
                    si.setLecturer(cellList.get(6).text());

                    // Set schedule study here!
                    ScheduleStudy scheduleStudy = new ScheduleStudy();

                    if (!cellList.get(7).text().isEmpty()) {
                        String[] cellSplit = cellList.get(7).text().split("; ");
                        for (String cellSplitItem : cellSplit) {
                            ScheduleItem scheduleItem = new ScheduleItem();
                            // Set day of week
                            if (cellSplitItem.toUpperCase().contains("CN")) {
                                scheduleItem.setDayOfWeek(0);
                            } else {
                                scheduleItem
                                        .setDayOfWeek(Integer.parseInt(cellSplitItem.split(",")[0].split(" ")[1]) - 1);
                            }
                            // Set lesson
                            LessonItem lessonItem = new LessonItem();
                            lessonItem.setStart(Integer.parseInt(cellSplitItem.split(",")[1].split("-")[0]));
                            lessonItem.setEnd(Integer.parseInt(cellSplitItem.split(",")[1].split("-")[1]));
                            scheduleItem.setLesson(lessonItem);
                            // Set room
                            scheduleItem.setRoom(cellSplitItem.split(",")[2]);
                            // Add to schedule list.
                            scheduleStudy.getScheduleList().add(scheduleItem);
                        }
                    }

                    // Set schedule study week list.
                    if (!cellList.get(8).text().isEmpty()) {
                        String[] cellSplit = cellList.get(8).text().split(";");
                        for (String cellSplitItem : cellSplit) {
                            WeekItem weekItem = new WeekItem();
                            weekItem.setStart(Integer.parseInt(cellSplitItem.split("-")[0]));
                            weekItem.setEnd(Integer.parseInt(cellSplitItem.split("-")[1]));
                            scheduleStudy.getWeekList().add(weekItem);
                        }
                    }

                    // Add to subject schedule item.
                    si.setSubjectStudy(scheduleStudy);

                    // Set subject point formula.
                    si.setPointFormula(cellList.get(10).text());

                    // Add to result
                    result.add(si);
                }
            }
        }

        // Schedule examination
        Element schExam = webData.getElementById("TTKB_GridLT");
        if (schExam != null) {
            Elements schExamList = schExam.getElementsByClass("GridRow");
            if (schExamList.size() > 0) {
                for (Element row : schExamList) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    if (cellList.size() < 5)
                        continue;

                    SubjectScheduleItem si = result.stream()
                            .filter(s -> s.getId().toString(false).equals(cellList.get(1).text()))
                            .findFirst()
                            .orElse(null);

                    // Set subject examination here!
                    if (si != null) {
                        ScheduleExam scheduleExam = new ScheduleExam();
                        // Set group
                        scheduleExam.setGroup(cellList.get(3).text());
                        // Set is global
                        scheduleExam.setIsGlobal(cellList.get(4).attr("class").contains("GridCheck"));
                        // Date + Room
                        String temp = cellList.get(5).text();
                        String[] tempSplitted = temp.split(", ");
                        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
                        for (String tempSplittedItem : tempSplitted) {
                            String[] itemSplitted = tempSplittedItem.split(": ");
                            if (itemSplitted.length < 2)
                                continue;

                            // Area for day
                            if (tempSplittedItem.contains("Ngày")) {
                                try {
                                    String[] dateSplitted = itemSplitted[1].split("/");
                                    localDateTime = LocalDateTime.of(
                                            Integer.parseInt(dateSplitted[2]),
                                            Integer.parseInt(dateSplitted[1]),
                                            Integer.parseInt(dateSplitted[0]),
                                            0, 0, 0);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            // Area for room
                            else if (tempSplittedItem.contains("Phòng")) {
                                scheduleExam.setRoom(itemSplitted[1]);
                            }
                            // Area for hours
                            else if (tempSplittedItem.contains("Giờ")) {
                                String[] timeSplitted = itemSplitted[1].split("h");
                                if (timeSplitted.length > 0)
                                    localDateTime = localDateTime.plusHours(Integer.parseInt(timeSplitted[0]) - 7);
                                if (timeSplitted.length > 1)
                                    localDateTime = localDateTime.plusMinutes(Integer.parseInt(timeSplitted[1]));
                            }
                        }
                        // Set date
                        scheduleExam.setDate(localDateTime.toEpochSecond(ZoneOffset.UTC) * 1000);
                        si.setSubjectExam(scheduleExam);
                    }
                }
            }
        }

        return result;
    }

    public static ArrayList<SubjectFeeItem> getSubjectFee(String sessionId, Integer year, Integer semester)
            throws Exception {
        if (!isLoggedIn(sessionId)) {
            throw new Exception("No account logged in this session. May be you haven't logged in?");
        }

        String url;
        switch (semester) {
            case 1:
            case 2:
                url = String.format(Variables.URL_SUBJECTFEE, year, semester, 0);
                break;
            case 3:
                url = String.format(Variables.URL_SUBJECTFEE, year, semester - 1, 1);
                break;
            default:
                throw new Exception("Invalid semester!");
        }

        CustomResponse response = CustomRequest.get(
                sessionIdToCookie(sessionId),
                url,
                60);

        if (response.getReturnCode() < 200 || response.getReturnCode() >= 300)
            throw new Exception(
                    "Server was return with code " + response.getReturnCode() + ". May be you haven't logged in?");

        Document webData = Jsoup.parse(response.getContentHtmlString());

        ArrayList<SubjectFeeItem> result = new ArrayList<>();

        Element schFee = webData.getElementById("THocPhi_GridInfo");
        if (schFee != null) {
            Elements schFeeList = schFee.getElementsByClass("GridRow");
            if (schFeeList.size() > 0) {
                for (Element row : schFeeList) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    if (cellList.size() < 10)
                        continue;

                    SubjectFeeItem sf = new SubjectFeeItem();
                    sf.setId(new SubjectCodeItem(cellList.get(1).text()));
                    sf.setName(cellList.get(2).text());
                    try {
                        sf.setCredit(Integer.parseInt(cellList.get(3).text()));
                    } catch (Exception ex) {
                        sf.setCredit(0);
                    }
                    sf.setIsHighQuality(cellList.get(4).attr("class").contains("GridCheck"));
                    try {
                        sf.setPrice(Double.parseDouble(cellList.get(5).text().replace(",", "")));
                    } catch (Exception ex) {
                        sf.setPrice(0.0);
                    }
                    sf.setDebt(cellList.get(6).attr("class").contains("GridCheck"));
                    sf.setIsRestudy(cellList.get(7).attr("class").contains("GridCheck"));
                    sf.setVerifiedPaymentAt(cellList.get(8).text());
                    result.add(sf);
                }
            }
        }

        return result;
    }

    public static AccountInformation getAccountInformation(String sessionId) throws Exception {
        if (!isLoggedIn(sessionId)) {
            throw new Exception("No account logged in this session. May be you haven't logged in?");
        }

        CustomResponse response = CustomRequest.get(
                sessionIdToCookie(sessionId),
                Variables.URL_ACCOUNTINFORMATION,
                60);

        if (response.getReturnCode() < 200 || response.getReturnCode() >= 300)
            throw new Exception(
                    "Server was return with code " + response.getReturnCode() + ". May be you haven't logged in?");

        Document webData = Jsoup.parse(response.getContentHtmlString());
        AccountInformation result = new AccountInformation(
                extFunc.getValueByID(webData, "CN_txtHoTen"),
                extFunc.getValueByID(webData, "CN_txtNgaySinh"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboNoiSinh"),
                extFunc.getValueByID(webData, "CN_txtGioiTinh"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboDanToc"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboQuocTich"),
                extFunc.getValueByID(webData, "CN_txtSoCMND"),
                extFunc.getValueByID(webData, "CN_txtNgayCap"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboNoiCap"),
                extFunc.getValueByID(webData, "CN_txtSoCCCD"),
                extFunc.getValueByID(webData, "CN_txtNcCCCD"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboTonGiao"),
                extFunc.getValueByID(webData, "CN_txtTKNHang"),
                extFunc.getValueByID(webData, "CN_txtNgHang"),
                extFunc.getValueByID(webData, "CN_txtSoBHYT"),
                extFunc.getValueByID(webData, "CN_txtHanBHYT"),
                extFunc.getValueByID(webData, "MainContent_CN_txtNganh"),
                extFunc.getValueByID(webData, "CN_txtLop"),
                extFunc.getValueByID(webData, "MainContent_CN_txtCTDT"),
                extFunc.getValueByID(webData, "MainContent_CN_txtCT2"),
                extFunc.getValueByID(webData, "CN_txtMail1"),
                extFunc.getValueByID(webData, "CN_txtMail2"),
                extFunc.getValueByID(webData, "CN_txtMK365"),
                extFunc.getValueByID(webData, "CN_txtFace"),
                extFunc.getValueByID(webData, "CN_txtPhone"),
                extFunc.getValueByID(webData, "CN_txtCuTru"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboDCCua"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboTinhCTru"),
                extFunc.getValueFromComboBoxByID(webData, "CN_cboQuanCTru"),
                extFunc.getValueFromComboBoxByID(webData, "CN_divPhuongCTru"),
                "");

        Element element = webData.getElementById("Main_lblHoTen");
        if (element != null) {
            String temp = element.text();
            result.setStudentId(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")));
        } else
            result.setStudentId("");

        return result;
    }

    public static AccountTrainingStatus getAccountTrainingStatus(String sessionId) throws Exception {
        if (!isLoggedIn(sessionId)) {
            throw new Exception("No account logged in this session. May be you haven't logged in?");
        }

        CustomResponse response = CustomRequest.get(
                sessionIdToCookie(sessionId),
                Variables.URL_ACCOUNTTRAININGSTATUS,
                60);
        if (response.getReturnCode() < 200 || response.getReturnCode() >= 300)
            throw new Exception(
                    "Server was return with code " + response.getReturnCode() + ". May be you haven't logged in?");

        Document webData = Jsoup.parse(response.getContentHtmlString());
        AccountTrainingStatus result = new AccountTrainingStatus();

        // Training status
        TrainingSummary trainSum = new TrainingSummary();
        Elements accTrainingStatus = webData.getElementById("KQRLGridTH").getElementsByClass("GridRow");
        for (Element data : accTrainingStatus) {
            Elements cellData = data.getElementsByClass("GridCell");
            if (cellData.get(0).text().isEmpty() || cellData.get(cellData.size() - 1).text().isEmpty()
                    || cellData.get(cellData.size() - 2).text().isEmpty()
                    || cellData.get(cellData.size() - 3).text().isEmpty())
                continue;

            Boolean first = false;
            if (trainSum.getSchoolYearStart() == null) {
                first = true;
            } else if (trainSum.getSchoolYearStart().isEmpty()) {
                first = true;
            }

            if (first) {
                trainSum.setSchoolYearStart(cellData.get(0).text());
                ;
            }
            trainSum.setSchoolYearCurrent(cellData.get(0).text());
            trainSum.setCreditCollected(Double.parseDouble(cellData.get(cellData.size() - 3).text()));
            trainSum.setAvgTrainingScore4(Double.parseDouble(cellData.get(cellData.size() - 2).text()));
            trainSum.setAvgSocial(Double.parseDouble(cellData.get(cellData.size() - 1).text()));
        }
        result.setTrainingSummary(trainSum);

        // Graduate status
        Element accGraduateStatus = webData.getElementById("KQRLdvCc");
        GraduateStatus graduateStatus = new GraduateStatus(
                accGraduateStatus.getElementById("KQRL_chkGDTC").hasAttr("checked"),
                accGraduateStatus.getElementById("KQRL_chkQP").hasAttr("checked"),
                accGraduateStatus.getElementById("KQRL_chkCCNN").hasAttr("checked"),
                accGraduateStatus.getElementById("KQRL_chkCCTH").hasAttr("checked"),
                accGraduateStatus.getElementById("KQRL_chkCNTN").hasAttr("checked"),
                accGraduateStatus.getElementById("KQRL_txtKT").text(),
                accGraduateStatus.getElementById("KQRL_txtKL").text(),
                accGraduateStatus.getElementById("KQRL_txtInfo").text(),
                accGraduateStatus.getElementById("KQRL_txtCNTN").text());
        result.setGraduateStatus(graduateStatus);

        // Subject Result list
        ArrayList<SubjectResult> accSubjectResult = new ArrayList<>();
        Elements accSubjectResultList = webData.getElementById("KQRL_divContent").getElementById("KQRLGridKQHT")
                .getElementsByClass("GridRow");
        for (int i = accSubjectResultList.size() - 1; i >= 0; i--) {
            Elements cellList = accSubjectResultList.get(i).getElementsByClass("GridCell");
            SubjectResult item = new SubjectResult(
                    Integer.parseInt(cellList.get(0).text()),
                    cellList.get(1).text(),
                    cellList.get(2).hasClass("GridCheck"),
                    cellList.get(3).text(),
                    cellList.get(4).text(),
                    Double.parseDouble(cellList.get(5).text()),
                    cellList.get(6).text().isEmpty() ? null : cellList.get(6).text(),
                    cellList.get(7).text().isEmpty() ? null : Double.parseDouble(cellList.get(7).text()),
                    cellList.get(8).text().isEmpty() ? null : Double.parseDouble(cellList.get(8).text()),
                    cellList.get(9).text().isEmpty() ? null : Double.parseDouble(cellList.get(9).text()),
                    cellList.get(10).text().isEmpty() ? null : Double.parseDouble(cellList.get(10).text()),
                    cellList.get(11).text().isEmpty() ? null : Double.parseDouble(cellList.get(11).text()),
                    cellList.get(12).text().isEmpty() ? null : Double.parseDouble(cellList.get(12).text()),
                    cellList.get(13).text().isEmpty() ? null : Double.parseDouble(cellList.get(13).text()),
                    cellList.get(14).text().isEmpty() ? null : Double.parseDouble(cellList.get(14).text()),
                    cellList.get(15).text().isEmpty() ? null : Double.parseDouble(cellList.get(15).text()),
                    cellList.get(16).text(),
                    accSubjectResult.stream().anyMatch(p -> p.getName() == cellList.get(4).text()));

            accSubjectResult.add(item);
        }
        result.setSubjectResultList(accSubjectResult);

        return result;
    }

    interface WebDocumentUtils {
        @Nullable
        String getValueByID(Document webData, String elementId);

        @Nullable
        String getValueFromComboBoxByID(Document webData, String elementId);
    }
}
