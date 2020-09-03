package com.lgorczynski.shopassist.ui.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.log_in.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterRepository {

    private static final String REGISTER_SERVICE_BASE_URL = CredentialsSingleton.BASE_URL;
    private static final String TAG = "RegisterRepository";

    private RegisterService registerService;
    private MutableLiveData<RegisterResponse> registerResponseMutableLiveData;

    public RegisterRepository(){
        registerResponseMutableLiveData = new MutableLiveData<>();

        registerService = new Retrofit.Builder()
                .baseUrl(REGISTER_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RegisterService.class);
    }

    public void registerUser(String email, String username, String password){
        registerService.register(email, username, password)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.isSuccessful()){
                            registerResponseMutableLiveData.postValue(response.body());
                            Log.d(TAG, "onResponse: " + response.body().getToken());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        registerResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public LiveData<RegisterResponse> getRegisterResponseLiveData(){
        return registerResponseMutableLiveData;
    }

}
