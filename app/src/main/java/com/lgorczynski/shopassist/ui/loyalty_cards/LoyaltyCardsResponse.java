package com.lgorczynski.shopassist.ui.loyalty_cards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lgorczynski.shopassist.ui.loyalty_cards.LoyaltyCard;

import java.util.List;

public class LoyaltyCardsResponse {

    @SerializedName("loyaltycards")
    @Expose
    private List<LoyaltyCard> loyaltycards;

    public List<LoyaltyCard> getLoyaltycards(){
        return loyaltycards;
    }

}
