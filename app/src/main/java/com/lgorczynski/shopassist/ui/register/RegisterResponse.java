package com.lgorczynski.shopassist.ui.register;

import com.google.gson.annotations.SerializedName;
import com.lgorczynski.shopassist.ui.log_in.LoginResponse;

public class RegisterResponse extends LoginResponse {

    @SerializedName("detail_email")
    private String emailDetail;

    @SerializedName("detail_username")
    private String usernameDetail;

    public String getEmailDetail() {
        return emailDetail;
    }

    public String getUsernameDetail() {
        return usernameDetail;
    }
}
