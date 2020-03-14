package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.lgorczynski.shopassist.R;

public class LoyaltyCardsFragment extends Fragment {

    private LoyaltyCardsViewModel loyaltyCardsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loyaltyCardsViewModel =
                ViewModelProviders.of(this).get(LoyaltyCardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loyalty_cards, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        loyaltyCardsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}