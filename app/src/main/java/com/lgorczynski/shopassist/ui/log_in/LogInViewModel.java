package com.lgorczynski.shopassist.ui.log_in;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogInViewModel extends ViewModel{

    private LoginRepository loginRepository;
    private LiveData<LoginResponse> loginResponseLiveData;
    private LiveData<AccountResponse> accountResponseLiveData;

    public LogInViewModel(){
        loginRepository = new LoginRepository();
        loginResponseLiveData = loginRepository.getLoginResponseLiveData();
        accountResponseLiveData = loginRepository.getAccountResponseLiveData();
    }

    public void login(String email, String password){
        loginRepository.loginUser(email, password);
    }

    public void getAccountDetails(int userID, String token){
        loginRepository.getAccountDetails(userID, token);
    }

    public void patchAccountDetails(int userID, String deviceRegistrationToken, String token){
        loginRepository.patchAccountDetails(userID, deviceRegistrationToken, token);
    }

    public LiveData<LoginResponse> getLoginResponseLiveData(){
        return loginResponseLiveData;
    }

    public LiveData<AccountResponse> getAccountResponseLiveData() {
        return accountResponseLiveData;
    }
}
