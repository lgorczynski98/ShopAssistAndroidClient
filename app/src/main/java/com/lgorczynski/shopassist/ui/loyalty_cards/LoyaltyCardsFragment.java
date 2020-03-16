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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.LoyaltyCard;
import com.lgorczynski.shopassist.R;

import java.util.List;

public class LoyaltyCardsFragment extends Fragment {

    private LoyaltyCardsViewModel loyaltyCardsViewModel;

    private RecyclerView recyclerView;
    private LoyaltyCardRecyclerViewAdapter adapter;

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

        recyclerView = root.findViewById(R.id.loyalty_cards_recycler_view);
        loyaltyCardsViewModel.getLoyaltyCards().observe(this, new Observer<List<LoyaltyCard>>() {
            @Override
            public void onChanged(List<LoyaltyCard> loyaltyCards) {
                adapter = new LoyaltyCardRecyclerViewAdapter(getContext(), loyaltyCards);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }
}