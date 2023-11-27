package io.dutwrapper.dutwrapper.model.enums;

public enum NewsSearchType {
    ByTitle("TieuDe"),
    ByContent("NoiDung");

    private String value;
    private NewsSearchType(String s) {
        this.value = s;
    }

    public String toString() {
        return value;
    }
}
