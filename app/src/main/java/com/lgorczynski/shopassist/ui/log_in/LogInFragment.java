package com.lgorczynski.shopassist.ui.log_in;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;

public class LogInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LogInFragment";

    private LogInViewModel logInViewModel;

    private NavController navController;

    private TextView emailTextView;
    private TextView passwordTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logInViewModel =
                ViewModelProviders.of(this).get(LogInViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);

        final Button signInButton = root.findViewById(R.id.sign_in_button);
        final Button registerButton = root.findViewById(R.id.register_button);
        emailTextView = root.findViewById(R.id.log_in_email_input);
        passwordTextView = root.findViewById(R.id.log_in_password_input);
        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        logInViewModel.getLoginResponseLiveData().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                CredentialsSingleton.getInstance().setToken("Token " + loginResponse.getToken());
                CredentialsSingleton.getInstance().setUserID(loginResponse.getUserID());
                Log.d(TAG, "onChanged: User\'s token: " + loginResponse.getToken());
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_token), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.token), CredentialsSingleton.getInstance().getToken());
                editor.putInt("user_id", CredentialsSingleton.getInstance().getUserID());
                editor.apply();
                getAccountDetails();
                navController.navigate(R.id.action_logInFragment_to_navigation_home);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_token), Context.MODE_PRIVATE);
        String sharedPrefToken = sharedPreferences.getString(getString(R.string.token), null);
        int sharedPrefUserID = sharedPreferences.getInt("user_id", -1);
        if(sharedPrefToken != null && sharedPrefUserID != -1){
            CredentialsSingleton.getInstance().setToken(sharedPrefToken);
            CredentialsSingleton.getInstance().setUserID(sharedPrefUserID);
            getAccountDetails();
            navController.navigate(R.id.action_logInFragment_to_navigation_home);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_in_button:{
//                navController.navigate(R.id.action_logInFragment_to_navigation_home);
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                logInViewModel.login(email, password);
                break;
            }
            case R.id.register_button:{
                navController.navigate(R.id.action_logInFragment_to_registerFragment);
            }
        }
    }

    private void getAccountDetails(){
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
                        logInViewModel.getAccountDetails(CredentialsSingleton.getInstance().getUserID(), CredentialsSingleton.getInstance().getToken());
                    }
                });

    }
}
