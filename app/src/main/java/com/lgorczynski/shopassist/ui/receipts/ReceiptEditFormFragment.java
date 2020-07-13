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
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.CustomPicasso;
import com.squareup.picasso.Picasso;

public class ReceiptEditFormFragment extends ReceiptFormFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_receipt_form, container, false);

        Receipt receipt = (Receipt)getArguments().getSerializable("receipt");

        title = root.findViewById(R.id.receipt_form_title);
        shopName = root.findViewById(R.id.receipt_form_shop_name);
        purchaseDate = root.findViewById(R.id.receipt_form_date);
        price = root.findViewById(R.id.receipt_form_cost);
        final ImageView receiptImage = root.findViewById(R.id.receipt_form_receipt_image);
        image = root.findViewById(R.id.receipt_form_image);

        title.setText(receipt.getTitle());
        shopName.setText(receipt.getShopName());
        purchaseDate.setText(receipt.getPurchaseDate());
        purchaseDate.setOnClickListener(this);
        price.setText(String.valueOf(receipt.getPrice()));

        CustomPicasso customPicasso = new CustomPicasso(getContext());
        Picasso picasso = customPicasso.getPicasso();
        picasso.load(CredentialsSingleton.RECEIPTS_IMAGE_BASE_URL + receipt.getId() + "/").into(receiptImage);
        picasso.load(CredentialsSingleton.RECEIPTS_THUMBNAIL_BASE_URL + receipt.getId() + "/").into(image);

        submit = root.findViewById(R.id.receipt_form_submit);
        returnTextView = root.findViewById(R.id.receipt_form_return_text_view);
        warrantyTextView = root.findViewById(R.id.receipt_form_warranty_text_view);
        returnSeekBar = root.findViewById(R.id.receipt_form_return_seek_bar);
        warrantySeekBar = root.findViewById(R.id.receipt_form_warranty_seek_bar);
        returnSeekBar.setProgress(receipt.getReturnWeeks());
        String setText = "Return (" + receipt.getReturnWeeks()  + " weeks)";
        returnTextView.setText(setText);
        setText = "Warranty (" + receipt.getWarrantyMonths() + " months)";
        warrantyTextView.setText(setText);
        warrantySeekBar.setProgress(receipt.getWarrantyMonths());
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
