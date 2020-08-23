package com.lgorczynski.shopassist.ui.log_in;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginService {

    @FormUrlEncoded
    @POST("/account/login/")
    Call<LoginResponse> login(
            @Field("username") String email,
            @Field("password") String password
    );

    @GET("/account/{user_id}/")
    Call<AccountResponse> getAccount(
            @Path(value = "user_id", encoded = true) int userID,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @PATCH("/account/{user_id}/")
    Call<AccountResponse> patchAccount(
            @Path(value = "user_id", encoded = true) int userID,
            @Field("device_registration_token") String deviceRegistrationToken,
            @Header("Authorization") String token
    );

}
