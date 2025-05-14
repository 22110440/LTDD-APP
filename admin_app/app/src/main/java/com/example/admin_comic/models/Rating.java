package com.example.admin_comic.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Rating {

    @SerializedName("id")
    private Integer id;
    @SerializedName("rating")
    private Integer rating;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("user")
    private User user;

    @SerializedName("comic")
    private Comic comic;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
