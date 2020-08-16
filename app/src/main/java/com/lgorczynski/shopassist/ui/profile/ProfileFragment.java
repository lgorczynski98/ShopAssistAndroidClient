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
import com.lgorczynski.shopassist.ui.receipts.Receipt;
import com.lgorczynski.shopassist.ui.receipts.ReceiptsViewModel;

import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private NavController navController;
    private LinearLayout settingsLayout;
    private LinearLayout downloadLocalCopyLayout;
    private ReceiptsViewModel receiptsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        receiptsViewModel =
                ViewModelProviders.of(this).get(ReceiptsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final Button logOutButton = root.findViewById(R.id.profile_log_out_button);
        final Button settingButton = root.findViewById(R.id.profile_settings_button);
        final Button licenceButton = root.findViewById(R.id.profile_licence_button);
        final Button aboutButton = root.findViewById(R.id.profile_about_us_button);
        settingsLayout = root.findViewById(R.id.profile_settings_layout);
        downloadLocalCopyLayout = root.findViewById(R.id.profile_settings_layout_download_copy);
        downloadLocalCopyLayout.setOnClickListener(this);
        logOutButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        licenceButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);

        receiptsViewModel.getReceipts(CredentialsSingleton.getInstance().getToken());

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
                editor.apply();
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
            }
        }
    }
}