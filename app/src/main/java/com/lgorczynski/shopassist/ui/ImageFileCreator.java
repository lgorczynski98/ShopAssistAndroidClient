package com.lgorczynski.shopassist.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ImageFileCreator {

    private Context context;

    public ImageFileCreator(Context context){
        this.context = context;
    }

    public File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
//        capturedPhotoPath = image.getAbsolutePath();
//        selectedPhotoPath = capturedPhotoPath;
        return image;
    }

    public File createTempThumbnailFile(Bitmap bitmap) throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File thumbnail = File.createTempFile(imageFileName, ".jpg", storageDir);
        try (OutputStream out = new FileOutputStream(thumbnail)){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        }
        catch (Exception e){
            Log.d(TAG, "createTempThumbnailFile: failed on creating temporary thumbnail file");
            throw new IOException("Failed on creating temporary thumbnail file");
        }
        return thumbnail;
    }

}
