package com.lgorczynski.shopassist.ui.loyalty_cards;

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
import retrofit2.http.Query;

public interface LoyaltyCardsService {

    @GET("loyaltycards/")
    Call<List<LoyaltyCard>> getLoyaltyCards(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("loyaltycards/")
    Call<LoyaltyCard> postLoyaltyCard(
            @Part("title") RequestBody title,
            @Part("barcode_format") RequestBody barcode_format,
            @Part("barcode_content") RequestBody barcode_content,
            @Part MultipartBody.Part image,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("loyaltycards/")
    Call<LoyaltyCard> postLoyaltyCards(
            @Field("title") String title,
            @Field("barcode_format") String barcode_format,
            @Field("barcode_content") String barcode_content,
            @Header("Authorization") String token
    );

    @DELETE("loyaltycards/{card_id}/")
    Call<LoyaltyCard> deleteLoyaltyCard(
            @Path(value = "card_id", encoded = true) int cardID,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @PATCH("loyaltycards/{card_id}/")
    Call<LoyaltyCard> patchLoyaltyCard(
            @Path(value = "card_id", encoded = true) int cardID,
            @Field("title") String title,
            @Header("Authorization") String token
    );

    @Multipart
    @PATCH("loyaltycards/{card_id}/")
    Call<LoyaltyCard> patchLoyaltyCard(
            @Path(value = "card_id", encoded = true) int cardID,
            @Part() MultipartBody.Part image,
            @Header("Authorization") String token
    );

    @Multipart
    @PATCH("loyaltycards/{card_id}/")
    Call<LoyaltyCard> patchLoyaltyCard(
            @Path(value = "card_id", encoded = true) int cardID,
            @Part("title") RequestBody title,
            @Part() MultipartBody.Part image,
            @Header("Authorization") String token
    );

    @POST("loyaltycards/share/{card_id}/{usersToShareUsername}/")
    Call<ShareResponse> shareLoyaltyCard(
            @Path(value = "card_id", encoded = true) int cardID,
            @Path(value = "usersToShareUsername", encoded = true) String username,
            @Header("Authorization") String token
    );

}
