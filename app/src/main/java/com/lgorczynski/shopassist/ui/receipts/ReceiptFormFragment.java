package com.lgorczynski.shopassist.ui.receipts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.lgorczynski.shopassist.R;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReceiptFormFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private File imageFile;

    private ReceiptProductsRecyclerViewAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_form, container, false);

        String imagePath = getArguments().getString("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageFile = new File(imagePath);

        final TextView text = root.findViewById(R.id.receipt_form_extracted_text_test);
        final ImageView imagePreview = root.findViewById(R.id.receipt_form_receipt_image);
        imagePreview.setImageBitmap(bitmap);

        String receiptText = detectReceiptText(bitmap);
        text.setText(receiptText);

        final EditText title = root.findViewById(R.id.receipt_form_title);
        final EditText shopName = root.findViewById(R.id.receipt_form_shop_name);
        final EditText purchaseDate = root.findViewById(R.id.receipt_form_date);
        final EditText price = root.findViewById(R.id.receipt_form_cost);
        final Button submit = root.findViewById(R.id.receipt_form_submit);
        final Button addProduct = root.findViewById(R.id.receipt_form_add_product);
        final RecyclerView recyclerView = root.findViewById(R.id.receipt_form_recycler_view);

        submit.setOnClickListener(this);
        addProduct.setOnClickListener(this);

        price.setText(getPurchaseCost(receiptText));
        purchaseDate.setText(getPurchaseDate(receiptText));
        shopName.setText(getShopName(receiptText));

        adapter = new ReceiptProductsRecyclerViewAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    private String detectReceiptText(Bitmap bitmap){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getContext()).build();
        if(!textRecognizer.isOperational())
            Toast.makeText(getContext(), "Error on text recognizing", Toast.LENGTH_SHORT).show();
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            StringBuilder stringBuilder = new StringBuilder();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        textRecognizer.release();
        return "";
    }

    private String getShopName(String receiptText){
        String firstReceiptLine;
        try {
            firstReceiptLine = receiptText.substring(0, receiptText.indexOf('\n'));
        }
        catch (StringIndexOutOfBoundsException e){
            Log.d(TAG, "getShopName: Didnt found any line of text");
            return "";
        }
        String pattern = "[sS]p\\. [zZ2] [oO0]\\.[oO0]\\.";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(firstReceiptLine);
        String shopName = firstReceiptLine;
        if(matcher.find())
            shopName = firstReceiptLine.substring(0, matcher.start());
        return shopName;
    }

    private String getPurchaseDate(String receiptText){
        String pattern = "(0[1-9]|[1-2][0-9]|3[0-1])\\-(0[1-9]|1[0-2])\\-([1-2][0-9]{3})";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(receiptText);
        String purchaseDate = "";
        if(matcher.find())
            purchaseDate = matcher.group(0);
        return purchaseDate;
    }

    private String getPurchaseCost(String receiptText){
        String pattern = "[0-9]+(\\.|,)[0-9]{2}";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(receiptText);
        String price = "";
        while(matcher.find())
            price = matcher.group(0);
        return price;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageFile.delete();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.receipt_form_submit:{
                Toast.makeText(getContext(), "Added receipt correctly", Toast.LENGTH_SHORT).show();
                navController.popBackStack();
            }
            case R.id.receipt_form_add_product:{
                adapter.addProductField();
            }
        }
    }
}
