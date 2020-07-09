package com.lgorczynski.shopassist.ui.receipts;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Receipt implements Serializable {

    private static final String TAG = "Receipt";

    private int id;
    private String title;
    private String shopName;
    private String purchaseDate;
    private String returnDate;
    private String warrantyEndDate;
    private int returnWeeks;
    private int warrantyMonths;
    private String imageUrl;
    private String receiptUrl;
    private float price;

    public Receipt(String title, String shopName, String purchaseDate, int returnWeeks, int warrantyMonths, float price, String imageUrl, String receiptUrl) {
        this.title = title;
        this.purchaseDate = purchaseDate;
        this.imageUrl = imageUrl;
        this.price = price;
        this.shopName = shopName;
        this.returnWeeks = returnWeeks;
        this.warrantyMonths = warrantyMonths;
        this.receiptUrl = receiptUrl;

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = dateFormat.parse(purchaseDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.WEEK_OF_YEAR, returnWeeks);
            returnDate = dateFormat.format(calendar.getTime());

            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, warrantyMonths);
            warrantyEndDate = dateFormat.format(calendar.getTime());
        }
        catch(Exception e) {
            Log.d(TAG, "Receipt: Wrong date parsing!");
        }

    }

    public int getId(){
        return id;
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

    public int getReturnWeeks() {
        return returnWeeks;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }
}
