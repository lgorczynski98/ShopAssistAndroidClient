package com.lgorczynski.shopassist.ui.receipts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ReceiptScannerActivity;
import com.lgorczynski.shopassist.ui.log_in.CredentialsSingleton;

import java.io.File;
import java.util.List;

import javax.xml.transform.Result;

public class ReceiptsFragment extends Fragment implements ReceiptRecyclerViewAdapter.OnReceiptClickListener{

    private ReceiptsViewModel receiptsViewModel;

    private RecyclerView recyclerView;
    private ReceiptRecyclerViewAdapter adapter;

    private NavController navController;

    private final static int REQUEST_CODE_DOCUMENT_SCAN = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        receiptsViewModel =
                ViewModelProviders.of(this).get(ReceiptsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_receipts, container, false);

        recyclerView = root.findViewById(R.id.receipt_recycler_view);
        receiptsViewModel.getReceiptsResponseLiveData().observe(this, new Observer<List<Receipt>>() {
            @Override
            public void onChanged(List<Receipt> receipts) {
                adapter = new ReceiptRecyclerViewAdapter(getContext(), receipts, ReceiptsFragment.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        receiptsViewModel.getReceipts(CredentialsSingleton.getInstance().getToken());

        FloatingActionButton fab = root.findViewById(R.id.receipt_fab);
        fab.setOnClickListener(v -> startScanningReceipt());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void startScanningReceipt(){
        Toast.makeText(getContext(), "Clicked on receipt fragment", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ReceiptScannerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DOCUMENT_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DOCUMENT_SCAN){
            Bundle extras = data.getExtras();
            if(extras != null){
                String imagePath = extras.getString("imagePath");
                Toast.makeText(getContext(), "Correctly extracted receipt!", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("imagePath", imagePath);
                navController.navigate(R.id.action_navigation_receipts_to_receiptFormFragment, bundle);
                return;
            }
        }
        Toast.makeText(getContext(), "Canceled on extracting receipt", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceiptClick(int position) {
        Receipt receipt = adapter.getItemOnPosition(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("receipt", receipt);
        navController.navigate(R.id.action_navigation_receipts_to_receiptPreviewFragment, bundle);
    }
}