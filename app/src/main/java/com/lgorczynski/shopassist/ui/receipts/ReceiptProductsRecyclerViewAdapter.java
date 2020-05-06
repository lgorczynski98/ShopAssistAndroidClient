package com.lgorczynski.shopassist.ui.receipts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.R;

public class ReceiptProductsRecyclerViewAdapter extends RecyclerView.Adapter<ReceiptProductsRecyclerViewAdapter.ViewHolder> {

    public int productsCount = 1;
    private Context context;

    public ReceiptProductsRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_form_product_layout, parent, false);
        ViewHolder holder = new ReceiptProductsRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productsCount;
    }

    public void addProductField(){
        productsCount++;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText productName;
        EditText productQuantity;
        EditText productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.fragment_receipt_form_product_name);
            productQuantity = itemView.findViewById(R.id.fragment_receipt_form_product_count);
            productPrice = itemView.findViewById(R.id.fragment_receipt_form_product_price);
        }
    }
}
