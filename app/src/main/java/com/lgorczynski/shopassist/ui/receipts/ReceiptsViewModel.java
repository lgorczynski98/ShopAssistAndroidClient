package com.lgorczynski.shopassist.ui.receipts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReceiptsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReceiptsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is receipt fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}