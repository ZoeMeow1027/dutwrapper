package io.dutwrapper.dutwrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Accounts {
    public static class AuthInfo {
        @Nullable
        String username;
        @Nullable
        String password;

        public AuthInfo(@Nullable String username, @Nullable String password) {
            this.username = username;
            this.password = password;
        }

        public boolean isValidAuth() {
            // Check username and password is null
            if (username == null || password == null) {
                return false;
            }
            // Check username or password length is smaller than 6.
            else return username.length() >= 6 && password.length() >= 6;
        }

        public @Nullable String getUsername() {
            return username;
        }

        public @Nullable String getPassword() {
            return password;
        }
    }

    public static class Session {
        @Nullable
        String sessionId;
        @Nullable
        String viewState;
        @Nullable
        String viewStateGenerator;

        public Session(@Nullable String sessionId, @Nullable String viewState,
                       @Nullable String viewStateGenerator) {
            this.sessionId = sessionId;
            this.viewState = viewState;
            this.viewStateGenerator = viewStateGenerator;
        }

        public @Nullable String getSessionId() {
            return sessionId;
        }

        public @Nullable String getViewState() {
            return viewState;
        }

        public @Nullable String getViewStateGenerator() {
            return viewStateGenerator;
        }

        public boolean isValidSession() {
            return (sessionId != null) && (viewState != null)
                    && (viewStateGenerator != null);
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static Session getSession() throws Exception {
        HttpClientWrapper.Response get = HttpClientWrapper.get(Variables.URL_SV_LOGIN, null, 60);
        if (get.getException() != null) {
            throw get.getException();
        }
        if (get.getStatusCode() / 100 != 2) {
            throw new Exception(
                    String.format("Request not successful: Web returned with code %d", get.getStatusCode()));
        }
        try {
            if (get.getContent() == null) {
                throw new Exception("No content here! Can't get session!");
            }

            Document webData = Jsoup.parse(get.getContent());
            return new Session(
                    get.getSessionId(),
                    Utils.jsoupExtraUtils.getValue(webData.getElementById("__VIEWSTATE")),
                    Utils.jsoupExtraUtils.getValue(webData.getElementById("__VIEWSTATEGENERATOR"))
            );
        } catch (Exception ex) {
            if (Variables.getShowDebugLogStatus()) {
                ex.printStackTrace();
            }
            return new Session(get.getSessionId(), null, null);
        }
    }

    public static void login(@Nonnull Session session, @Nonnull AuthInfo auth) throws Exception {
        if (!session.isValidSession()) {
            throw new Exception("Invalid session!");
        }

        Map<String, String> data = new HashMap<>();
        data.put("__VIEWSTATE", session.getViewState());
        data.put("__VIEWSTATEGENERATOR", session.getViewStateGenerator());
        data.put("_ctl0:MainContent:DN_txtAcc", auth.getUsername());
        data.put("_ctl0:MainContent:DN_txtPass", auth.getPassword());
        data.put("_ctl0:MainContent:QLTH_btnLogin", "Đăng+nhập");

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));

        HttpClientWrapper.post(
                Variables.URL_SV_LOGIN,
                HttpClientWrapper.toUrlEncode(data, "UTF-8"),
                headers,
                60);
    }

    public static boolean isLoggedIn(@Nonnull Session session) throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));

        HttpClientWrapper.Response response = HttpClientWrapper.get(Variables.URL_SV_CHECKLOGGEDIN, headers, 60);

        if (response.getException() != null) {
            throw response.getException();
        }

        return response.getStatusCode() >= 200 && response.getStatusCode() <= 300;
    }

    public static void logout(@Nonnull Session session) throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));

        HttpClientWrapper.get(Variables.URL_SV_LOGOUT, headers, 60);
    }

    public static ArrayList<AccountInformation.SubjectInformation> fetchSubjectSchedule(@Nonnull Session session, @Nonnull Integer year,
                                                                      @Nonnull Integer semester)
            throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        if (!isLoggedIn(session)) {
            throw new Exception(
                    "No account logged in this session. May be you haven't logged in or this session is expired?");
        }

        String url;
        switch (semester) {
            case 1:
            case 2:
                url = String.format(Variables.URL_SV_SUBJECTINFORMATION, year, semester, 0);
                break;
            case 3:
                url = String.format(Variables.URL_SV_SUBJECTINFORMATION, year, semester - 1, 1);
                break;
            default:
                throw new Exception("Invalid semester!");
        }

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));
        HttpClientWrapper.Response response = HttpClientWrapper.get(
                url,
                headers,
                60);

        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new Exception(String.format(
                    "Server was return with code %d. May be you haven't logged in or this session is expired?",
                    response.getStatusCode()));
        }

        if (response.getContent() == null) {
            return new ArrayList<>();
        }

        Document webData = Jsoup.parse(response.getContent());

        ArrayList<AccountInformation.SubjectInformation> result = new ArrayList<>();

        // Schedule study
        Element schStudy = webData.getElementById("TTKB_GridInfo");
        if (schStudy != null) {
            Elements schStudyList = schStudy.getElementsByClass("GridRow");
            if (!schStudyList.isEmpty()) {
                for (Element row : schStudyList) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    if (cellList.size() < 10)
                        continue;

                    AccountInformation.SubjectInformation si = new AccountInformation.SubjectInformation();
                    si.setId(new AccountInformation.SubjectCode(cellList.get(1).text()));
                    si.setName(cellList.get(2).text());
                    try {
                        si.setCredit(Integer.parseInt(cellList.get(3).text()));
                    } catch (Exception ex) {
                        si.setCredit(0);
                    }
                    si.setIsHighQuality(cellList.get(5).attr("class").contains("GridCheck"));
                    si.setLecturer(cellList.get(6).text());

                    // Set schedule study here!
                    AccountInformation.ScheduleStudy scheduleStudy = new AccountInformation.ScheduleStudy();

                    if (!cellList.get(7).text().isEmpty()) {
                        String[] cellSplit = cellList.get(7).text().split("; ");
                        for (String cellSplitItem : cellSplit) {
                            AccountInformation.ScheduleItem scheduleItem = new AccountInformation.ScheduleItem();
                            // Set day of week
                            if (cellSplitItem.toUpperCase().contains("CN")) {
                                scheduleItem.setDayOfWeek(0);
                            } else {
                                scheduleItem
                                        .setDayOfWeek(Integer.parseInt(cellSplitItem.split(",")[0].split(" ")[1]) - 1);
                            }
                            // Set lesson
                            AccountInformation.LessonAffected lessonAffected = new AccountInformation.LessonAffected(
                                    Integer.parseInt(cellSplitItem.split(",")[1].split("-")[0]),
                                    Integer.parseInt(cellSplitItem.split(",")[1].split("-")[1])
                            );
                            scheduleItem.setLesson(lessonAffected);
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
                            AccountInformation.WeekAffected weekItem = new AccountInformation.WeekAffected(
                                    Integer.parseInt(cellSplitItem.split("-")[0]),
                                    Integer.parseInt(cellSplitItem.split("-")[1])
                            );
                            scheduleStudy.getWeekAffected().add(weekItem);
                        }
                    }

                    // Add to subject schedule item.
                    si.setScheduleStudy(scheduleStudy);

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
            if (!schExamList.isEmpty()) {
                for (Element row : schExamList) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    if (cellList.size() < 5)
                        continue;

                    AccountInformation.SubjectInformation si = result.stream()
                            .filter(s -> s.getId().toString(false).equals(cellList.get(1).text()))
                            .findFirst()
                            .orElse(null);

                    // Set subject examination here!
                    if (si != null) {
                        AccountInformation.ScheduleExam scheduleExam = new AccountInformation.ScheduleExam();
                        // Set group
                        scheduleExam.setGroup(cellList.get(3).text());
                        // Set is global
                        scheduleExam.setIsGlobal(cellList.get(4).attr("class").contains("GridCheck"));
                        // Date + Room
                        String temp = cellList.get(5).text();
                        String[] tempSplitList = temp.split(", ");
                        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
                        for (String tempSplitItem : tempSplitList) {
                            String[] itemSplit = tempSplitItem.split(": ");
                            if (itemSplit.length < 2)
                                continue;

                            // Area for day
                            if (tempSplitItem.contains("Ngày")) {
                                try {
                                    String[] dateSplit = itemSplit[1].split("/");
                                    localDateTime = LocalDateTime.of(
                                            Integer.parseInt(dateSplit[2]),
                                            Integer.parseInt(dateSplit[1]),
                                            Integer.parseInt(dateSplit[0]),
                                            0, 0, 0);
                                } catch (Exception ex) {
                                    if (Variables.getShowDebugLogStatus()) {
                                        //noinspection CallToPrintStackTrace
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            // Area for room
                            else if (tempSplitItem.contains("Phòng")) {
                                scheduleExam.setRoom(itemSplit[1]);
                            }
                            // Area for hours
                            else if (tempSplitItem.contains("Giờ")) {
                                String[] timeSplit = itemSplit[1].split("h");
                                if (timeSplit.length > 0)
                                    localDateTime = localDateTime.plusHours(Integer.parseInt(timeSplit[0]) - 7);
                                if (timeSplit.length > 1)
                                    localDateTime = localDateTime.plusMinutes(Integer.parseInt(timeSplit[1]));
                            }
                        }
                        // Set date
                        scheduleExam.setDate(localDateTime.toEpochSecond(ZoneOffset.UTC) * 1000);
                        si.setScheduleExam(scheduleExam);
                    }
                }
            }
        }

        return result;
    }

    public static ArrayList<AccountInformation.SubjectFee> fetchSubjectFee(@Nonnull Session session, @Nonnull Integer year,
                                                            @Nonnull Integer semester)
            throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        if (!isLoggedIn(session)) {
            throw new Exception(
                    "No account logged in this session. May be you haven't logged in or this session is expired?");
        }

        String url;
        switch (semester) {
            case 1:
            case 2:
                url = String.format(Variables.URL_SV_SUBJECTFEE, year, semester, 0);
                break;
            case 3:
                url = String.format(Variables.URL_SV_SUBJECTFEE, year, semester - 1, 1);
                break;
            default:
                throw new Exception("Invalid semester!");
        }

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));
        HttpClientWrapper.Response response = HttpClientWrapper.get(
                url,
                headers,
                60);

        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new Exception(String.format(
                    "Server was return with code %d. May be you haven't logged in?",
                    response.getStatusCode()));
        }

        if (response.getContent() == null) {
            return new ArrayList<>();
        }

        Document webData = Jsoup.parse(response.getContent());

        ArrayList<AccountInformation.SubjectFee> result = new ArrayList<>();

        Element schFee = webData.getElementById("THocPhi_GridInfo");
        if (schFee != null) {
            Elements schFeeList = schFee.getElementsByClass("GridRow");
            if (!schFeeList.isEmpty()) {
                for (Element row : schFeeList) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    if (cellList.size() < 10)
                        continue;

                    AccountInformation.SubjectFee sf = new AccountInformation.SubjectFee();
                    sf.setId(new AccountInformation.SubjectCode(cellList.get(1).text()));
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

    public static AccountInformation.StudentInformation fetchStudentInformation(@Nonnull Session session) throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        if (!isLoggedIn(session)) {
            throw new Exception(
                    "No account logged in this session. May be you haven't logged in or this session is expired?");
        }

        ArrayList<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));
        HttpClientWrapper.Response response = HttpClientWrapper.get(
                Variables.URL_SV_ACCOUNTINFORMATION,
                headers,
                60);

        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new Exception(String.format(
                    "Server was return with code %d. May be you haven't logged in?",
                    response.getStatusCode()));
        }

        if (response.getContent() == null) {
            throw new Exception("WrapperResponse was returned content is null");
        }

        Document webData = Jsoup.parse(response.getContent());
        AccountInformation.StudentInformation result = new AccountInformation.StudentInformation(
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtHoTen")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtNgaySinh")),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboNoiSinh"))),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtGioiTinh")),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboDanToc"))),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboQuocTich"))),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtSoCMND")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtNgayCap")),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboNoiCap"))),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtSoCCCD")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtNcCCCD")),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboTonGiao"))),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtTKNHang")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtNgHang")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtSoBHYT")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtHanBHYT")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("MainContent_CN_txtNganh")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtLop")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("MainContent_CN_txtCTDT")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("MainContent_CN_txtCT2")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtMail1")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtMail2")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtMK365")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtFace")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtPhone")),
                Utils.jsoupExtraUtils.getValue(webData.getElementById("CN_txtCuTru")),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboDCCua"))),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboTinhCTru"))),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_cboQuanCTru"))),
                Utils.jsoupExtraUtils.getText(
                        Utils.jsoupExtraUtils.getSelectedValueFromComboBox(webData.getElementById("CN_divPhuongCTru"))),
                "");

        Element element = webData.getElementById("Main_lblHoTen");
        if (element != null) {
            String temp = element.text();
            result.setStudentId(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")")));
        } else
            result.setStudentId("");

        return result;
    }

    public static AccountInformation.TrainingStatus fetchTrainingStatus(@Nonnull Session session) throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        if (!isLoggedIn(session)) {
            throw new Exception(
                    "No account logged in this session. May be you haven't logged in or this session is expired?");
        }

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));
        HttpClientWrapper.Response response = HttpClientWrapper.get(
                Variables.URL_SV_ACCOUNTTRAININGSTATUS,
                headers,
                60);

        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new Exception(String.format(
                    "Server was return with code %d. May be you haven't logged in?",
                    response.getStatusCode()));
        }

        if (response.getContent() == null) {
            throw new Exception("WrapperResponse was returned content is null");
        }

        Document webData = Jsoup.parse(response.getContent());
        AccountInformation.TrainingStatus result = new AccountInformation.TrainingStatus();

        // Training status
        AccountInformation.TrainingSummary trainSum = new AccountInformation.TrainingSummary();
        Elements accTrainingStatus = webData.getElementById("KQRLGridTH").getElementsByClass("GridRow");
        for (Element data : accTrainingStatus) {
            Elements cellData = data.getElementsByClass("GridCell");
            if (cellData.get(0).text().isEmpty() || cellData.get(cellData.size() - 1).text().isEmpty()
                    || cellData.get(cellData.size() - 2).text().isEmpty()
                    || cellData.get(cellData.size() - 3).text().isEmpty())
                continue;

            boolean first = false;
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
        AccountInformation.GraduateStatus graduateStatus = new AccountInformation.GraduateStatus(
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
        ArrayList<AccountInformation.SubjectResult> accSubjectResult = new ArrayList<>();
        Elements accSubjectResultList = webData.getElementById("KQRL_divContent").getElementById("KQRLGridKQHT")
                .getElementsByClass("GridRow");
        for (int i = accSubjectResultList.size() - 1; i >= 0; i--) {
            Elements cellList = accSubjectResultList.get(i).getElementsByClass("GridCell");
            AccountInformation.SubjectResult item = new AccountInformation.SubjectResult(
                    Integer.parseInt(cellList.get(0).text()),
                    cellList.get(1).text(),
                    cellList.get(2).hasClass("GridCheck"),
                    new AccountInformation.SubjectCode(cellList.get(3).text()),
                    cellList.get(4).text(),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(5)),
                    cellList.get(6).text().isEmpty() ? null : cellList.get(6).text(),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(7)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(8)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(9)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(10)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(11)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(12)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(13)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(14)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(15)),
                    Utils.jsoupExtraUtils.elementToDoubleOrNull(cellList.get(16)),
                    cellList.get(17).text(),
                    accSubjectResult.stream().anyMatch(p -> p.getId().getSubjectId().compareTo(new AccountInformation.SubjectCode(cellList.get(3).text()).getSubjectId() != null ? (new AccountInformation.SubjectCode(cellList.get(3).text()).getSubjectId()) : "") == 0)
            );

            accSubjectResult.add(item);
        }
        result.setSubjectResultList(accSubjectResult);

        return result;
    }

    public static ArrayList<AccountInformation.FormRequest> fetchFormRequest(@Nonnull Session session) throws Exception {
        if (!session.isValidSession())
            throw new Exception("Invalid session!");

        if (!isLoggedIn(session)) {
            throw new Exception(
                    "No account logged in this session. May be you haven't logged in or this session is expired?");
        }

        List<HttpClientWrapper.Header> headers = new ArrayList<>(Collections.singletonList(
                new HttpClientWrapper.Header("Cookie",
                        Utils.jsoupExtraUtils.sessionIdToSubCookie(session.getSessionId()))));
        HttpClientWrapper.Response response = HttpClientWrapper.get(
                Variables.URL_SV_FORMREQUESTED,
                headers,
                60);

        if (response.getException() != null) {
            throw response.getException();
        }

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new Exception(String.format(
                    "Server was return with code %d. May be you haven't logged in or this session is expired?",
                    response.getStatusCode()));
        }

        if (response.getContent() == null) {
            return new ArrayList<>();
        }

        Document webData = Jsoup.parse(response.getContent());

        ArrayList<AccountInformation.FormRequest> result = new ArrayList<>();

        Element formRequest = webData.getElementById("LDDN_GridInfo");
        if (formRequest != null) {
            Elements requestItem = formRequest.getElementsByClass("GridRow");
            if (!requestItem.isEmpty()) {
                for (Element row : requestItem) {
                    Elements cellList = row.getElementsByClass("GridCell");

                    AccountInformation.FormRequest sf = new AccountInformation.FormRequest(
                            cellList.get(1).text(),
                            cellList.get(2).text(),
                            cellList.get(3).text(),
                            convertToDateTimeUnix(cellList.get(4).text()),
                            Integer.parseInt(cellList.get(5).text()),
                            convertToDateTimeUnix(cellList.get(10).text()),
                            convertToDateTimeUnix(cellList.get(11).text()),
                            convertToDateTimeUnix(cellList.get(12).text()),
                            cellList.get(13).attr("class").contains("GridCheck"),
                            cellList.get(14).text()
                    );
                    result.add(sf);
                }
            }

        }

        return result;
    }

    private static Long convertToDateTimeUnix(@Nonnull String formRequestTimeString) {
        String regex = "(\\d{2}):(\\d{2}) (\\d{2})/(\\d{2})/(\\d{4})";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(formRequestTimeString);

        try {
            if (matcher.find()) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(matcher.group(5)));
                cal.set(Calendar.MONTH, Integer.parseInt(matcher.group(4)) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(3)));
                cal.set(
                        Calendar.HOUR,
                        Integer.parseInt(matcher.group(1)) == 12
                                ? 12
                                : Integer.parseInt(matcher.group(2)) < 7
                                ? Integer.parseInt(matcher.group(1)) + 12
                                : Integer.parseInt(matcher.group(1))
                );
                cal.set(Calendar.MINUTE, Integer.parseInt(matcher.group(1)));
                cal.set(Calendar.SECOND, 0);
                return cal.toInstant().toEpochMilli();
            }
            else return 0L;
        } catch (Exception ex) {
            return 0L;
        }
    }

}
