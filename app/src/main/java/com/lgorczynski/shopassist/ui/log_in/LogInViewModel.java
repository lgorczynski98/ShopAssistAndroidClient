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

    public LogInViewModel(){
        loginRepository = new LoginRepository();
        loginResponseLiveData = loginRepository.getLoginResponseLiveData();
    }

    public void login(String email, String password){
        loginRepository.loginUser(email, password);
    }

    public LiveData<LoginResponse> getLoginResponseLiveData(){
        return loginResponseLiveData;
    }
}
