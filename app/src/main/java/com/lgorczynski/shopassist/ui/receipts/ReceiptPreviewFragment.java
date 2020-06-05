package com.lgorczynski.shopassist.ui.receipts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgorczynski.shopassist.R;
import com.squareup.picasso.Picasso;

public class ReceiptPreviewFragment extends Fragment implements View.OnClickListener {

    private Receipt receipt;
    private NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_preview, container, false);

        receipt = (Receipt) getArguments().getSerializable("receipt");

        final ImageView imageView = root.findViewById(R.id.receipt_preview_image);
        final TextView title = root.findViewById(R.id.receipt_preview_title);
        final TextView shopName = root.findViewById(R.id.receipt_preview_shop_name);
        final TextView purchaseDate = root.findViewById(R.id.receipt_preview_purchase_date);
        final TextView price = root.findViewById(R.id.receipt_preview_price);
        final TextView returnDate = root.findViewById(R.id.receipt_preview_return_date);
        final TextView warrantyDate = root.findViewById(R.id.receipt_preview_warranty_date);
        final FloatingActionButton fab = root.findViewById(R.id.receipt_preview_edit_fab);
        fab.setOnClickListener(this);

        title.setText(receipt.getTitle());
        shopName.setText(receipt.getShopName());
        purchaseDate.setText(receipt.getPurchaseDate());
        price.setText(String.valueOf(receipt.getPrice()));
        returnDate.setText(receipt.getReturnDate());
        warrantyDate.setText(receipt.getWarrantyEndDate());

        Picasso.get().load(receipt.getReceiptUrl()).into(imageView);

        return root;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.receipt_preview_edit_fab){
            Bundle bundle = new Bundle();
            bundle.putSerializable("receipt", receipt);
            navController.navigate(R.id.action_receiptPreviewFragment_to_receiptEditFormFragment, bundle);
        }
    }
}
