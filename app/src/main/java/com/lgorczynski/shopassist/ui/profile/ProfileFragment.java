package com.lgorczynski.shopassist.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.ImageFileCreator;
import com.lgorczynski.shopassist.ui.log_in.LogInViewModel;
import com.lgorczynski.shopassist.ui.loyalty_cards.ShareDialog;
import com.lgorczynski.shopassist.ui.profile.dialogs.EmailChangeDialog;
import com.lgorczynski.shopassist.ui.profile.dialogs.UsernameChangeDialog;
import com.lgorczynski.shopassist.ui.receipts.Receipt;
import com.lgorczynski.shopassist.ui.receipts.ReceiptsViewModel;

import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener, UsernameChangeDialog.UsernameChangeListener, EmailChangeDialog.EmailChangeListener {

    private NavController navController;

    private ProfileViewModel profileViewModel;

    private LinearLayout settingsLayout;
    private LinearLayout downloadLocalCopyLayout;
    private LinearLayout usernameChangeLayout;
    private LinearLayout emailChangeLayout;
    private LinearLayout passwordChangeLayout;
    private ReceiptsViewModel receiptsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        receiptsViewModel =
                ViewModelProviders.of(this).get(ReceiptsViewModel.class);

        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final Button logOutButton = root.findViewById(R.id.profile_log_out_button);
        final Button settingButton = root.findViewById(R.id.profile_settings_button);
        final Button licenceButton = root.findViewById(R.id.profile_licence_button);
        final Button aboutButton = root.findViewById(R.id.profile_about_us_button);
        settingsLayout = root.findViewById(R.id.profile_settings_layout);
        downloadLocalCopyLayout = root.findViewById(R.id.profile_settings_layout_download_copy);
        downloadLocalCopyLayout.setOnClickListener(this);
        usernameChangeLayout = root.findViewById(R.id.profile_settings_layout_change_username);
        usernameChangeLayout.setOnClickListener(this);
        emailChangeLayout = root.findViewById(R.id.profile_settings_layout_change_email);
        emailChangeLayout.setOnClickListener(this);
        passwordChangeLayout = root.findViewById(R.id.profile_settings_layout_change_password);
        passwordChangeLayout.setOnClickListener(this);
        logOutButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        licenceButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

        receiptsViewModel.getReceipts(CredentialsSingleton.getInstance().getToken());

        profileViewModel.getProfileInfoResponseLiveData().observe(this, profileInfoResponse -> {
            if(profileInfoResponse == null)
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            else{
                if(profileInfoResponse.getUsername() != null) CredentialsSingleton.getInstance().setUsername(profileInfoResponse.getUsername());
                if(profileInfoResponse.getEmail() != null) CredentialsSingleton.getInstance().setEmail(profileInfoResponse.getEmail());
                if(profileInfoResponse.getDetail() != null) Toast.makeText(getContext(), profileInfoResponse.getDetail(), Toast.LENGTH_SHORT).show();
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
        switch (view.getId()){
            case R.id.profile_log_out_button:{
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_token), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.token), null);
                editor.putInt("user_id", -1);
                editor.apply();

                LogInViewModel logInViewModel = ViewModelProviders.of(this).get(LogInViewModel.class);
                logInViewModel.patchAccountDetails(CredentialsSingleton.getInstance().getUserID(), "", CredentialsSingleton.getInstance().getToken());

                navController.navigate(R.id.action_navigation_profile_to_logInFragment);
                break;
            }
            case R.id.profile_settings_button:{
                if(settingsLayout.getVisibility() == View.VISIBLE)
                    settingsLayout.setVisibility(View.GONE);
                else
                    settingsLayout.setVisibility(View.VISIBLE);
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
            case R.id.profile_settings_layout_download_copy:{
                List<Receipt> receipts = receiptsViewModel.getReceiptsResponseLiveData().getValue();
                ImageFileCreator imageFileCreator = new ImageFileCreator(getContext());
                imageFileCreator.createReceiptsImageFiles(receipts);
                break;
            }
            case R.id.profile_settings_layout_change_username:{
                UsernameChangeDialog usernameChangeDialog = new UsernameChangeDialog(this);
                usernameChangeDialog.show(getActivity().getSupportFragmentManager(), "username_change_dialog");
                break;
            }
            case R.id.profile_settings_layout_change_email:{
                EmailChangeDialog emailChangeDialog = new EmailChangeDialog(this);
                emailChangeDialog.show(getActivity().getSupportFragmentManager(), "email_change_dialog");
                break;
            }
            case R.id.profile_settings_layout_change_password:{
                Toast.makeText(getContext(), "Change password", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onUsernameChange(String username) {
        profileViewModel.changeUsername(username, CredentialsSingleton.getInstance().getToken());
    }

    @Override
    public void onEmailChange(String email) {
        profileViewModel.changeEmail(email, CredentialsSingleton.getInstance().getToken());
    }
}