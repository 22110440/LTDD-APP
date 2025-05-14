package com.example.BTL_App_truyen_tranh.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chapter {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private List<String> image;

    @SerializedName("comicId")
    private int comicId;

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

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public int getComicId() {
        return comicId;
    }

    public void setComicId(int comicId) {
        this.comicId = comicId;
    }

    @Override
    public String toString() {
        return title;
    }
}
