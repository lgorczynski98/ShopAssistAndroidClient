package com.lgorczynski.shopassist.ui.receipts;

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

public class ReceiptsRepository {

    private static final String RECEIPTS_SERVICE_BASE_URL = CredentialsSingleton.BASE_URL;
    private static final String TAG = "ReceiptsRepository";

    private ReceiptsService receiptsService;
    private MutableLiveData<List<Receipt>> receiptsResponseMutableLiveData;

    public ReceiptsRepository(){
        receiptsResponseMutableLiveData = new MutableLiveData<>();

        receiptsService = new Retrofit.Builder()
                .baseUrl(RECEIPTS_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReceiptsService.class);
    }

    public void getReceipts(String token){

        receiptsService.getReceipts(token)
                .enqueue(new Callback<List<Receipt>>() {
                    @Override
                    public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
                        receiptsResponseMutableLiveData.postValue(response.body());
                        Log.d(TAG, "onResponse: successfull");
                    }

                    @Override
                    public void onFailure(Call<List<Receipt>> call, Throwable t) {
                        receiptsResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

    }

    public MutableLiveData<List<Receipt>> getReceiptsResponseMutableLiveData() {
        return receiptsResponseMutableLiveData;
    }
}
