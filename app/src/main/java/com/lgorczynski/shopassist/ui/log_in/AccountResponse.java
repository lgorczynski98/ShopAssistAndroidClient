package com.lgorczynski.shopassist.ui.log_in;

import com.google.gson.annotations.SerializedName;

public class AccountResponse {

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("device_registration_token")
    private String deviceRegistrationToken;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDeviceRegistrationToken() {
        return deviceRegistrationToken;
    }
}
