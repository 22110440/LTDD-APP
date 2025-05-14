package com.example.BTL_App_truyen_tranh.models;

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
