package io.dutwrapper.dutwrapper;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import io.dutwrapper.dutwrapper.customrequest2.WrapperResponse;
import io.dutwrapper.dutwrapper.model.accounts.AccountInformation;
import io.dutwrapper.dutwrapper.model.accounts.SubjectFeeItem;
import io.dutwrapper.dutwrapper.model.accounts.SubjectScheduleItem;
import io.dutwrapper.dutwrapper.model.accounts.trainingresult.AccountTrainingStatus;

import java.io.IOException;
import java.util.ArrayList;

class AccountTest {
    static String initialize() throws IOException {
        WrapperResponse response = Account.getSessionId();
        return response.getSessionId();
    }

    static void login(String sessionId, String user, String pass) throws Exception {
        Account.login(
                sessionId,
                user,
                pass);

        if (Account.isLoggedIn(sessionId)) {
            System.out.println("Logged in!");
        } else
            throw new Exception("This Session ID hasn't logged in!");
    }

    static void getSubjectSchedule(String sessionId, Integer year, Integer semester) throws Exception {
        ArrayList<SubjectScheduleItem> subjectScheduleList = Account.getSubjectSchedule(sessionId, year, semester);
        for (SubjectScheduleItem subjectScheduleItem: subjectScheduleList) {
            System.out.println(String.format(
                "Subject #%d\nName: %s\nID: %s\n========================",
                subjectScheduleList.indexOf(subjectScheduleItem),
                subjectScheduleItem.getName(),
                subjectScheduleItem.getId()
            ));
        }
    }

    static void getSubjectFee(String sessionId, Integer year, Integer semester) throws Exception {
        ArrayList<SubjectFeeItem> subjectFeeList = Account.getSubjectFee(sessionId, year, semester);
        for (SubjectFeeItem subjectFeeItem: subjectFeeList) {
            System.out.println(String.format(
                "Subject #%d\nName: %s\nID: %s\n========================",
                subjectFeeList.indexOf(subjectFeeItem),
                subjectFeeItem.getName(),
                subjectFeeItem.getId()
            ));
        }
    }

    static void getAccountInformation(String sessionId) throws Exception {
        AccountInformation accInfo = Account.getAccountInformation(sessionId);
        System.out.println(new Gson().toJson(accInfo));
    }

    static void getAccountTrainingStatus(String sessionId) throws Exception {
        AccountTrainingStatus accountTrainingStatus = Account.getAccountTrainingStatus(sessionId);
        System.out.println(new Gson().toJson(accountTrainingStatus));
    }

    static void logout(String sessionId) throws Exception {
        Account.logout(sessionId);

        if (!Account.isLoggedIn(sessionId)) {
            System.out.println("Logged out!");
        } else
            throw new Exception("This Session ID hasn't logged out yet!");
    }

    @Test
    void finalTest() throws Exception {
        String sessionId;

        String env = System.getenv("dut_account");

        if (env == null)
            throw new Exception("No account found! Please define \"dut_account\" variable to test account library.");

        String user = env.split("\\|")[0];
        String pass = env.split("\\|")[1];

        Integer year = 22;
        Integer semester = 1;

        // Initialize Session ID
        sessionId = initialize();
        System.out.println(sessionId);

        // Login
        login(sessionId, user, pass);

        // Subject schedule
        getSubjectSchedule(sessionId, year, semester);

        // Subject fee
        getSubjectFee(sessionId, year, semester);

        // Account information
        getAccountInformation(sessionId);

        // Account training status
        getAccountTrainingStatus(sessionId);

        // Logout
        logout(sessionId);
    }
}