package com.lgorczynski.shopassist.ui.receipt_scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lgorczynski.shopassist.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptScannerImagePreviewFragment extends Fragment implements View.OnClickListener {

    private Bitmap bitmap;
    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_scanner_preview, container, false);

        bitmap = getArguments().getParcelable("bitmap");
        imageView = root.findViewById(R.id.receipt_scanner_preview_image);
        imageView.setImageBitmap(bitmap);

        final ImageButton applyButton = root.findViewById(R.id.receipt_scanner_preview_apply);
        final ImageButton rotateLeft = root.findViewById(R.id.receipt_scanner_preview_rotate_left);
        final ImageButton rotateRight = root.findViewById(R.id.receipt_scanner_preview_rotate_right);
        applyButton.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.receipt_scanner_preview_apply:{
                try {
                    Intent intent = new Intent();
                    intent.putExtra("image", createImageFile().getAbsolutePath());
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
                catch(Exception e) {
                    Toast.makeText(getContext(), "Failed on saving extracted receipt", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.receipt_scanner_preview_rotate_left:{
                bitmap = rotateBitmap(bitmap, -90);
                imageView.setImageBitmap(bitmap);
                break;
            }
            case R.id.receipt_scanner_preview_rotate_right:{
                bitmap = rotateBitmap(bitmap, 90);
                imageView.setImageBitmap(bitmap);
                break;
            }
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

}
