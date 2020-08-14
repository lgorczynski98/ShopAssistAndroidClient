package com.lgorczynski.shopassist.ui.receipts;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.print.PrintHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;
import com.lgorczynski.shopassist.ui.CustomPicasso;
import com.squareup.picasso.Picasso;

public class ReceiptPreviewFragment extends Fragment implements View.OnClickListener {

    private Receipt receipt;
    private NavController navController;
    private ImageView imageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipt_preview, container, false);

        receipt = (Receipt) getArguments().getSerializable("receipt");

        imageView = root.findViewById(R.id.receipt_preview_image);
        final TextView title = root.findViewById(R.id.receipt_preview_title);
        final TextView shopName = root.findViewById(R.id.receipt_preview_shop_name);
        final TextView purchaseDate = root.findViewById(R.id.receipt_preview_purchase_date);
        final TextView price = root.findViewById(R.id.receipt_preview_price);
        final TextView returnDate = root.findViewById(R.id.receipt_preview_return_date);
        final TextView warrantyDate = root.findViewById(R.id.receipt_preview_warranty_date);
        final Button print = root.findViewById(R.id.receipt_preview_print);

        title.setText(receipt.getTitle());
        shopName.setText(receipt.getShopName());
        purchaseDate.setText(receipt.getPurchaseDate());
        price.setText(String.valueOf(receipt.getPrice()));
        returnDate.setText(receipt.getReturnDate());
        warrantyDate.setText(receipt.getWarrantyEndDate());

        CustomPicasso customPicasso = new CustomPicasso(getContext());
        Picasso picasso = customPicasso.getPicasso();
        picasso.load(CredentialsSingleton.RECEIPTS_IMAGE_BASE_URL + receipt.getId() + "/").into(imageView);

        print.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.receipt_preview_print)
            printReceipt();
    }

    private void printReceipt() {
        PrintHelper printHelper = new PrintHelper(getContext());
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        printHelper.printBitmap("Print receipt: " + receipt.getTitle(), bitmap);
    }
}
