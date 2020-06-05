package com.lgorczynski.shopassist.ui.receipts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lgorczynski.shopassist.R;
import com.squareup.picasso.Picasso;

public class ReceiptEditFormFragment extends ReceiptFormFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_receipt_form, container, false);

        Receipt receipt = (Receipt)getArguments().getSerializable("receipt");

        final EditText title = root.findViewById(R.id.receipt_form_title);
        final EditText shopName = root.findViewById(R.id.receipt_form_shop_name);
        final EditText purchaseDate = root.findViewById(R.id.receipt_form_date);
        final EditText price = root.findViewById(R.id.receipt_form_cost);
        final ImageView receiptImage = root.findViewById(R.id.receipt_form_receipt_image);
        image = root.findViewById(R.id.receipt_form_image);

        title.setText(receipt.getTitle());
        shopName.setText(receipt.getShopName());
        purchaseDate.setText(receipt.getPurchaseDate());
        price.setText(String.valueOf(receipt.getPrice()));

        Picasso.get().load(receipt.getReceiptUrl()).into(receiptImage);
        Picasso.get().load(receipt.getImageUrl()).into(image);

        final Button submit = root.findViewById(R.id.receipt_form_submit);
        final TextView returnTextView = root.findViewById(R.id.receipt_form_return_text_view);
        final TextView warrantyTextView = root.findViewById(R.id.receipt_form_warranty_text_view);
        final SeekBar returnSeekBar = root.findViewById(R.id.receipt_form_return_seek_bar);
        final SeekBar warrantySeekBar = root.findViewById(R.id.receipt_form_warranty_seek_bar);
        returnSeekBar.setProgress(2);
        String setText = "Return (2 weeks)";
        returnTextView.setText(setText);
        setText = "Warranty (24 months)";
        warrantyTextView.setText(setText);
        warrantySeekBar.setProgress(24);
        returnSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String text = "Return (" + i + " weeks)";
                if(i == returnSeekBar.getMax())
                    text = "Return (unrestricted)";
                returnTextView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        warrantySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String text = "Warranty (" + i + " months)";
                if(i == warrantySeekBar.getMax())
                    text = "Warranty (lifetime)";
                warrantyTextView.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        submit.setOnClickListener(this);

        final Button cameraButton = root.findViewById(R.id.receipt_form_from_camera_button);
        final Button galleryButton = root.findViewById(R.id.receipt_form_from_gallery_button);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);

        return root;
    }
}
