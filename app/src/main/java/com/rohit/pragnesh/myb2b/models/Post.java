package com.rohit.pragnesh.myb2b.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Post implements Serializable {

    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("initial_price")
    @Expose
    private int initialPrice;
    @SerializedName("current_price")
    @Expose
    private int currentPrice;
    @SerializedName("date_posted")
    @Expose
    private String datePosted;
    @SerializedName("date_end")
    @Expose
    private String dateEnd;

    public Post() {
    }

    public Post(int id, String url, String author, String title, String description, String content,
                int initialPrice, int currentPrice, String datePosted, String dateEnd) {
        this.id = id;
        this.url = url;
        this.author = author;
        this.title = title;
        this.description = description;
        this.content = content;
        this.initialPrice = initialPrice;
        this.currentPrice = currentPrice;
        this.datePosted = datePosted;
        this.dateEnd = dateEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

}