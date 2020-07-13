package com.lgorczynski.shopassist.ui.receipts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ReceiptScannerActivity;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;

import java.util.List;

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

    @Override
    public void onSettingsClick(int position) {
        Receipt receipt = adapter.getItemOnPosition(position);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.receipts_settings_bottom_sheet_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        final TextView title = bottomSheetDialog.findViewById(R.id.receipts_settings_bottom_sheet_dialog_title);
        title.setText(receipt.getTitle());

        final LinearLayout edit = bottomSheetDialog.findViewById(R.id.receipts_settings_bottom_sheet_dialog_edit_layout);
        final LinearLayout delete = bottomSheetDialog.findViewById(R.id.receipts_settings_bottom_sheet_dialog_delete_layout);

        edit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("receipt", receipt);
            navController.navigate(R.id.action_navigation_receipts_to_receiptEditFormFragment, bundle);
            bottomSheetDialog.cancel();
        });
        delete.setOnClickListener(view -> {
            receiptsViewModel.deleteReceipt(receipt.getId(), CredentialsSingleton.getInstance().getToken());
            bottomSheetDialog.cancel();
        });

        bottomSheetDialog.show();
    }
}