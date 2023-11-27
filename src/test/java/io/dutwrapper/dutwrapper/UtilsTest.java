package io.dutwrapper.dutwrapper;

import org.junit.jupiter.api.Test;

import io.dutwrapper.dutwrapper.Utils;

public class UtilsTest {
    @Test
    void finalTest() throws Exception {
        Long currentUnix = Utils.getCurrentTimeInUnix();
        System.out.println(currentUnix);

        System.out.println(Utils.getCurrentSchoolWeek().toString());
    }
}
