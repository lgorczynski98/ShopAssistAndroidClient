package com.lgorczynski.shopassist.ui.receipts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.R;

import java.util.ArrayList;
import java.util.List;

public class ReceiptFormRecyclerViewAdapter extends RecyclerView.Adapter<ReceiptFormRecyclerViewAdapter.ViewHolder> {

    private List<String> productNames;
    private List<String> prices;
    private List<String> count;

    public ReceiptFormRecyclerViewAdapter() {
        productNames = new ArrayList<>();
        productNames.add("");
        prices = new ArrayList<>();
        prices.add("");
        count = new ArrayList<>();
        count.add("");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_receipt_form_product_list_item, parent, false);
        ReceiptFormRecyclerViewAdapter.ViewHolder holder = new ReceiptFormRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText productName;
        EditText price;
        EditText count;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.fragment_receipt_form_product_name);
            price = itemView.findViewById(R.id.fragment_receipt_form_product_price);
            count = itemView.findViewById(R.id.fragment_receipt_form_product_count);
        }
    }
}
