package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.lgorczynski.shopassist.R;

public class ShareDialog extends AppCompatDialogFragment{

    private EditText editTextUsername;
    private ShareDialogListener shareDialogListener;
    private int cardID;

    public ShareDialog(ShareDialogListener shareDialogListener, int cardID){
        this.shareDialogListener = shareDialogListener;
        this.cardID = cardID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.loyalty_card_share_dialog, null);

        editTextUsername = view.findViewById(R.id.loyalty_card_share_dialog_username_input);
        builder.setView(view)
                .setTitle(R.string.share)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.share, ((dialogInterface, i) -> {
                    shareDialogListener.onShare(editTextUsername.getText().toString(), cardID);
                }));

        return builder.create();
    }

    interface ShareDialogListener{
        void onShare(String username, int cardID);
    }
}
