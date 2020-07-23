package com.lgorczynski.shopassist.ui.receipts;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PriceTextWatcher implements TextWatcher {

    EditText editText;

    public PriceTextWatcher(EditText editText){
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        String input = charSequence.toString();
        if(input.length() > 0 && input.contains(".")){
            int dotIndex = input.indexOf('.');
            if(input.length() - dotIndex >= 4){
                String formated = input.substring(0, dotIndex + 3);
                editText.setText(formated);
                editText.setSelection(formated.length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}
