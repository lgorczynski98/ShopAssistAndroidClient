package com.lgorczynski.shopassist.connections;

import com.lgorczynski.shopassist.ui.loyalty_cards.LoyaltyCard;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ConnectionService {

    @GET("logo/")
    Call<ResponseBody> checkServerConnection();

}
