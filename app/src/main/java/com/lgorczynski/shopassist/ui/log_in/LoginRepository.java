package com.lgorczynski.shopassist.ui.log_in;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lgorczynski.shopassist.ui.CredentialsSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository {

    private static final String LOGIN_SERVICE_BASE_URL = CredentialsSingleton.BASE_URL;
    private static final String TAG = "LoginRepository";

    private LoginService loginService;
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<AccountResponse> accountResponseMutableLiveData;

    public LoginRepository(){
        loginResponseMutableLiveData = new MutableLiveData<>();
        accountResponseMutableLiveData = new MutableLiveData<>();

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
                        loginResponseMutableLiveData.postValue(response.body());
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: " + response.body().getToken());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        loginResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void getAccountDetails(int userID, String token){
        loginService.getAccount(userID, token)
                .enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if(response.isSuccessful()){
                            accountResponseMutableLiveData.postValue(response.body());
                            Log.d(TAG, "onResponse: got account details for: " + response.body().getUsername());

                            CredentialsSingleton.getInstance().setUsername(response.body().getUsername());
                            CredentialsSingleton.getInstance().setEmail(response.body().getEmail());
                            Log.d(TAG, "onChanged: Username: " + response.body().getUsername());
                            Log.d(TAG, "onChanged: Email: " + response.body().getEmail());
                            Log.d(TAG, "onChanged: Server device registration token: " + response.body().getDeviceRegistrationToken());
                            Log.d(TAG, "onChanged: On device registration token: " + CredentialsSingleton.getInstance().getDeviceRegistrationToken());
                            if(response.body().getDeviceRegistrationToken() == null || !response.body().getDeviceRegistrationToken().equals(CredentialsSingleton.getInstance().getDeviceRegistrationToken()))
                                patchAccountDetails(CredentialsSingleton.getInstance().getUserID(), CredentialsSingleton.getInstance().getDeviceRegistrationToken(), CredentialsSingleton.getInstance().getToken());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        accountResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    public void patchAccountDetails(int userID, String deviceRegistrationToken, String token){
        loginService.patchAccount(userID, deviceRegistrationToken, token)
                .enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "onResponse: Device registration token updated sucessfully");
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: Device registration token update failed");
                    }
                });
    }

    public LiveData<LoginResponse> getLoginResponseLiveData(){
        return loginResponseMutableLiveData;
    }

    public LiveData<AccountResponse> getAccountResponseLiveData(){
        return accountResponseMutableLiveData;
    }

}
