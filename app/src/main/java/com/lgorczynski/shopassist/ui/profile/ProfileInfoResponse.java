package com.lgorczynski.shopassist.ui.profile;

import com.google.gson.annotations.SerializedName;

public class ProfileInfoResponse {

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("detail")
    private String detail;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDetail() {
        return detail;
    }
}
