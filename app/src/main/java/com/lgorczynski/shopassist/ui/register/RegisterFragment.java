package com.lgorczynski.shopassist.ui.register;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.log_in.LogInViewModel;
import com.lgorczynski.shopassist.ui.log_in.LoginResponse;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private RegisterViewModel registerViewModel;
    private LogInViewModel logInViewModel;

    private NavController navController;

    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;
    private EditText password2Text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                ViewModelProviders.of(this).get(RegisterViewModel.class);
        logInViewModel =
                ViewModelProviders.of(this).get(LogInViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        emailText = root.findViewById(R.id.register_email_input);
        usernameText = root.findViewById(R.id.register_username_input);
        passwordText = root.findViewById(R.id.register_password_input);
        password2Text = root.findViewById(R.id.register_password2_input);

        final Button signUp = root.findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(this);

        registerViewModel.getRegisterResponseLiveData().observe(this, registerResponse -> {
            if(registerResponse == null){
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                return;
            }
            if(registerResponse.getEmailDetail() != null || registerResponse.getUsernameDetail() != null){
                Toast.makeText(getContext(), getRegisterResponseDetailMessage(registerResponse.getEmailDetail(), registerResponse.getUsernameDetail()), Toast.LENGTH_LONG).show();
                return;
            }
            CredentialsSingleton.getInstance().setToken("Token " + registerResponse.getToken());
            CredentialsSingleton.getInstance().setUserID(registerResponse.getUserID());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_token), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.token), CredentialsSingleton.getInstance().getToken());
            editor.putInt("user_id", CredentialsSingleton.getInstance().getUserID());
            editor.apply();
            logInViewModel.getAccountDetails();
            navController.navigate(R.id.action_registerFragment_to_navigation_loyalty_cards);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_up_button){
            String password = passwordText.getText().toString();
            String password2 = password2Text.getText().toString();
            if(!password.equals(password2)){
                Toast.makeText(getContext(), "Passwords must match", Toast.LENGTH_SHORT).show();
                return;
            }
            String email = emailText.getText().toString();
            String username = usernameText.getText().toString();
            registerViewModel.register(email, username, password);
        }
    }

    private String getRegisterResponseDetailMessage(String emailDetail, String usernameDetail){
        String message = "";
        if(emailDetail != null){
            message += emailDetail;
            if(usernameDetail != null)
                message += ("\n" + usernameDetail);
        }
        else if(usernameDetail != null)
            message += usernameDetail;
        return message;
    }

}
