package com.lgorczynski.shopassist.ui.loyalty_cards;

import com.google.gson.annotations.SerializedName;

public class ShareResponse {

    @SerializedName("usernameTo")
    private String sharedUsersUsername;

    @SerializedName("detail")
    private String detail;

    public String getSharedUsersUsername() {
        return sharedUsersUsername;
    }

    public String getDetail() {
        return detail;
    }
}
