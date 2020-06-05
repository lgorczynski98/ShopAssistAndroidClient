package com.lgorczynski.shopassist.ui.register;

import com.lgorczynski.shopassist.ui.log_in.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterService {

    @FormUrlEncoded
    @POST("account/register/")
    Call<LoginResponse> register(
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password
    );
}
