package com.lgorczynski.shopassist.ui.loyalty_cards;

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

public class LoyaltyCardRecyclerViewAdapter extends RecyclerView.Adapter<LoyaltyCardRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "LoyaltyCardRecyclerView";

    private List<LoyaltyCard> mAllLoyaltyCards;
    private List<LoyaltyCard> mLoyaltyCards;
    private Context mContext;
    private OnCardClickListener mOnCardClickListener;

    private CustomPicasso customPicasso;

    public LoyaltyCardRecyclerViewAdapter(Context mContext, List<LoyaltyCard> mLoyaltyCards, OnCardClickListener onCardClickListener) {
        this.mAllLoyaltyCards = mLoyaltyCards;
        try {
            this.mLoyaltyCards = new ArrayList<>(mLoyaltyCards);
        }
        catch(NullPointerException e) {
            this.mLoyaltyCards = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mOnCardClickListener = onCardClickListener;
        this.customPicasso = new CustomPicasso(mContext);
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

        Picasso picasso = customPicasso.getPicasso();
        picasso.load(CredentialsSingleton.LOYALTYCARDS_IMAGE_BASE_URL + loyaltyCard.getId() + "/").into(holder.image);
        holder.title.setText(loyaltyCard.getTitle());
    }

    @Override
    public int getItemCount() {
        try {
            return mLoyaltyCards.size();
        }
        catch(Exception e) {
            return 0;
        }
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {

        private int previousPatternLenght = 0;

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<LoyaltyCard> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mAllLoyaltyCards);
                previousPatternLenght = 0;
            }
            else{
                if(previousPatternLenght > charSequence.length()){
                    try {
                        mLoyaltyCards = new ArrayList<>(mAllLoyaltyCards);
                    }
                    catch(NullPointerException e) {
                        mLoyaltyCards = new ArrayList<>();
                    }
                }
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(LoyaltyCard loyaltyCard : mLoyaltyCards){
                    if(loyaltyCard.getTitle().toLowerCase().contains(filterPattern))
                        filteredList.add(loyaltyCard);
                }
                previousPatternLenght = charSequence.length();
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mLoyaltyCards.clear();
            try {
                mLoyaltyCards.addAll((List)filterResults.values);
            }
            catch(NullPointerException e) {
                Log.d(TAG, "publishResults: No result to be displayed");
            }
            notifyDataSetChanged();
        }
    };
}
