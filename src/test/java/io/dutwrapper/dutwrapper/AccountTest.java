package io.dutwrapper.dutwrapper;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class AccountTest {
    @Test
    void test() throws Exception {
        String envDutAccount = System.getenv("dut_account");
        if (envDutAccount == null)
            throw new Exception("No account found! Please define \"dut_account\" variable to test account library.");
        if (envDutAccount.split("\\|").length != 2)
            throw new Exception("\"dut_account\" variable isn't define correctly! Please follow format username|password.");
        String user = envDutAccount.split("\\|")[0];
        String pass = envDutAccount.split("\\|")[1];
        Integer year = 20;
        Integer semester = 2;

        Account.Session session = Account.getSession();
        Account.AuthInfo authInfo = new Account.AuthInfo(user, pass);
        Gson gson = new Gson();

        System.out.println("Create new Session");
        System.out.println(gson.toJson(session));
        System.out.println(gson.toJson(Account.isLoggedIn(session)));
        System.out.println("\nLogin and check again");
        Account.login(session, authInfo);
        System.out.println(gson.toJson(Account.isLoggedIn(session)));
        System.out.println("\nFetch subject schedule list");
        System.out.println(gson.toJson(Account.fetchSubjectSchedule(session, year, semester)));
        System.out.println("\nFetch subject fee list");
        System.out.println(gson.toJson(Account.fetchSubjectFee(session, year, semester)));
        System.out.println("\nFetch account information");
        System.out.println(gson.toJson(Account.fetchAccountInformation(session)));
        System.out.println("\nFetch account training status");
        System.out.println(gson.toJson(Account.fetchAccountTrainingStatus(session)));
        System.out.println("\nLogout and check if logged out");
        Account.logout(session);
        System.out.println(gson.toJson(Account.isLoggedIn(session)));
    }
}
