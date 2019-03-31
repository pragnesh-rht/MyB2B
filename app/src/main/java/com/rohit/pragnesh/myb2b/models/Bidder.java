package com.rohit.pragnesh.myb2b.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bidder {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("post")
    @Expose
    private String post;
    @SerializedName("prices")
    @Expose
    private int prices;
    @SerializedName("bid_author")
    @Expose
    private String bidAuthor;
    @SerializedName("non_field_errors")
    @Expose
    private String non_field_errors;

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getPrices() {
        return prices;
    }

    public void setPrices(int prices) {
        this.prices = prices;
    }

    public String getBidAuthor() {
        return bidAuthor;
    }

    public void setBidAuthor(String bidAuthor) {
        this.bidAuthor = bidAuthor;
    }

    public String getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(String non_field_errors) {
        this.non_field_errors = non_field_errors;
    }
}