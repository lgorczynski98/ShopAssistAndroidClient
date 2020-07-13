package com.lgorczynski.shopassist.ui.receipts;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ReceiptsService {

    @GET("receipts/")
    Call<List<Receipt>> getReceipts(
        @Header("Authorization") String token
    );

    @Multipart
    @POST("receipts/")
    Call<Receipt> postReceipt(
        @Part("title") RequestBody title,
        @Part("shop_name") RequestBody shopName,
        @Part("purchase_date") RequestBody purchaseDate,
        @Part("purchase_cost") RequestBody purchaseCost,
        @Part("weeks_to_return") RequestBody weeksToReturn,
        @Part("months_of_warranty") RequestBody monthsOfWarranty,
        @Part MultipartBody.Part image,
        @Part MultipartBody.Part thumbnail,
        @Header("Authorization") String token
    );

    @Multipart
    @POST("receipts/")
    Call<Receipt> postReceipt(
            @Part("title") RequestBody title,
            @Part("shop_name") RequestBody shopName,
            @Part("purchase_date") RequestBody purchaseDate,
            @Part("purchase_cost") RequestBody purchaseCost,
            @Part("weeks_to_return") RequestBody weeksToReturn,
            @Part("months_of_warranty") RequestBody monthsOfWarranty,
            @Part MultipartBody.Part image,
            @Header("Authorization") String token
    );

    @DELETE("receipts/{receipt_id}/")
    Call<Receipt> deleteReceipt(
            @Path(value = "receipt_id", encoded = true) int receiptID,
            @Header("Authorization") String token
    );
}
