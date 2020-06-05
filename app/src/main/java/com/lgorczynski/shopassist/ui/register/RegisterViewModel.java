package com.lgorczynski.shopassist.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lgorczynski.shopassist.ui.log_in.LoginResponse;

public class RegisterViewModel extends ViewModel {

    private RegisterRepository registerRepository;
    private LiveData<LoginResponse> loginResponseLiveData;

    public RegisterViewModel() {
        registerRepository = new RegisterRepository();
        loginResponseLiveData = registerRepository.getLoginRepsonseLiveData();
    }

    public void register(String email, String username, String password){
        registerRepository.registerUser(email, username, password);
    }

    public LiveData<LoginResponse> getLoginResponseLiveData(){
        return loginResponseLiveData;
    }
}
