package com.lgorczynski.shopassist.ui.log_in;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lgorczynski.shopassist.R;

public class LogInFragment extends Fragment implements View.OnClickListener {

    private LogInViewModel logInViewModel;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logInViewModel =
                ViewModelProviders.of(this).get(LogInViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);
//        final TextView textView = root.findViewById(R.id.text_log_in);
//        logInViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        final Button signInButton = view.findViewById(R.id.sign_in_button);
        final Button registerButton = view.findViewById(R.id.register_button);
        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_in_button:{
                navController.navigate(R.id.action_logInFragment_to_navigation_home);
                break;
            }
            case R.id.register_button:{
                navController.navigate(R.id.action_logInFragment_to_registerFragment);
            }
        }
    }
}
