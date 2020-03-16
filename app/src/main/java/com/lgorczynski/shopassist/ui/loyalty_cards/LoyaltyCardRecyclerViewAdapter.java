package com.lgorczynski.shopassist.ui.loyalty_cards;

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

import com.lgorczynski.shopassist.LoyaltyCard;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.receipts.ReceiptRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoyaltyCardRecyclerViewAdapter extends RecyclerView.Adapter<LoyaltyCardRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "LoyaltyCardRecyclerView";

    private List<LoyaltyCard> mLoyaltyCards;
    private Context mContext;

    public LoyaltyCardRecyclerViewAdapter(Context mContext, List<LoyaltyCard> mLoyaltyCards) {
        this.mLoyaltyCards = mLoyaltyCards;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_card_list_item_layout, parent, false);
        ViewHolder holder = new LoyaltyCardRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final LoyaltyCard loyaltyCard = mLoyaltyCards.get(position);
        Picasso.get().load(loyaltyCard.getImageUrl()).into(holder.image);
        holder.title.setText(loyaltyCard.getTitle());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Clicked on: " + loyaltyCard.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLoyaltyCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.loyalty_card_list_image);
            title = itemView.findViewById(R.id.loyalty_card_list_title);
            parentLayout = itemView.findViewById(R.id.loyalty_card_parent_layout);
        }
    }
}
