package com.lgorczynski.shopassist.ui.receipts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.image_managing.CustomPicasso;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReceiptRecyclerViewAdapter extends RecyclerView.Adapter<ReceiptRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "ReceiptRecyclerViewAdap";

    private List<Receipt> mReceipts;
    private List<Receipt> mAllReceipts;
    private Context mContext;
    private OnReceiptClickListener mOnReceiptClickListener;

    private CustomPicasso customPicasso;

    public ReceiptRecyclerViewAdapter(Context mContext, List<Receipt> mReceipts, OnReceiptClickListener onReceiptClickListener) {
        this.mAllReceipts = mReceipts;
        try {
            this.mReceipts = new ArrayList<>(mReceipts);
        }
        catch(NullPointerException e) {
            this.mReceipts = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mOnReceiptClickListener = onReceiptClickListener;
        this.customPicasso = new CustomPicasso(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_list_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnReceiptClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final Receipt receipt = mReceipts.get(position);

        Picasso picasso = customPicasso.getPicasso();
        picasso.load(CredentialsSingleton.RECEIPTS_THUMBNAIL_BASE_URL + receipt.getId() + "/").into(holder.image);

        holder.title.setText(receipt.getTitle());
        holder.price.setText(String.valueOf(receipt.getPrice()));
        holder.date.setText(receipt.getPurchaseDate());
    }

    @Override
    public int getItemCount() {
        try {
            return mReceipts.size();
        }
        catch(Exception e) {
            return 0;
        }
    }

    public Receipt getItemOnPosition(int position){
        return mReceipts.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView price;
        TextView date;
        Button setting;
        ConstraintLayout parentLayout;
        OnReceiptClickListener onReceiptClickListener;

        public ViewHolder(@NonNull View itemView, OnReceiptClickListener onReceiptClickListener) {
            super(itemView);

            image = itemView.findViewById(R.id.receipt_list_image);
            title = itemView.findViewById(R.id.receipt_list_title);
            price = itemView.findViewById(R.id.receipt_list_price);
            date = itemView.findViewById(R.id.receipt_list_date);
            setting = itemView.findViewById(R.id.receipt_list_setting_button);
            parentLayout = itemView.findViewById(R.id.receipt_parent_layout);
            this.onReceiptClickListener = onReceiptClickListener;
            parentLayout.setOnClickListener(this);
            setting.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.receipt_parent_layout:
                    onReceiptClickListener.onReceiptClick(getAdapterPosition());
                    break;
                case R.id.receipt_list_setting_button:
                    onReceiptClickListener.onSettingsClick(getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnReceiptClickListener{
        void onReceiptClick(int position);
        void onSettingsClick(int position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {

        private int previousPatternLenght = 0;

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Receipt> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mAllReceipts);
                previousPatternLenght = 0;
            }
            else{
                if(previousPatternLenght > charSequence.length()){
                    try {
                        mReceipts = new ArrayList<>(mAllReceipts);
                    }
                    catch(NullPointerException e) {
                        mReceipts = new ArrayList<>();
                    }
                }
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Receipt receipt : mReceipts){
                    if(receipt.getTitle().toLowerCase().contains(filterPattern)
                    || receipt.getShopName().toLowerCase().contains(filterPattern)){
                        filteredList.add(receipt);
                    }
                }
                previousPatternLenght = charSequence.length();
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mReceipts.clear();
            try {
                mReceipts.addAll((List)filterResults.values);
            }
            catch(NullPointerException e) {
                Log.d(TAG, "publishResults: No results to be displayed");
            }
            notifyDataSetChanged();
        }
    };
}
