package com.lgorczynski.shopassist.ui.log_in;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @FormUrlEncoded
    @POST("/account/login/")
    Call<LoginResponse> login(
            @Field("username") String email,
            @Field("password") String password
    );

}
