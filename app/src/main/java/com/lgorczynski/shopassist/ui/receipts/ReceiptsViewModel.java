package com.lgorczynski.shopassist.ui.receipts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsViewModel extends ViewModel {

    private static final String TAG = "ReceiptsViewModel";
    private ReceiptsRepository receiptsRepository;
    private LiveData<List<Receipt>> receiptsResponseLiveData;

    public ReceiptsViewModel() {
        receiptsRepository = new ReceiptsRepository();
        receiptsResponseLiveData = receiptsRepository.getReceiptsResponseMutableLiveData();
    }

    public void getReceipts(String token){
        receiptsRepository.getReceipts(token);
    }

    public LiveData<List<Receipt>> getReceiptsResponseLiveData() {
        return receiptsResponseLiveData;
    }
}