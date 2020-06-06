package com.lgorczynski.shopassist.ui.loyalty_cards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyCard {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image")
    @Expose
    private String imageUrl;

    @SerializedName("barcode_format")
    @Expose
    private String format;

    @SerializedName("barcode_content")
    @Expose
    private String content;

    public LoyaltyCard(String title, String imageUrl, String format, String content) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.format = format;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
