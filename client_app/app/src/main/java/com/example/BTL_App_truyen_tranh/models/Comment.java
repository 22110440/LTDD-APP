package com.example.BTL_App_truyen_tranh.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    @SerializedName("id")
    private Integer id;

    @SerializedName("text")
    private String text;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("user")
    private User user;

    @SerializedName("comic")
    private Comic comic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
        return dateFormat.format(createdAt);

    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }
}
