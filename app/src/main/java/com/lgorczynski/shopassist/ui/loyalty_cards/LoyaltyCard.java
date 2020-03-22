package com.lgorczynski.shopassist.ui.loyalty_cards;

public class LoyaltyCard {

    private String title;
    private String imageUrl;
    private String format;
    private String content;

    public LoyaltyCard(String title, String imageUrl, String format, String content) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.format = format;
        this.content = content;
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
