package com.lgorczynski.shopassist.ui.receipts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.lgorczynski.shopassist.R;

public class ReceiptsFragment extends Fragment {

    private ReceiptsViewModel receiptsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        receiptsViewModel =
                ViewModelProviders.of(this).get(ReceiptsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_receipts, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        receiptsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}