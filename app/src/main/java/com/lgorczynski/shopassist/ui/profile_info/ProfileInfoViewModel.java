package com.lgorczynski.shopassist.ui.profile_info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile info fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
