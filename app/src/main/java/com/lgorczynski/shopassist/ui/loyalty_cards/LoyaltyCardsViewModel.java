package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lgorczynski.shopassist.ui.log_in.CredentialsSingleton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoyaltyCardsViewModel extends ViewModel {

    private static final String TAG = "LoyaltyCardsViewModel";
    private LoyaltyCardsRepository loyaltyCardsRepository;
    private LiveData<List<LoyaltyCard>> loyaltyCardsResponseLiveData;

    public LoyaltyCardsViewModel(){
        loyaltyCardsRepository = new LoyaltyCardsRepository();
        loyaltyCardsResponseLiveData = loyaltyCardsRepository.getLoyaltyCardsResponseMutableLiveData();
    }

    public void getLoyaltyCards(String token){
        loyaltyCardsRepository.getLoyaltyCards(token);
    }

    public void postLoyaltyCard(String title, String barcode_format, String barcode_content, File image, String token){
        loyaltyCardsRepository.postLoyaltyCard(title, barcode_format, barcode_content, image, token);
    }

    public void postLoyaltyCard(String title, String barcode_format, String barcode_content, String token){
        loyaltyCardsRepository.postLoyaltyCard(title, barcode_format, barcode_content, token);
    }

    public void deleteLoyaltyCard(int cardID, String token){
        loyaltyCardsRepository.deleteLoyaltyCard(cardID, token);
    }

    public LiveData<List<LoyaltyCard>> getLoyaltyCardsResponseLiveData(){
        return loyaltyCardsResponseLiveData;
    }

    public void patchLoyaltyCard(int cardID, String title, String token){
        loyaltyCardsRepository.patchLoyaltyCard(cardID, title, token);
    }

    public void patchLoyaltyCard(int cardID, File image, String token){
        loyaltyCardsRepository.patchLoyaltyCard(cardID, image, token);
    }

    public void patchLoyaltyCard(int cardID, String title, File image, String token){
        loyaltyCardsRepository.patchLoyaltyCard(cardID, title, image, token);
    }
}