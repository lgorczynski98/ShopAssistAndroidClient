package com.lgorczynski.shopassist;

public class Receipt {

    private String title;
    private String purchaseDate;
    private String imageUrl;
    private float price;

    public Receipt(String title, String purchaseDate, String imageUrl, float price) {
        this.title = title;
        this.purchaseDate = purchaseDate;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
