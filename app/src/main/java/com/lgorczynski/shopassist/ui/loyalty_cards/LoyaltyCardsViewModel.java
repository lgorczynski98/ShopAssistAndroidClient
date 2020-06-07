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

    public void deleteLoyaltyCard(String userID, String token){
        loyaltyCardsRepository.deleteLoyaltyCard(userID, token);
    }

    public LiveData<List<LoyaltyCard>> getLoyaltyCardsResponseLiveData(){
        return loyaltyCardsResponseLiveData;
    }

//    public LoyaltyCardsViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is loyalty cards fragment");
//
//        List<LoyaltyCard> exampleLoyaltyCards = new ArrayList<>();
//        exampleLoyaltyCards.add(new LoyaltyCard("Biedronka", "https://galeriamlociny.pl/app/uploads/wayfinder/url_logo/3039.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Lidl", "https://pliki.portalspozywczy.pl/i/10/69/80/106980_r0_940.jpg", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Kaufland", "https://www.kaufland.pl/etc.clientlibs/kaufland/clientlibs/clientlib-site/resources/frontend/img/opengraph_image_default-581151e556.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Stokrotka", "https://www.lublinplaza.pl/_cache/shops/510-255/fill/stokrotka.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Biedronka", "https://galeriamlociny.pl/app/uploads/wayfinder/url_logo/3039.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Lidl", "https://pliki.portalspozywczy.pl/i/10/69/80/106980_r0_940.jpg", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Kaufland", "https://www.kaufland.pl/etc.clientlibs/kaufland/clientlibs/clientlib-site/resources/frontend/img/opengraph_image_default-581151e556.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Stokrotka", "https://www.lublinplaza.pl/_cache/shops/510-255/fill/stokrotka.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Biedronka", "https://galeriamlociny.pl/app/uploads/wayfinder/url_logo/3039.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Lidl", "https://pliki.portalspozywczy.pl/i/10/69/80/106980_r0_940.jpg", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Kaufland", "https://www.kaufland.pl/etc.clientlibs/kaufland/clientlibs/clientlib-site/resources/frontend/img/opengraph_image_default-581151e556.png", "UPC_A", "72527273070"));
//        exampleLoyaltyCards.add(new LoyaltyCard("Stokrotka", "https://www.lublinplaza.pl/_cache/shops/510-255/fill/stokrotka.png", "UPC_A", "72527273070"));
//
//        mLoyaltyCards = new MutableLiveData<>();
//        mLoyaltyCards.setValue(exampleLoyaltyCards);
//    }


}