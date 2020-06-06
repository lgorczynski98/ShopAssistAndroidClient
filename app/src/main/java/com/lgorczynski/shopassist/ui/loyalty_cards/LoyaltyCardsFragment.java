package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.google.android.gms.vision.text.Line;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.CaptureActivityPortrait;
import com.lgorczynski.shopassist.ui.log_in.CredentialsSingleton;

import java.util.List;

public class LoyaltyCardsFragment extends Fragment implements LoyaltyCardRecyclerViewAdapter.OnCardClickListener, View.OnClickListener{

    private static final String TAG = "LoyaltyCardsFragment";
    private static final String LOYALTYCARDS_IMAGE_BASE_URL = CredentialsSingleton.BASE_URL + "loyaltycards/image/";

    private LoyaltyCardsViewModel loyaltyCardsViewModel;

    private RecyclerView recyclerView;
    private LoyaltyCardRecyclerViewAdapter adapter;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loyaltyCardsViewModel =
                ViewModelProviders.of(this).get(LoyaltyCardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loyalty_cards, container, false);

        //recycler view z kartami lojalnosciowymi
        recyclerView = root.findViewById(R.id.loyalty_cards_recycler_view);
        loyaltyCardsViewModel.getLoyaltyCardsResponseLiveData().observe(this, new Observer<List<LoyaltyCard>>() {
            @Override
            public void onChanged(List<LoyaltyCard> loyaltyCards) {
                adapter = new LoyaltyCardRecyclerViewAdapter(getContext(), loyaltyCards, LoyaltyCardsFragment.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        loyaltyCardsViewModel.getLoyaltyCards(CredentialsSingleton.getInstance().getToken());

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
        WindowManager.LayoutParams windowParams = getActivity().getWindow().getAttributes();
        final float brightness = windowParams.screenBrightness;
        windowParams.screenBrightness = 1.0f;
        getActivity().getWindow().setAttributes(windowParams);
        LoyaltyCard card = adapter.getItemOnPosition(position);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.loyalty_cards_bottom_sheet_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                windowParams.screenBrightness = brightness;
                getActivity().getWindow().setAttributes(windowParams);
            }
        });
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

    @Override
    public void onSettingClick(int position) {
        LoyaltyCard card = adapter.getItemOnPosition(position);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.loyalty_cards_settings_bottom_sheet_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        final TextView title = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_title);
        title.setText(card.getTitle());

        final LinearLayout share = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_share_layout);
        final LinearLayout edit = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_edit_layout);
        final LinearLayout delete = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_delete_layout);

        share.setOnClickListener(this);
        edit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("content", card.getContent());
            bundle.putString("format", card.getFormat());
            bundle.putString("title", card.getTitle());
            bundle.putString("imageUrl", LOYALTYCARDS_IMAGE_BASE_URL + card.getId() + "/");
            navController.navigate(R.id.action_navigation_loyalty_cards_to_loyaltyCardEditFormFragment, bundle);
            bottomSheetDialog.cancel();
        });
        delete.setOnClickListener(this);

        bottomSheetDialog.show();
    }

    //setting buttons
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loyalty_cards_settings_bottom_sheet_dialog_share_layout:{
                Toast.makeText(getContext(), "Clicked on share", Toast.LENGTH_SHORT).show();

                break;
            }
            case R.id.loyalty_cards_settings_bottom_sheet_dialog_delete_layout:{
                Toast.makeText(getContext(), "Clicked on delete", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}