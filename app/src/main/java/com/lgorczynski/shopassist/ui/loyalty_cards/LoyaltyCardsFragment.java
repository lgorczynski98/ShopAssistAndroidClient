package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.CaptureActivityPortrait;

import java.util.List;

public class LoyaltyCardsFragment extends Fragment implements LoyaltyCardRecyclerViewAdapter.OnCardClickListener {

    private static final String TAG = "LoyaltyCardsFragment";

    private LoyaltyCardsViewModel loyaltyCardsViewModel;

    private RecyclerView recyclerView;
    private LoyaltyCardRecyclerViewAdapter adapter;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loyaltyCardsViewModel =
                ViewModelProviders.of(this).get(LoyaltyCardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loyalty_cards, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        loyaltyCardsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //recycler view z kartami lojalnosciowymi
        recyclerView = root.findViewById(R.id.loyalty_cards_recycler_view);
        loyaltyCardsViewModel.getLoyaltyCards().observe(this, new Observer<List<LoyaltyCard>>() {
            @Override
            public void onChanged(List<LoyaltyCard> loyaltyCards) {
                adapter = new LoyaltyCardRecyclerViewAdapter(getContext(), loyaltyCards, LoyaltyCardsFragment.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        //skanowanie kodow
        FloatingActionButton fab = root.findViewById(R.id.loyalty_cards_fab);
        fab.setOnClickListener(v -> startScanning());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    private void startScanning(){
        IntentIntegrator integrator= IntentIntegrator.forSupportFragment(this);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.setPrompt("Scan your loyalty card barcode");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String toast;
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents() + " " + result.getFormatName();
                Bundle bundle = new Bundle();
                bundle.putString("content", result.getContents());
                bundle.putString("format", result.getFormatName());
                navController.navigate(R.id.action_navigation_loyalty_cards_to_loyaltyCardFormFragment, bundle);
            }

            Toast.makeText(getContext(), toast, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCardClick(int position) {
//        Toast.makeText(getContext(), "Click on item: " + adapter.getItemOnPosition(position).getTitle(), Toast.LENGTH_SHORT).show();
        LoyaltyCard card = adapter.getItemOnPosition(position);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.loyalty_cards_bottom_sheet_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        final ImageView barcodeImage = bottomSheetDialog.findViewById(R.id.loyalty_cards_bottom_sheet_dialog_image);
        final TextView contentText = bottomSheetDialog.findViewById(R.id.loyalty_cards_bottom_sheet_dialog_content_text);
        contentText.setText(card.getContent());
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(card.getContent(), BarcodeFormat.valueOf(card.getFormat()), 800, 400);
            barcodeImage.setImageBitmap(bitmap);
        }
        catch(Exception e) {
            Log.d(TAG, "onCardClick: Failed while generating barcode");
        }
        bottomSheetDialog.show();
    }
}