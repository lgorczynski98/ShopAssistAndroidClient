package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lgorczynski.shopassist.ui.log_in.CredentialsSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoyaltyCardsRepository {

    private static final String LOYALTYCARDS_SERVICE_BASE_URL = CredentialsSingleton.BASE_URL;
    private static final String TAG = "LoyaltyCardsRepository";

    private LoyaltyCardsService loyaltyCardsService;
    private MutableLiveData<List<LoyaltyCard>> loyaltyCardsResponseMutableLiveData;

    public LoyaltyCardsRepository(){
        loyaltyCardsResponseMutableLiveData = new MutableLiveData<>();

        loyaltyCardsService = new Retrofit.Builder()
                .baseUrl(LOYALTYCARDS_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoyaltyCardsService.class);
    }

    public void getLoyaltyCards(String token){

        loyaltyCardsService.getLoyaltyCards(token)
                .enqueue(new Callback<List<LoyaltyCard>>() {
                    @Override
                    public void onResponse(Call<List<LoyaltyCard>> call, Response<List<LoyaltyCard>> response) {
                        loyaltyCardsResponseMutableLiveData.postValue(response.body());
                        Log.d(TAG, "onResponse: successfull");
                    }

                    @Override
                    public void onFailure(Call<List<LoyaltyCard>> call, Throwable t) {
                        loyaltyCardsResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public LiveData<List<LoyaltyCard>> getLoyaltyCardsResponseMutableLiveData() {
        return loyaltyCardsResponseMutableLiveData;
    }
}
