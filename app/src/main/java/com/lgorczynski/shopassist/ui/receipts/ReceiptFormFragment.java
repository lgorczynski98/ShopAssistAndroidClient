package com.lgorczynski.shopassist.ui.receipts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReceiptFormFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private NavController navController;
    private File imageFile;
    protected ReceiptsViewModel receiptsViewModel;

    protected ImageView image;
    private String capturedPhotoPath;
    private String selectedPhotoPath;
    private String currentPhotoPath;
    private int REQUEST_PICK_PHOTO = 500;
    private int REQUEST_CAPTURE = 501;

    protected EditText title;
    protected EditText shopName;
    protected EditText purchaseDate;
    protected EditText price;
    protected Button submit;
    protected TextView returnTextView;
    protected TextView warrantyTextView;
    protected SeekBar returnSeekBar;
    protected SeekBar warrantySeekBar;
    protected DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        receiptsViewModel =
                ViewModelProviders.of(this).get(ReceiptsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_receipt_form, container, false);

        String imagePath = getArguments().getString("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageFile = new File(imagePath);

        final TextView text = root.findViewById(R.id.receipt_form_extracted_text_test);
        final ImageView imagePreview = root.findViewById(R.id.receipt_form_receipt_image);
        imagePreview.setImageBitmap(bitmap);

        String receiptText = detectReceiptText(bitmap);
        text.setText(receiptText);

        title = root.findViewById(R.id.receipt_form_title);
        shopName = root.findViewById(R.id.receipt_form_shop_name);
        purchaseDate = root.findViewById(R.id.receipt_form_date);
        purchaseDate.setOnClickListener(this);
        price = root.findViewById(R.id.receipt_form_cost);
        submit = root.findViewById(R.id.receipt_form_submit);
        returnTextView = root.findViewById(R.id.receipt_form_return_text_view);
        warrantyTextView = root.findViewById(R.id.receipt_form_warranty_text_view);
        returnSeekBar = root.findViewById(R.id.receipt_form_return_seek_bar);
        warrantySeekBar = root.findViewById(R.id.receipt_form_warranty_seek_bar);
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

        price.setText(getPurchaseCost(receiptText));
        purchaseDate.setText(getPurchaseDate(receiptText));
        shopName.setText(getShopName(receiptText));

        final Button cameraButton = root.findViewById(R.id.receipt_form_from_camera_button);
        final Button galleryButton = root.findViewById(R.id.receipt_form_from_gallery_button);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        image = root.findViewById(R.id.receipt_form_image);

        return root;
    }

    private String detectReceiptText(Bitmap bitmap){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
        if(!textRecognizer.isOperational())
            Toast.makeText(getContext(), "Error on text recognizing", Toast.LENGTH_SHORT).show();
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            StringBuilder stringBuilder = new StringBuilder();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        textRecognizer.release();
        return "";
    }

    private String getShopName(String receiptText){
        String firstReceiptLine;
        try {
            firstReceiptLine = receiptText.substring(0, receiptText.indexOf('\n'));
        }
        catch (StringIndexOutOfBoundsException e){
            Log.d(TAG, "getShopName: Didnt found any line of text");
            return "";
        }
        String pattern = "[sS]p\\. [zZ2] [oO0]\\.[oO0]\\.";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(firstReceiptLine);
        String shopName = firstReceiptLine;
        if(matcher.find())
            shopName = firstReceiptLine.substring(0, matcher.start());
        return shopName;
    }

    private String getPurchaseDate(String receiptText){
//        String pattern = "((0[1-9])|([1-2][0-9])|(3[0-1]))\\-((0[1-9])|(1[0-2]))\\-([1-2][0-9]{3})";      //10-07-2020
        String pattern = "([1-2][0-9]{3})\\-((0[1-9])|(1[0-2]))\\-((0[1-9])|([1-2][0-9])|(3[0-1]))";        //2020-07-10
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(receiptText);
        String purchaseDate = "";
        if(matcher.find())
            purchaseDate = matcher.group(0);
        return purchaseDate;
    }

    private String getPurchaseCost(String receiptText){
        String pattern = "[0-9]+(\\.|,)[0-9]{2}";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(receiptText);
        String price = "";
        while(matcher.find())
            price = matcher.group(0);
        return price;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            imageFile.delete();
        }
        catch(NullPointerException e) {
            Log.d(TAG, "onDestroy: ImageFile is null");
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.receipt_form_date:
                showDatePickerDialog();
                break;
            case R.id.receipt_form_submit:
                try {
                    File thumbnail = new File(currentPhotoPath);
                    receiptsViewModel.postReceipt(title.getText().toString(), shopName.getText().toString(), purchaseDate.getText().toString(), price.getText().toString(), returnSeekBar.getProgress(), warrantySeekBar.getProgress(), imageFile, thumbnail, CredentialsSingleton.getInstance().getToken());
                }
                catch(Exception e) {
                    receiptsViewModel.postReceipt(title.getText().toString(), shopName.getText().toString(), purchaseDate.getText().toString(), price.getText().toString(), returnSeekBar.getProgress(), warrantySeekBar.getProgress(), imageFile, CredentialsSingleton.getInstance().getToken());
                }
                navController.popBackStack();
                break;
            case R.id.receipt_form_from_camera_button:
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
            case R.id.receipt_form_from_gallery_button:
                Intent photoPick = new Intent(Intent.ACTION_PICK);
                photoPick.setType("image/*");
                startActivityForResult(photoPick, REQUEST_PICK_PHOTO);
        }
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        String date = dateFormat.format(calendar.getTime());
        purchaseDate.setText(date);
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
            currentPhotoPath = imagePath;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            image.setImageBitmap(bitmap);
            cursor.close();
        }
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            currentPhotoPath = capturedPhotoPath;
            Bitmap bitmap = BitmapFactory.decodeFile(capturedPhotoPath, options);
            image.setImageBitmap(bitmap);
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        capturedPhotoPath = image.getAbsolutePath();
        selectedPhotoPath = capturedPhotoPath;
        return image;
    }
}
