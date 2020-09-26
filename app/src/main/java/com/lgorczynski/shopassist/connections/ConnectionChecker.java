package com.lgorczynski.shopassist.connections;

import android.content.Context;
import android.util.Log;

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.loyalty_cards.LoyaltyCard;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionChecker {

    private static final String TAG = "ConnectionChecker";
    private Context context;

    public ConnectionChecker(Context context){
        this.context = context;
    }

    public void checkConnections(){
        try {
            if(canConnectToInternet()){
                Log.d(TAG, "onResume: can connect to internet");
                checkServerConnection();
            }
            else{
                Log.d(TAG, "onResume: cant connect to internet");
                NoConnectionAlertDialog noConnectionAlertDialog = new NoConnectionAlertDialog(R.string.no_internet_connection, context);
                noConnectionAlertDialog.getDialog().show();
            }
        }
        catch(Exception e) {
            Log.d(TAG, "onResume: error");
            NoConnectionAlertDialog noConnectionAlertDialog = new NoConnectionAlertDialog(R.string.no_internet_connection, context);
            noConnectionAlertDialog.getDialog().show();
        }
    }

    private boolean canConnectToInternet() throws IOException, InterruptedException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    private void checkServerConnection(){
        ConnectionService connectionService = new Retrofit.Builder()
                .baseUrl(CredentialsSingleton.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ConnectionService.class);

        connectionService.checkServerConnection().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                    Log.d(TAG, "onResponse: can connect to server");
                else{
                    NoConnectionAlertDialog noConnectionAlertDialog = new NoConnectionAlertDialog(R.string.no_server_connection, context);
                    noConnectionAlertDialog.getDialog().show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: cant connect to server");
                NoConnectionAlertDialog noConnectionAlertDialog = new NoConnectionAlertDialog(R.string.no_server_connection, context);
                noConnectionAlertDialog.getDialog().show();
            }
        });
    }

}
