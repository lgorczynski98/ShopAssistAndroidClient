package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.lgorczynski.shopassist.R;
import com.squareup.picasso.Picasso;


public class LoyaltyCardEditFormFragment extends LoyaltyCardFormFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  super.onCreateView(inflater, container, savedInstanceState);

        String title = getArguments().getString("title");
        String imageUrl = getArguments().getString("imageUrl");

        final EditText editText = root.findViewById(R.id.loyalty_card_form_title_edit_text);
        editText.setText(title);

        final ImageView image = root.findViewById(R.id.loyalty_card_form_card_image);
        LoyaltyCardsPicasso loyaltyCardsPicasso = new LoyaltyCardsPicasso(getContext());
        Picasso picasso = loyaltyCardsPicasso.getPicasso();
        picasso.load(imageUrl).into(image);

        return root;
    }
}
