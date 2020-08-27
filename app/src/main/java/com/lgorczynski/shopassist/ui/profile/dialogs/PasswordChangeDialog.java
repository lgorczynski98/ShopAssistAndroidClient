package com.lgorczynski.shopassist.ui.profile.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.lgorczynski.shopassist.R;

public class PasswordChangeDialog extends AppCompatDialogFragment {

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private PasswordChangeListener passwordChangeListener;

    public PasswordChangeDialog(PasswordChangeListener passwordChangeListener){
        this.passwordChangeListener = passwordChangeListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_password_change_dialog, null);

        oldPasswordEditText = view.findViewById(R.id.profile_password_change_dialog_old_password_input);
        newPasswordEditText = view.findViewById(R.id.profile_password_change_dialog_new_password_input);
        confirmPasswordEditText = view.findViewById(R.id.profile_password_change_dialog_confirm_password_input);
        builder.setView(view)
                .setTitle(R.string.password)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.change, (dialogInterface, i) -> {
                   if(!newPasswordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
                       Toast.makeText(getContext(), "Passwords must match", Toast.LENGTH_SHORT).show();
                   else
                       passwordChangeListener.onPasswordChange(oldPasswordEditText.getText().toString(), newPasswordEditText.getText().toString());
                });
        return builder.create();
    }

    public interface PasswordChangeListener{
        void onPasswordChange(String password, String newPassword);
    }

}
