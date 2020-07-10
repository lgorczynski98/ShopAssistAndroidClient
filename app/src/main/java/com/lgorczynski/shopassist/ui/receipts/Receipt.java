package com.lgorczynski.shopassist.ui.receipts;

import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Receipt implements Serializable {

    private static final String TAG = "Receipt";

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("shop_name")
    @Expose
    private String shopName;

    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;

    @SerializedName("weeks_to_return")
    @Expose
    private int returnWeeks;

    @SerializedName("months_of_warranty")
    @Expose
    private int warrantyMonths;

    @SerializedName("thumbnail")
    @Expose
    private String imageUrl;

    @SerializedName("image")
    @Expose
    private String receiptUrl;

    @SerializedName("purchase_cost")
    @Expose
    private float price;

    private String returnDate;
    private String warrantyEndDate;

    public Receipt(String title, String shopName, String purchaseDate, int returnWeeks, int warrantyMonths, float price, String imageUrl, String receiptUrl) {
        this.title = title;
        this.purchaseDate = purchaseDate;
        this.imageUrl = imageUrl;
        this.price = price;
        this.shopName = shopName;
        this.returnWeeks = returnWeeks;
        this.warrantyMonths = warrantyMonths;
        this.receiptUrl = receiptUrl;
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
        if(returnDate == null){
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(purchaseDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.WEEK_OF_YEAR, returnWeeks);
                returnDate = dateFormat.format(calendar.getTime());
            }
            catch(Exception e) {
                Log.d(TAG, "getReturnDate: Wrong return date parsing");
            }
        }
        return returnDate;
    }

    public String getWarrantyEndDate() {
        if(warrantyEndDate == null){
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(purchaseDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH, warrantyMonths);
                warrantyEndDate = dateFormat.format(calendar.getTime());
            }
            catch(Exception e) {
                Log.d(TAG, "getReturnDate: Wrong warranty end date parsing");
            }
        }
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setReturnWeeks(int returnWeeks) {
        this.returnWeeks = returnWeeks;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
