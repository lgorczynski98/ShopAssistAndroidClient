package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lgorczynski.shopassist.R;

public class LoyaltyCardFormFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loyalty_card_form, container, false);

        final ImageView barcodeImage = root.findViewById(R.id.loyalty_card_form_barcode);
        final TextView contentText = root.findViewById(R.id.loyalty_card_form_text);

        String content = getArguments().getString("content");
        contentText.setText(content);
        BarcodeFormat format = BarcodeFormat.valueOf(getArguments().getString("format"));
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, format, 800, 400);
            barcodeImage.setImageBitmap(bitmap);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return root;
    }
}
