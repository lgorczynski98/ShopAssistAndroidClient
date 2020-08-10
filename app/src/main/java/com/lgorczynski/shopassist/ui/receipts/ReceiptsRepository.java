package com.lgorczynski.shopassist.ui.receipts;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.lgorczynski.shopassist.ui.CredentialsSingleton;

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

import static androidx.constraintlayout.widget.Constraints.TAG;

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

    public void postReceipt(String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, File image, File thumbnail, String token){
        RequestBody requestTitle = RequestBody.create(title, MediaType.parse("multipart/form-data"));
        RequestBody requestShopName = RequestBody.create(shopName, MediaType.parse("multipart/form-data"));
        RequestBody requestPurchaseDate = RequestBody.create(purchaseDate, MediaType.parse("multipart/form-data"));
        RequestBody requestPurchaseCost = RequestBody.create(purchaseCost, MediaType.parse("multipart/form-data"));
        RequestBody requestWeeksToReturn = RequestBody.create(String.valueOf(weeksToReturn), MediaType.parse("multipart/form-data"));
        RequestBody requestMonthsOfWarranty = RequestBody.create(String.valueOf(monthsOfWarranty), MediaType.parse("multipart/form-data"));

        RequestBody requestImage = RequestBody.create(image, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", image.getName(), requestImage);

        RequestBody requestThumbnail = RequestBody.create(thumbnail, MediaType.parse("multipart/form-data"));
        MultipartBody.Part thumbnailBody = MultipartBody.Part.createFormData("thumbnail", thumbnail.getName(), requestThumbnail);
        
        receiptsService.postReceipt(requestTitle, requestShopName, requestPurchaseDate, requestPurchaseCost, requestWeeksToReturn, requestMonthsOfWarranty, imageBody, thumbnailBody, token)
                .enqueue(new Callback<Receipt>() {
                    @Override
                    public void onResponse(Call<Receipt> call, Response<Receipt> response) {
                        Log.d(TAG, "onPostResponse: Receipt added correctly");
                        boolean deleted = thumbnail.delete();
                        Log.d(TAG, "onResponce: Temp thumbnail file deleted: " + deleted);
                        deleted = image.delete();
                        Log.d(TAG, "onResponse: Receipt image file deleted: " + deleted);
                    }

                    @Override
                    public void onFailure(Call<Receipt> call, Throwable t) {
                        Log.d(TAG, "onPostFailure: Error on adding receipt");
                        boolean deleted = thumbnail.delete();
                        Log.d(TAG, "onResponce: Temp thumbnail file deleted: " + deleted);
                        deleted = image.delete();
                        Log.d(TAG, "onResponse: Receipt image file deleted: " + deleted);
                    }
                });
    }

    public void postReceipt(String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, File image, String token){
        RequestBody requestTitle = RequestBody.create(title, MediaType.parse("multipart/form-data"));
        RequestBody requestShopName = RequestBody.create(shopName, MediaType.parse("multipart/form-data"));
        RequestBody requestPurchaseDate = RequestBody.create(purchaseDate, MediaType.parse("multipart/form-data"));
        RequestBody requestPurchaseCost = RequestBody.create(purchaseCost, MediaType.parse("multipart/form-data"));
        RequestBody requestWeeksToReturn = RequestBody.create(String.valueOf(weeksToReturn), MediaType.parse("multipart/form-data"));
        RequestBody requestMonthsOfWarranty = RequestBody.create(String.valueOf(monthsOfWarranty), MediaType.parse("multipart/form-data"));

        RequestBody requestImage = RequestBody.create(image, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", image.getName(), requestImage);

        receiptsService.postReceipt(requestTitle, requestShopName, requestPurchaseDate, requestPurchaseCost, requestWeeksToReturn, requestMonthsOfWarranty, imageBody, token)
                .enqueue(new Callback<Receipt>() {
                    @Override
                    public void onResponse(Call<Receipt> call, Response<Receipt> response) {
                        Log.d(TAG, "onPostResponse: Receipt added correctly");
                        boolean deleted = image.delete();
                        Log.d(TAG, "onResponse: Receipt image file deleted: " + deleted);
                    }

                    @Override
                    public void onFailure(Call<Receipt> call, Throwable t) {
                        Log.d(TAG, "onPostFailure: Error on adding receipt");
                        boolean deleted = image.delete();
                        Log.d(TAG, "onResponse: Receipt image file deleted: " + deleted);
                    }
                });
    }

    public void deleteReceipt(int receiptID, String token){
        receiptsService.deleteReceipt(receiptID, token)
                .enqueue(new Callback<Receipt>() {
                    @Override
                    public void onResponse(Call<Receipt> call, Response<Receipt> response) {
                        Log.d(TAG, "onDeleteResponse: Receipt deleted succesfully");
                    }

                    @Override
                    public void onFailure(Call<Receipt> call, Throwable t) {
                        Log.d(TAG, "onDeleteFailure: Error");
                    }
                });
    }

    public void patchReceipt(int receiptID, String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, String token){
        receiptsService.patchReceipt(receiptID, title, shopName, purchaseDate, purchaseCost, weeksToReturn, monthsOfWarranty, token)
                .enqueue(new Callback<Receipt>() {
                    @Override
                    public void onResponse(Call<Receipt> call, Response<Receipt> response) {
                        Log.d(TAG, "onResponse: Updated succesfully");
                    }

                    @Override
                    public void onFailure(Call<Receipt> call, Throwable t) {
                        Log.d(TAG, "onFailure: Error on updating");
                    }
                });
    }

    public void patchReceipt(int receiptID, File thumbnail, String token){
        RequestBody requestFile = RequestBody.create(thumbnail, MediaType.parse("multipart/form-data"));
        MultipartBody.Part thumbnailBody = MultipartBody.Part.createFormData("thumbnail", thumbnail.getName(), requestFile);

        receiptsService.patchReceipt(receiptID, thumbnailBody, token)
                .enqueue(new Callback<Receipt>() {
                    @Override
                    public void onResponse(Call<Receipt> call, Response<Receipt> response) {
                        Log.d(TAG, "onResponse: Updated succesfully");
                        boolean deleted = thumbnail.delete();
                        Log.d(TAG, "onResponse: Temp thumbnail deleted: " + deleted);
                    }

                    @Override
                    public void onFailure(Call<Receipt> call, Throwable t) {
                        Log.d(TAG, "onFailure: Error on updating");
                        boolean deleted = thumbnail.delete();
                        Log.d(TAG, "onFailure: Temp thumbnail deleted: " + deleted);
                    }
                });
    }

    public void patchReceipt(int receiptID, String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, File thumbnail, String token){
        RequestBody requestFile = RequestBody.create(thumbnail, MediaType.parse("multipart/form-data"));
        MultipartBody.Part thumbnailBody = MultipartBody.Part.createFormData("thumbnail", thumbnail.getName(), requestFile);

        RequestBody requestTitle = RequestBody.create(title, MediaType.parse("multipart/form-data"));
        RequestBody requestShopName = RequestBody.create(shopName, MediaType.parse("multipart/form-data"));
        RequestBody requestPurchaseDate = RequestBody.create(purchaseDate, MediaType.parse("multipart/form-data"));
        RequestBody requestPurchaseCost = RequestBody.create(purchaseCost, MediaType.parse("multipart/form-data"));
        RequestBody requestWeeksToReturn = RequestBody.create(String.valueOf(weeksToReturn), MediaType.parse("multipart/form-data"));
        RequestBody requestMonthsOfWarranty = RequestBody.create(String.valueOf(monthsOfWarranty), MediaType.parse("multipart/form-data"));

        receiptsService.patchReceipt(receiptID, requestTitle, requestShopName, requestPurchaseDate, requestPurchaseCost, requestWeeksToReturn, requestMonthsOfWarranty, thumbnailBody, token)
                .enqueue(new Callback<Receipt>() {
                    @Override
                    public void onResponse(Call<Receipt> call, Response<Receipt> response) {
                        Log.d(TAG, "onResponse: Updated succesfully");
                        boolean deleted = thumbnail.delete();
                        Log.d(TAG, "onResponse: Temp thumbnail deleted: " + deleted);
                    }

                    @Override
                    public void onFailure(Call<Receipt> call, Throwable t) {
                        Log.d(TAG, "onFailure: Error on updating");
                        boolean deleted = thumbnail.delete();
                        Log.d(TAG, "onFailure: Temp thumbnail deleted: " + deleted);
                    }
                });
    }

    public MutableLiveData<List<Receipt>> getReceiptsResponseMutableLiveData() {
        return receiptsResponseMutableLiveData;
    }
}
