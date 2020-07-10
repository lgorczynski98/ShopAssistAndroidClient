package com.lgorczynski.shopassist.ui.receipts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReceiptsService {

    @GET("receipts/")
    Call<List<Receipt>> getReceipts(
        @Header("Authorization") String token
    );
}
