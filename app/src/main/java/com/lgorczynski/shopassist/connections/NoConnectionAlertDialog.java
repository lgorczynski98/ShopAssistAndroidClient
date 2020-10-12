package com.lgorczynski.shopassist.connections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.lgorczynski.shopassist.R;

public class NoConnectionAlertDialog {

    private int message;
    private Context context;
    private ConnectionChecker connectionChecker;

    public NoConnectionAlertDialog(int message, Context context){
        this.message = message;
        this.context = context;
        connectionChecker = new ConnectionChecker(context);
    }

    public AlertDialog.Builder getDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.retry, (dialogInterface, i) -> {
                    Activity activity = (Activity) context;
                    activity.recreate();
                    connectionChecker.checkConnections();
                });
        builder.create();
        return builder;
    }
}
