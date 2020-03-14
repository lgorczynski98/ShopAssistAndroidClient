package com.lgorczynski.shopassist.ui.loyalty_cards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoyaltyCardsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoyaltyCardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is loyalty cards fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}