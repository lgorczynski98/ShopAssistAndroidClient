package com.lgorczynski.shopassist.ui.log_in;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    private String token;

    public String getToken(){
        return token;
    }

}
