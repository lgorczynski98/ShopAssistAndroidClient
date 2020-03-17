package com.lgorczynski.shopassist.ui.receipts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReceiptRecyclerViewAdapter extends RecyclerView.Adapter<ReceiptRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "ReceiptRecyclerViewAdap";

    private List<Receipt> mReceipts;
    private Context mContext;

    public ReceiptRecyclerViewAdapter(Context mContext, List<Receipt> mReceipts) {
        this.mReceipts = mReceipts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_list_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final Receipt receipt = mReceipts.get(position);
        Picasso.get().load(receipt.getImageUrl()).into(holder.image);
        holder.title.setText(receipt.getTitle());
        holder.price.setText(String.valueOf(receipt.getPrice()));
        holder.date.setText(receipt.getPurchaseDate());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Clicked on: " + receipt.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReceipts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView price;
        TextView date;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.receipt_list_image);
            title = itemView.findViewById(R.id.receipt_list_title);
            price = itemView.findViewById(R.id.receipt_list_price);
            date = itemView.findViewById(R.id.receipt_list_date);
            parentLayout = itemView.findViewById(R.id.receipt_parent_layout);
        }
    }
}
