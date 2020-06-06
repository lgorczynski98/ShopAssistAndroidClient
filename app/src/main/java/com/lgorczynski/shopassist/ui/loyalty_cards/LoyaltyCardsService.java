package com.lgorczynski.shopassist.ui.loyalty_cards;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LoyaltyCardsService {

    @GET("loyaltycards/")
    Call<List<LoyaltyCard>> getLoyaltyCards(
            @Header("Authorization") String token
    );

}
