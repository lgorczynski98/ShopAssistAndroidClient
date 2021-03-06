package com.lgorczynski.shopassist.image_managing;

import android.content.Context;

import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomPicasso {

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

    private Picasso picasso;

    public CustomPicasso(Context mContext){
        picasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(client)).build();
    }

    public Picasso getPicasso() {
        return picasso;
    }
}
