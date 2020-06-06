package com.lgorczynski.shopassist.ui.register;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.log_in.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.log_in.LoginResponse;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private RegisterViewModel registerViewModel;

    private NavController navController;

    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;
    private EditText password2Text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                ViewModelProviders.of(this).get(RegisterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        emailText = root.findViewById(R.id.register_email_input);
        usernameText = root.findViewById(R.id.register_username_input);
        passwordText = root.findViewById(R.id.register_password_input);
        password2Text = root.findViewById(R.id.register_password2_input);

        final Button signUp = root.findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(this);

        registerViewModel.getLoginResponseLiveData().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Token");
                alert.setMessage(loginResponse.getToken());
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CredentialsSingleton.getInstance().setToken("Token " + loginResponse.getToken());
                        navController.navigate(R.id.action_registerFragment_to_navigation_loyalty_cards);
                    }
                });
                alert.create().show();
            }
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
                passwordsAlert();
                return;
            }
            String email = emailText.getText().toString();
            String username = usernameText.getText().toString();
            registerViewModel.register(email, username, password);
        }
    }

    private void passwordsAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Error");
        alert.setMessage("Passwords must match");
        alert.setCancelable(true);
        alert.create().show();
    }

}
