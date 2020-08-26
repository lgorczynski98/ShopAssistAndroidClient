package com.lgorczynski.shopassist.ui.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lgorczynski.shopassist.ui.CredentialsSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileRepository {

    private static final String PROFILE_INFO_BASE_URL = CredentialsSingleton.BASE_URL;
    private static final String TAG = "ProfileRepository";

    private ProfileService profileService;
    private MutableLiveData<ProfileInfoResponse> profileInfoResponseMutableLiveData;

    public ProfileRepository(){
        profileInfoResponseMutableLiveData = new MutableLiveData<>();

        profileService = new Retrofit.Builder()
                .baseUrl(PROFILE_INFO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProfileService.class);
    }

    public void changeUsername(String username, String token){
        profileService.changeUsername(username, token)
                .enqueue(new Callback<ProfileInfoResponse>() {
                    @Override
                    public void onResponse(Call<ProfileInfoResponse> call, Response<ProfileInfoResponse> response) {
                        profileInfoResponseMutableLiveData.postValue(response.body());
                        try {
                            Log.d(TAG, "onResponse: Username changed to: " + response.body().getUsername());
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onResponse: Username wasnt returned");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileInfoResponse> call, Throwable t) {
                        profileInfoResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: Username change failed");
                    }
                });
    }

    public void changeEmail(String email, String token){
        profileService.changeEmail(email, token)
                .enqueue(new Callback<ProfileInfoResponse>() {
                    @Override
                    public void onResponse(Call<ProfileInfoResponse> call, Response<ProfileInfoResponse> response) {
                        profileInfoResponseMutableLiveData.postValue(response.body());
                        Log.d(TAG, "onResponse: Email changed to: " + response.body().getEmail());
                    }

                    @Override
                    public void onFailure(Call<ProfileInfoResponse> call, Throwable t) {
                        profileInfoResponseMutableLiveData.postValue(null);
                        try {
                            Log.d(TAG, "onFailure: Email change failed");
                        }
                        catch(Exception e) {
                            Log.d(TAG, "onFailure: Email wasnt returned");
                        }
                    }
                });
    }

    public void changePassword(String password, String token){
        profileService.changePassword(password, token)
                .enqueue(new Callback<ProfileInfoResponse>() {
                    @Override
                    public void onResponse(Call<ProfileInfoResponse> call, Response<ProfileInfoResponse> response) {
                        profileInfoResponseMutableLiveData.postValue(response.body());
                        Log.d(TAG, "onResponse: Password changed successfully");
                    }

                    @Override
                    public void onFailure(Call<ProfileInfoResponse> call, Throwable t) {
                        profileInfoResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onFailure: Password change failed");
                    }
                });
    }

    public LiveData<ProfileInfoResponse> getProfileInfoResponseMutableLiveData() {
        return profileInfoResponseMutableLiveData;
    }
}
