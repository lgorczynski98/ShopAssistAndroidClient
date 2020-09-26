package com.lgorczynski.shopassist.image_managing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageScaler {

    public static Bitmap getScaledBitmap(String photoPath, int prefWidth, int prefHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        //set to get image width and height
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        options.inSampleSize = calcSampleSize(options, prefWidth, prefHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        return bitmap;
    }

    private static int calcSampleSize(BitmapFactory.Options options, int prefWidth, int prefHeight){
        final int width = options.outWidth;
        final int height = options.outHeight;
        int sampleSizeRatio = 1;
        if (height > prefHeight || width > prefWidth){
            final int heightRatio = Math.round((float) height / (float) prefHeight);
            final int widthRatio = Math.round((float) width / (float) prefWidth);
            sampleSizeRatio = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return sampleSizeRatio;
    }
}
