package com.lgorczynski.shopassist.ui.receipts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
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

    public void postReceipt(String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, File image, File thumbnail, String token){
        receiptsRepository.postReceipt(title, shopName, purchaseDate, purchaseCost, weeksToReturn, monthsOfWarranty, image, thumbnail, token);
    }

    public void postReceipt(String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, File image, String token){
        receiptsRepository.postReceipt(title, shopName, purchaseDate, purchaseCost, weeksToReturn, monthsOfWarranty, image, token);
    }

    public void deleteReceipt(int receiptID, String token, BottomSheetDialog bottomSheetDialog){
        receiptsRepository.deleteReceipt(receiptID, token, bottomSheetDialog);
    }

    public void patchReceipt(int receiptID, String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, String token){
        receiptsRepository.patchReceipt(receiptID, title, shopName, purchaseDate, purchaseCost, weeksToReturn, monthsOfWarranty, token);
    }

    public void patchReceipt(int receiptID, File thumbnail, String token){
        receiptsRepository.patchReceipt(receiptID, thumbnail, token);
    }

    public void patchReceipt(int receiptID, String title, String shopName, String purchaseDate, String purchaseCost, int weeksToReturn, int monthsOfWarranty, File thumbnail, String token){
        receiptsRepository.patchReceipt(receiptID, title, shopName, purchaseDate, purchaseCost, weeksToReturn, monthsOfWarranty, thumbnail, token);
    }

    public LiveData<List<Receipt>> getReceiptsResponseLiveData() {
        return receiptsResponseLiveData;
    }

    public void setRepositoryNavController(NavController navController){
        receiptsRepository.setNavController(navController);
    }
}