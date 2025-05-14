package com.example.admin_comic.models;

public enum ChartFilter {
    DAY("day"),
    MONTH("month"),
    WEEK("week");

    private String value;

    ChartFilter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
