package com.lgorczynski.shopassist.ui.receipts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Receipt>> mReceipts;

    public ReceiptsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is receipt fragment");

        List<Receipt> exampleReceipts = new ArrayList<>();
        exampleReceipts.add(new Receipt("Buty Fila", "15.03.2020", "https://www.e-bestsell.pl/userdata/gfx/207.jpg", 18.99f));
        exampleReceipts.add(new Receipt("Bluza adidas", "15.03.2020", "https://szopex.blob.core.windows.net/shops/media/f1000/2018/adidas/113666/bluza-adidas-trefoil-hoodie-dt7964-5bbc79e1b5b60.jpg", 15f));
        exampleReceipts.add(new Receipt("Spodnie robocze", "15.03.2020", "https://sklep.lahtipro.pl/1642-large_default/spodnie-ostrzegawcze-robocze-do-pasa-zolte-slim-fit-lahti-pro-l40511.jpg", 135.99f));
        exampleReceipts.add(new Receipt("Kask ochronny", "15.03.2020", "https://image.ceneostatic.pl/data/products/50588316/i-condor-kask-budowlany-zolty-con-shk-0006.jpg", 100.99f));
        exampleReceipts.add(new Receipt("Buty Fila", "15.03.2020", "https://www.e-bestsell.pl/userdata/gfx/207.jpg", 18.99f));
        exampleReceipts.add(new Receipt("Bluza adidas", "15.03.2020", "https://szopex.blob.core.windows.net/shops/media/f1000/2018/adidas/113666/bluza-adidas-trefoil-hoodie-dt7964-5bbc79e1b5b60.jpg", 15f));
        exampleReceipts.add(new Receipt("Spodnie robocze", "15.03.2020", "https://sklep.lahtipro.pl/1642-large_default/spodnie-ostrzegawcze-robocze-do-pasa-zolte-slim-fit-lahti-pro-l40511.jpg", 135.99f));
        exampleReceipts.add(new Receipt("Kask ochronny", "15.03.2020", "https://image.ceneostatic.pl/data/products/50588316/i-condor-kask-budowlany-zolty-con-shk-0006.jpg", 100.99f));
        exampleReceipts.add(new Receipt("Buty Fila", "15.03.2020", "https://www.e-bestsell.pl/userdata/gfx/207.jpg", 18.99f));
        exampleReceipts.add(new Receipt("Bluza adidas", "15.03.2020", "https://szopex.blob.core.windows.net/shops/media/f1000/2018/adidas/113666/bluza-adidas-trefoil-hoodie-dt7964-5bbc79e1b5b60.jpg", 15f));
        exampleReceipts.add(new Receipt("Spodnie robocze", "15.03.2020", "https://sklep.lahtipro.pl/1642-large_default/spodnie-ostrzegawcze-robocze-do-pasa-zolte-slim-fit-lahti-pro-l40511.jpg", 135.99f));
        exampleReceipts.add(new Receipt("Kask ochronny", "15.03.2020", "https://image.ceneostatic.pl/data/products/50588316/i-condor-kask-budowlany-zolty-con-shk-0006.jpg", 100.99f));


        mReceipts = new MutableLiveData<>();
        mReceipts.setValue(exampleReceipts);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<Receipt>> getReceipts() {
        return mReceipts;
    }
}