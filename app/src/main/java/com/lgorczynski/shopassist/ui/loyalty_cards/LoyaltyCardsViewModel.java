package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyCardsViewModel extends ViewModel {

    private static final String TAG = "LoyaltyCardsViewModel";

    private MutableLiveData<String> mText;

    private MutableLiveData<List<LoyaltyCard>> mLoyaltyCards;


    public LoyaltyCardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is loyalty cards fragment");

        List<LoyaltyCard> exampleLoyaltyCards = new ArrayList<>();
        exampleLoyaltyCards.add(new LoyaltyCard("Biedronka", "https://galeriamlociny.pl/app/uploads/wayfinder/url_logo/3039.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Lidl", "https://pliki.portalspozywczy.pl/i/10/69/80/106980_r0_940.jpg", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Kaufland", "https://www.kaufland.pl/etc.clientlibs/kaufland/clientlibs/clientlib-site/resources/frontend/img/opengraph_image_default-581151e556.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Stokrotka", "https://www.lublinplaza.pl/_cache/shops/510-255/fill/stokrotka.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Biedronka", "https://galeriamlociny.pl/app/uploads/wayfinder/url_logo/3039.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Lidl", "https://pliki.portalspozywczy.pl/i/10/69/80/106980_r0_940.jpg", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Kaufland", "https://www.kaufland.pl/etc.clientlibs/kaufland/clientlibs/clientlib-site/resources/frontend/img/opengraph_image_default-581151e556.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Stokrotka", "https://www.lublinplaza.pl/_cache/shops/510-255/fill/stokrotka.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Biedronka", "https://galeriamlociny.pl/app/uploads/wayfinder/url_logo/3039.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Lidl", "https://pliki.portalspozywczy.pl/i/10/69/80/106980_r0_940.jpg", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Kaufland", "https://www.kaufland.pl/etc.clientlibs/kaufland/clientlibs/clientlib-site/resources/frontend/img/opengraph_image_default-581151e556.png", "UPC_A", "72527273070"));
        exampleLoyaltyCards.add(new LoyaltyCard("Stokrotka", "https://www.lublinplaza.pl/_cache/shops/510-255/fill/stokrotka.png", "UPC_A", "72527273070"));

        mLoyaltyCards = new MutableLiveData<>();
        mLoyaltyCards.setValue(exampleLoyaltyCards);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<LoyaltyCard>> getLoyaltyCards() {
        return mLoyaltyCards;
    }
}