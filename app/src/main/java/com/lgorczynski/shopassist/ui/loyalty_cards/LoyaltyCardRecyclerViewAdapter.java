package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lgorczynski.shopassist.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoyaltyCardRecyclerViewAdapter extends RecyclerView.Adapter<LoyaltyCardRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "LoyaltyCardRecyclerView";

    private List<LoyaltyCard> mLoyaltyCards;
    private Context mContext;
    private OnCardClickListener mOnCardClickListener;

    public LoyaltyCardRecyclerViewAdapter(Context mContext, List<LoyaltyCard> mLoyaltyCards, OnCardClickListener onCardClickListener) {
        this.mLoyaltyCards = mLoyaltyCards;
        this.mContext = mContext;
        this.mOnCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_card_list_item_layout, parent, false);
        ViewHolder holder = new LoyaltyCardRecyclerViewAdapter.ViewHolder(view, mOnCardClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final LoyaltyCard loyaltyCard = mLoyaltyCards.get(position);
        Picasso.get().load(loyaltyCard.getImageUrl()).into(holder.image);
        holder.title.setText(loyaltyCard.getTitle());
    }

    @Override
    public int getItemCount() {
        return mLoyaltyCards.size();
    }

    public LoyaltyCard getItemOnPosition(int position){
        return mLoyaltyCards.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        Button setting;
        ConstraintLayout parentLayout;
        OnCardClickListener onCardClickListener;

        public ViewHolder(@NonNull View itemView, OnCardClickListener onCardClickListener) {
            super(itemView);

            image = itemView.findViewById(R.id.loyalty_card_list_image);
            title = itemView.findViewById(R.id.loyalty_card_list_title);
            setting = itemView.findViewById(R.id.loyalty_card_list_setting_button);
            parentLayout = itemView.findViewById(R.id.loyalty_card_parent_layout);
            this.onCardClickListener = onCardClickListener;
            parentLayout.setOnClickListener(this);
            setting.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.loyalty_card_parent_layout:{
                    onCardClickListener.onCardClick(getAdapterPosition());
                    break;
                }
                case R.id.loyalty_card_list_setting_button:{
                    onCardClickListener.onSettingClick(getAdapterPosition());
                    break;
                }
            }
        }
    }

    public interface OnCardClickListener{
        void onCardClick(int position);
        void onSettingClick(int position);
    }
}
