package com.lgorczynski.shopassist.ui.receipts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.R;

public class ReceiptFormFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReceiptFormRecyclerViewAdapter receiptFormRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_form, container, false);

        recyclerView = root.findViewById(R.id.fragment_receipt_form_recyclerview);


        return root;
    }
}
