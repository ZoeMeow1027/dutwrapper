package io.dutwrapper.dutwrapper;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class AccountTest {
    @Test
    void test() throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String envDutAccount = System.getenv("dut_account");
        // System.out.println(envDutAccount);
        if (envDutAccount == null)
            throw new Exception("No account found! Please define \"dut_account\" variable to test account library.");
        if (envDutAccount.split("\\|").length != 2)
            throw new Exception("\"dut_account\" variable isn't define correctly! Please follow format username|password.");
        String user = envDutAccount.split("\\|")[0];
        String pass = envDutAccount.split("\\|")[1];
        Integer year = 20;
        Integer semester = 2;

        Accounts.Session session = Accounts.getSession();
        Accounts.AuthInfo authInfo = new Accounts.AuthInfo(user, pass);

        System.out.println("Create new session");
        System.out.println(gson.toJson(session));
        System.out.println(gson.toJson(Accounts.isLoggedIn(session)));
        System.out.println("\nLogin and check again");
        Accounts.login(session, authInfo);
        System.out.println(gson.toJson(Accounts.isLoggedIn(session)));
        System.out.println("\nFetch subject schedule list");
        System.out.println(gson.toJson(Accounts.fetchSubjectSchedule(session, year, semester)));
        System.out.println("\nFetch subject fee list");
        System.out.println(gson.toJson(Accounts.fetchSubjectFee(session, year, semester)));
        System.out.println("\nFetch account information");
        System.out.println(gson.toJson(Accounts.fetchStudentInformation(session)));
        System.out.println("\nFetch account training status");
        System.out.println(gson.toJson(Accounts.fetchTrainingStatus(session)));
        System.out.println("\nLogout and check if logged out");
        Accounts.logout(session);
        System.out.println(gson.toJson(Accounts.isLoggedIn(session)));
    }
}
