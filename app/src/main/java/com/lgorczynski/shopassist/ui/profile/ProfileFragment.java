package com.lgorczynski.shopassist.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lgorczynski.shopassist.R;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private ProfileViewModel profileViewModel;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        final Button logOutButton = view.findViewById(R.id.profile_log_out_button);
        final Button settingButton = view.findViewById(R.id.profile_setting_button);
        final Button profileInfoButton = view.findViewById(R.id.profile_change_profile_info_button);
        final Button licenceButton = view.findViewById(R.id.profile_licence_button);
        final Button aboutButton = view.findViewById(R.id.profile_about_us_button);
        logOutButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        profileInfoButton.setOnClickListener(this);
        licenceButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_log_out_button:{
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_token), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.token), null);
                editor.apply();
                navController.navigate(R.id.action_navigation_profile_to_logInFragment);
                break;
            }
            case R.id.profile_setting_button:{
                navController.navigate(R.id.action_navigation_profile_to_settingsFragment);
                break;
            }
            case R.id.profile_change_profile_info_button:{
                navController.navigate(R.id.action_navigation_profile_to_profileInfoFragment);
                break;
            }
            case R.id.profile_licence_button:{
                navController.navigate(R.id.action_navigation_profile_to_licenceFragment);
                break;
            }
            case R.id.profile_about_us_button:{
                navController.navigate(R.id.action_navigation_profile_to_aboutFragment);
                break;
            }
        }
    }
}