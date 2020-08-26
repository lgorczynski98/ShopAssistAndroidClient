package com.lgorczynski.shopassist.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private static final String TAG = "ProfileViewModel";

    private ProfileRepository profileRepository;
    private LiveData<ProfileInfoResponse> profileInfoResponseLiveData;

    public ProfileViewModel(){
        profileRepository = new ProfileRepository();
        profileInfoResponseLiveData = profileRepository.getProfileInfoResponseMutableLiveData();
    }

    public void changeUsername(String username, String token){
        profileRepository.changeUsername(username, token);
    }

    public void changeEmail(String email, String token){
        profileRepository.changeEmail(email, token);
    }

    public void changePassword(String password, String token){
        profileRepository.changePassword(password, token);
    }

    public LiveData<ProfileInfoResponse> getProfileInfoResponseLiveData() {
        return profileInfoResponseLiveData;
    }
}
