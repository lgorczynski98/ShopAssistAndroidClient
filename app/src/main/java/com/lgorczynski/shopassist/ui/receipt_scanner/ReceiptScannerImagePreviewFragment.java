package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lgorczynski.shopassist.R;

import java.io.File;


public class ReceiptScannerImagePreviewFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_receipt_scanner_preview, container, false);

        final ImageView previewImage = root.findViewById(R.id.receipt_scanner_preview_image);
        previewImage.setImageURI(Uri.fromFile(new File(getArguments().getString("imagePath"))));

        return root;
    }
}