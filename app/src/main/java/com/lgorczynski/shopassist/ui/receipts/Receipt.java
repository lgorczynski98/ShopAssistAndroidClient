package com.lgorczynski.shopassist.ui.receipts;

import android.os.Parcelable;

import java.io.Serializable;

public class Receipt implements Serializable {

    private String title;
    private String shopName;
    private String purchaseDate;
    private String returnDate;
    private String warrantyEndDate;
    private String imageUrl;
    private String receiptUrl;
    private float price;

    public Receipt(String title, String shopName, String purchaseDate, String returnDate, String warrantyEndDate, float price, String imageUrl, String receiptUrl) {
        this.title = title;
        this.purchaseDate = purchaseDate;
        this.imageUrl = imageUrl;
        this.price = price;
        this.shopName = shopName;
        this.returnDate = returnDate;
        this.warrantyEndDate = warrantyEndDate;
        this.receiptUrl = receiptUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public String getShopName() {
        return shopName;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }
}
