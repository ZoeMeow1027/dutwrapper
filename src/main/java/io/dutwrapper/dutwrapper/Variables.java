package io.dutwrapper.dutwrapper;

@SuppressWarnings("SpellCheckingInspection")
public class Variables {
    public final static String URL_MAIN_SCHOOLCURRENTWEEK = "http://dut.udn.vn/Lichtuan";
    public final static String URL_SV_MAINPAGE = "http://sv.dut.udn.vn/";
    public final static String URL_SV_NEWS = "http://sv.dut.udn.vn/WebAjax/evLopHP_Load.aspx?E=%s&PAGETB=%d&COL=%s&NAME=%s&TAB=0";
    public final static String URL_SV_LOGIN = "http://sv.dut.udn.vn/PageDangNhap.aspx";
    public final static String URL_SV_CHECKLOGGEDIN = "http://sv.dut.udn.vn/WebAjax/evLopHP_Load.aspx?E=TTKBLoad&Code=2310";
    public final static String URL_SV_LOGOUT = "http://sv.dut.udn.vn/PageLogout.aspx";
    public final static String URL_SV_SUBJECTINFORMATION = "http://sv.dut.udn.vn/WebAjax/evLopHP_Load.aspx?E=TTKBLoad&Code=%d%d%d";
    public final static String URL_SV_SUBJECTFEE = "http://sv.dut.udn.vn/WebAjax/evLopHP_Load.aspx?E=THPhiLoad&Code=%d%d%d";
    public final static String URL_SV_ACCOUNTINFORMATION = "http://sv.dut.udn.vn/PageCaNhan.aspx";
    public final static String URL_SV_ACCOUNTTRAININGSTATUS = "http://sv.dut.udn.vn/PageKQRL.aspx";
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:126.0) Gecko/20100101 Firefox/126.0";

    private static boolean _showDebugLog = false;
    public static boolean getShowDebugLogStatus() { return _showDebugLog; }
    public static void setShowDebugLogStatus(boolean enabled) { _showDebugLog = enabled; }
}
