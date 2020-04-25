package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lgorczynski.shopassist.R;

public class ReceiptScannerImagePreviewFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_scanner_preview, container, false);

        Bitmap bitmap = getArguments().getParcelable("bitmap");
        final ImageView imageView = root.findViewById(R.id.receipt_scanner_preview_image);
        imageView.setImageBitmap(bitmap);

        return root;
    }

}
