package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CustomPicasso;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.squareup.picasso.Picasso;

import java.io.File;


public class LoyaltyCardEditFormFragment extends LoyaltyCardFormFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  super.onCreateView(inflater, container, savedInstanceState);

        int cardID = getArguments().getInt("id");
        String title = getArguments().getString("title");
        String imageUrl = getArguments().getString("imageUrl");

        final EditText editText = root.findViewById(R.id.loyalty_card_form_title_edit_text);
        editText.setText(title);

        final ImageView image = root.findViewById(R.id.loyalty_card_form_card_image);
        CustomPicasso customPicasso = new CustomPicasso(getContext());
        Picasso picasso = customPicasso.getPicasso();
        picasso.load(imageUrl).into(image);

        final Button submitButton = root.findViewById(R.id.loyalty_card_form_submit_button);
        submitButton.setOnClickListener(view -> {
            boolean titleChanged = false;
            boolean imageChanged = false;
            File imageFile = null;
            if(!titleEditText.getText().toString().equals(title))
                titleChanged = true;
            if(currentPhotoPath != null){
                imageChanged = true;
                imageFile = new File(currentPhotoPath);
            }
            if(titleChanged && imageChanged)
                loyaltyCardsViewModel.patchLoyaltyCard(cardID, titleEditText.getText().toString(), imageFile, CredentialsSingleton.getInstance().getToken());
            else if(titleChanged)
                loyaltyCardsViewModel.patchLoyaltyCard(cardID, titleEditText.getText().toString(), CredentialsSingleton.getInstance().getToken());
            else if(imageChanged)
                loyaltyCardsViewModel.patchLoyaltyCard(cardID, imageFile, CredentialsSingleton.getInstance().getToken());
            navController.popBackStack();
        });

        return root;
    }
}
