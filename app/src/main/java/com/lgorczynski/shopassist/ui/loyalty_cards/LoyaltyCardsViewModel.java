package com.lgorczynski.shopassist.ui.loyalty_cards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
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

    public void deleteLoyaltyCard(int cardID, String token, BottomSheetDialog bottomSheetDialog){
        loyaltyCardsRepository.deleteLoyaltyCard(cardID, token, bottomSheetDialog);
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

    public void setRepositoryNavController(NavController navController) {
        loyaltyCardsRepository.setNavController(navController);
    }
}