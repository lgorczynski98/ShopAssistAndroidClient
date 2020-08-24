package com.lgorczynski.shopassist.ui.log_in;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;

public class LogInViewModel extends ViewModel{

    private static final String TAG = "LogInViewModel";

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

    private void getAccountDetails(int userID, String token){
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

    public void getAccountDetails(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, "onComplete: Current device registration token: " + token);
                        CredentialsSingleton.getInstance().setDeviceRegistrationToken(token);
                        getAccountDetails(CredentialsSingleton.getInstance().getUserID(), CredentialsSingleton.getInstance().getToken());
                    }
                });

    }
}
