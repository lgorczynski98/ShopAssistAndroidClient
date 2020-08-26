package com.lgorczynski.shopassist.ui.profile.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.lgorczynski.shopassist.R;

public class EmailChangeDialog extends AppCompatDialogFragment {

    private EditText emailEditText;
    private EmailChangeListener emailChangeListener;

    public EmailChangeDialog(EmailChangeListener emailChangeListener){
        this.emailChangeListener = emailChangeListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_email_change_dialog, null);

        emailEditText = view.findViewById(R.id.profile_email_change_dialog_username_input);
        builder.setView(view)
                .setTitle(R.string.profile_change_email_title)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.change, (dialogInterface, i) -> {
                    emailChangeListener.onEmailChange(emailEditText.getText().toString());
                });
        return builder.create();
    }

    public interface EmailChangeListener{
        void onEmailChange(String email);
    }

}
