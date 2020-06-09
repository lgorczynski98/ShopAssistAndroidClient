package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lgorczynski.shopassist.ui.log_in.CredentialsSingleton;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                        Log.d(TAG, "onGetResponse: successfull");
                    }

                    @Override
                    public void onFailure(Call<List<LoyaltyCard>> call, Throwable t) {
                        loyaltyCardsResponseMutableLiveData.postValue(null);
                        Log.d(TAG, "onGetFailure: " + t.getMessage());
                    }
                });
    }

    public void postLoyaltyCard(String title, String barcode_format, String barcode_content, File image, String token){
        RequestBody requestFile = RequestBody.create(image, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        RequestBody requestTitle = RequestBody.create(title, MediaType.parse("multipart/form-data"));
        RequestBody requestBarcodeFormat = RequestBody.create(barcode_format, MediaType.parse("multipart/form-data"));
        RequestBody requestBarcodeContent = RequestBody.create(barcode_content, MediaType.parse("multipart/form-data"));

        loyaltyCardsService.postLoyaltyCard(requestTitle, requestBarcodeFormat, requestBarcodeContent, imageBody, token)
                .enqueue(new Callback<LoyaltyCard>() {
                    @Override
                    public void onResponse(Call<LoyaltyCard> call, Response<LoyaltyCard> response) {
                        Log.d(TAG, "onPostResponse: Loyalty card added succesfully");
                    }

                    @Override
                    public void onFailure(Call<LoyaltyCard> call, Throwable t) {
                        Log.d(TAG, "onPostFailure: Error on adding loyaltycard");
                    }
                });
    }

    public void postLoyaltyCard(String title, String barcode_format, String barcode_content, String token){
        loyaltyCardsService.postLoyaltyCards(title, barcode_format, barcode_content, token)
                .enqueue(new Callback<LoyaltyCard>() {
                    @Override
                    public void onResponse(Call<LoyaltyCard> call, Response<LoyaltyCard> response) {
                        Log.d(TAG, "onPostResponse: Loyalty card added succesfully");
                    }

                    @Override
                    public void onFailure(Call<LoyaltyCard> call, Throwable t) {
                        Log.d(TAG, "onPostFailure: Error on adding loyaltycard");
                    }
                });
    }

    public void deleteLoyaltyCard(int cardID, String token){
        loyaltyCardsService.deleteLoyaltyCard(cardID, token)
                .enqueue(new Callback<LoyaltyCard>() {
                    @Override
                    public void onResponse(Call<LoyaltyCard> call, Response<LoyaltyCard> response) {
                        Log.d(TAG, "onDeleteResponse: Card deleted succesfully");
                    }

                    @Override
                    public void onFailure(Call<LoyaltyCard> call, Throwable t) {
                        Log.d(TAG, "onDeleteFailure: Error");
                    }
                });
    }

    public void patchLoyaltyCard(int cardID, String title, String token){
        loyaltyCardsService.patchLoyaltyCard(cardID, title, token)
                .enqueue(new Callback<LoyaltyCard>() {
                    @Override
                    public void onResponse(Call<LoyaltyCard> call, Response<LoyaltyCard> response) {
                        Log.d(TAG, "onPatchResponse: Updated succesfully");
                    }

                    @Override
                    public void onFailure(Call<LoyaltyCard> call, Throwable t) {
                        Log.d(TAG, "onPatchFailure: Error on updating");
                    }
                });
    }

    public void patchLoyaltyCard(int cardID, File image, String token){
        RequestBody requestFile = RequestBody.create(image, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", image.getName(), requestFile);

        loyaltyCardsService.patchLoyaltyCard(cardID, imageBody, token)
                .enqueue(new Callback<LoyaltyCard>() {
                    @Override
                    public void onResponse(Call<LoyaltyCard> call, Response<LoyaltyCard> response) {
                        Log.d(TAG, "onPatchResponse: Updated succesfully");
                    }

                    @Override
                    public void onFailure(Call<LoyaltyCard> call, Throwable t) {
                        Log.d(TAG, "onPatchFailure: Error on updating");
                    }
                });
    }

    public void patchLoyaltyCard(int cardID, String title, File image, String token){
        RequestBody requestFile = RequestBody.create(image, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        RequestBody requestTitle = RequestBody.create(title, MediaType.parse("multipart/form-data"));

        loyaltyCardsService.patchLoyaltyCard(cardID, requestTitle, imageBody, token)
                .enqueue(new Callback<LoyaltyCard>() {
                    @Override
                    public void onResponse(Call<LoyaltyCard> call, Response<LoyaltyCard> response) {
                        Log.d(TAG, "onPatchResponse: Updated succesfully");
                    }

                    @Override
                    public void onFailure(Call<LoyaltyCard> call, Throwable t) {
                        Log.d(TAG, "onPatchFailure: Error on updating");
                    }
                });
    }

    public LiveData<List<LoyaltyCard>> getLoyaltyCardsResponseMutableLiveData() {
        return loyaltyCardsResponseMutableLiveData;
    }
}
