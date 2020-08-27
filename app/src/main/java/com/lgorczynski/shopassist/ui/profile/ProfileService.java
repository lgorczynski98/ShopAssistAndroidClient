package com.lgorczynski.shopassist.ui.profile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ProfileService {

    @FormUrlEncoded
    @POST("/account/changeusername/")
    Call<ProfileInfoResponse> changeUsername(
        @Field("username") String username,
        @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("/account/changeemail/")
    Call<ProfileInfoResponse> changeEmail(
            @Field("email") String email,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("/account/changepassword/")
    Call<ProfileInfoResponse> changePassword(
            @Field("password") String password,
            @Field("new_password") String newPassword,
            @Header("Authorization") String token
    );

}
