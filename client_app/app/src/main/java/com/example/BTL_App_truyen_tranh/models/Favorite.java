package com.example.BTL_App_truyen_tranh.models;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    @SerializedName("id")
    private int id;

    @SerializedName("comic")
    private Comic comic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }
}
