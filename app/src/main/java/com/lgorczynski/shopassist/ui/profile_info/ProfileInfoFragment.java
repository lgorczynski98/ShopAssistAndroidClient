package com.lgorczynski.shopassist.ui.profile_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.loyalty_cards.LoyaltyCardsViewModel;

public class ProfileInfoFragment extends Fragment {

    private ProfileInfoViewModel profileInfoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileInfoViewModel =
                ViewModelProviders.of(this).get(ProfileInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile_info, container, false);
        final TextView textView = root.findViewById(R.id.text_profile_info);
        profileInfoViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
