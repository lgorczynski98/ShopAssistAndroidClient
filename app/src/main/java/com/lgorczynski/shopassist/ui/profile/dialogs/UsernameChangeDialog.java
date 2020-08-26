package com.lgorczynski.shopassist.ui.profile.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.lgorczynski.shopassist.R;

public class UsernameChangeDialog extends AppCompatDialogFragment {

    private EditText usernameEditText;
    private UsernameChangeListener usernameChangeListener;

    public UsernameChangeDialog(UsernameChangeListener usernameChangeListener){
        this.usernameChangeListener = usernameChangeListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_username_change_dialog, null);

        usernameEditText = view.findViewById(R.id.profile_username_change_dialog_username_input);
        builder.setView(view)
                .setTitle(R.string.profile_change_username_title)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.change, (dialogInterface, i) -> {
                    usernameChangeListener.onUsernameChange(usernameEditText.getText().toString());
                });
        return builder.create();
    }

    public interface UsernameChangeListener{
        void onUsernameChange(String username);
    }

}
