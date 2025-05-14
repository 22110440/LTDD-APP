package com.example.BTL_App_truyen_tranh.models;

import com.google.gson.annotations.SerializedName;

public class Genre {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

}
