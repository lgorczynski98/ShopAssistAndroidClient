package com.lgorczynski.shopassist.ui.log_in;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository {

    private static final String LOGIN_SERVICE_BASE_URL = "http://192.168.0.52:8000/";
    private static final String TAG = "LoginRepository";

    private LoginService loginService;
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;

    public LoginRepository(){
        loginResponseMutableLiveData = new MutableLiveData<>();

        loginService = new Retrofit.Builder()
                .baseUrl(LOGIN_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginService.class);
    }

    public void loginUser(String email, String password){
        loginService.login(email, password)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.isSuccessful()){
                            loginResponseMutableLiveData.postValue(response.body());
                            Log.d(TAG, "onResponse: " + response.body().getToken());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        loginResponseMutableLiveData.postValue(new LoginResponse());
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public LiveData<LoginResponse> getLoginResponseLiveData(){
        return loginResponseMutableLiveData;
    }

}
