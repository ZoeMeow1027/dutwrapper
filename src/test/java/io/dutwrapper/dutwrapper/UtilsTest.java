package io.dutwrapper.dutwrapper;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class UtilsTest {
    @Test
    void finalTest() throws Exception {
        Utils.DutSchoolYearItem test = Utils.getCurrentSchoolWeek();
        System.out.println(new Gson().toJson(test));
        System.out.println(test);
    }
}
