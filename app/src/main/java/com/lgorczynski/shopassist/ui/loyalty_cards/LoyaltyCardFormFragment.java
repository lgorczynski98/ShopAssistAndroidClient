package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lgorczynski.shopassist.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class LoyaltyCardFormFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "LoyaltyCardFormFragment";
    private NavController navController;

    private int REQUEST_PICK_PHOTO = 100;
    private int REQUEST_CAPTURE = 101;

    private ImageView cardImage;
    private String capturedPhotoPath;
    private String selectedPhotoPath;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loyalty_card_form, container, false);

        final ImageView barcodeImage = root.findViewById(R.id.loyalty_card_form_barcode);
        final TextView contentText = root.findViewById(R.id.loyalty_card_form_text);
        cardImage = root.findViewById(R.id.loyalty_card_form_card_image);

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

        final Button cameraButton = root.findViewById(R.id.loyalty_card_form_from_camera_button);
        final Button galleryButton = root.findViewById(R.id.loyalty_card_form_from_gallery_button);
        final Button submitButton = root.findViewById(R.id.loyalty_card_form_submit_button);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loyalty_card_form_from_gallery_button:{
                Intent photoPick = new Intent(Intent.ACTION_PICK);
                photoPick.setType("image/*");
                startActivityForResult(photoPick, REQUEST_PICK_PHOTO);
                break;
            }
            case R.id.loyalty_card_form_from_camera_button:{
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    }
                    catch(Exception e) {
                        Log.d(TAG, "onClick: Error on starting camera intent");
                    }
                    if(photoFile != null){
                        Uri photoUri = FileProvider.getUriForFile(getContext(),
                                "com.lgorczynski.shopassist.fileprovider",
                                photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(cameraIntent, REQUEST_CAPTURE);
                    }
                }
                break;
            }
            case R.id.loyalty_card_form_submit_button:{
                Toast.makeText(getContext(), "Added loyalty card correctly", Toast.LENGTH_SHORT).show();
                navController.popBackStack();
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK && data != null){
            Uri pickedPhoto = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedPhoto, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            selectedPhotoPath = imagePath;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            cardImage.setImageBitmap(bitmap);
            cursor.close();
        }
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(capturedPhotoPath, options);
            cardImage.setImageBitmap(bitmap);
        }
    }

    private File createImageFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        capturedPhotoPath = image.getAbsolutePath();
        selectedPhotoPath = capturedPhotoPath;
        return image;
    }
}
