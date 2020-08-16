package com.lgorczynski.shopassist.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.lgorczynski.shopassist.ui.receipts.Receipt;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageFileCreator {

    private Context context;
    private static final String TAG = "ImageFileCreator";

    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", CredentialsSingleton.getInstance().getToken())
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

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

    public void createReceiptsImageFiles(List<Receipt> receipts){
        DownloaderTask downloaderTask = new DownloaderTask(context);
        downloaderTask.execute(receipts.toArray());
    }

    private static class DownloaderTask extends AsyncTask<Object, String, Object>{

        private Context context;

        public DownloaderTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Started downloading local copies", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(context, "Local copy created", Toast.LENGTH_SHORT).show();
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(String[] values) {
            Toast.makeText(context, "Saved: " + values[0], Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            for (Object object : objects){
                Receipt receipt = (Receipt) object;
                Request request = new Request.Builder().url(CredentialsSingleton.RECEIPTS_IMAGE_BASE_URL + receipt.getId() + "/").build();
                try
                {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "createReceiptsImageFiles: reponse ok");
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    createReceiptImageFile(receipt.getTitle(), bitmap);
                    publishProgress(receipt.getTitle());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Log.d(TAG, "createReceiptsImageFiles: responso no ok");
                }
            }
            return null;
        }

        private void createReceiptImageFile(String title, Bitmap bitmap) throws IOException{
            String fileName = title.replaceAll(" ", "_");
            String saveImageURL = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, fileName);
            Uri.parse(saveImageURL);
        }
    }
}
