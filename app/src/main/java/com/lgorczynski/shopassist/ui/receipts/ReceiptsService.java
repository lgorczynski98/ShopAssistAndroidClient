package com.lgorczynski.shopassist.ui.receipts;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
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

   @FormUrlEncoded
   @PATCH("receipts/{receipt_id}/")
   Call<Receipt> patchReceipt(
           @Path(value = "receipt_id", encoded=true) int receiptID,
           @Field("title") String title,
           @Field("shop_name") String shopName,
           @Field("purchase_date") String purchaseDate,
           @Field("purchase_cost") String purchaseCost,
           @Field("weeks_to_return") int weeksToReturn,
           @Field("months_of_warranty") int monthsOfWarranty,
           @Header("Authorization") String token
   );

   @Multipart
   @PATCH("receipts/{receipt_id}/")
   Call<Receipt> patchReceipt(
           @Path(value = "receipt_id", encoded=true) int receiptID,
           @Part() MultipartBody.Part thumbnail,
           @Header("Authorization") String token
   );

    @Multipart
    @PATCH("receipts/{receipt_id}/")
    Call<Receipt> patchReceipt(
            @Path(value = "receipt_id", encoded=true) int receiptID,
            @Part("title") RequestBody title,
            @Part("shop_name") RequestBody shopName,
            @Part("purchase_date") RequestBody purchaseDate,
            @Part("purchase_cost") RequestBody purchaseCost,
            @Part("weeks_to_return") RequestBody weeksToReturn,
            @Part("months_of_warranty") RequestBody monthsOfWarranty,
            @Part() MultipartBody.Part thumbnail,
            @Header("Authorization") String token
    );

}
