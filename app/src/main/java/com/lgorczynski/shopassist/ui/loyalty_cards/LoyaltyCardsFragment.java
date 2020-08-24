package com.lgorczynski.shopassist.ui.loyalty_cards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.lgorczynski.shopassist.CaptureActivityPortrait;
import com.lgorczynski.shopassist.MainActivity;
import com.lgorczynski.shopassist.R;
import com.lgorczynski.shopassist.ui.CredentialsSingleton;

import java.util.List;

public class LoyaltyCardsFragment extends Fragment implements LoyaltyCardRecyclerViewAdapter.OnCardClickListener, ShareDialog.ShareDialogListener {

    private static final String TAG = "LoyaltyCardsFragment";
    private static final String LOYALTYCARDS_IMAGE_BASE_URL = CredentialsSingleton.BASE_URL + "loyaltycards/image/";

    private LoyaltyCardsViewModel loyaltyCardsViewModel;

    private RecyclerView recyclerView;
    private LoyaltyCardRecyclerViewAdapter adapter;

    private NavController navController;
    private BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loyaltyCardsViewModel =
                ViewModelProviders.of(this).get(LoyaltyCardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_loyalty_cards, container, false);

        setHasOptionsMenu(true);
        //recycler view z kartami lojalnosciowymi
        recyclerView = root.findViewById(R.id.loyalty_cards_recycler_view);
        loyaltyCardsViewModel.getLoyaltyCardsResponseLiveData().observe(this, loyaltyCards -> {
            adapter = new LoyaltyCardRecyclerViewAdapter(getContext(), loyaltyCards, LoyaltyCardsFragment.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        });

        loyaltyCardsViewModel.getShareResponseLiveData().observe(this, shareResponse -> {
            if(shareResponse.getDetail() != null)
                Toast.makeText(getContext(), shareResponse.getDetail(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Shared successfully", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        searchItem.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    adapter.getFilter().filter(s);
                }
                catch(NullPointerException e) {
                    Log.d(TAG, "onQueryTextChange: Null adapter???");
                }
                return false;
            }
        });
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
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.loyalty_cards_settings_bottom_sheet_dialog);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        final TextView title = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_title);
        title.setText(card.getTitle());

        final LinearLayout share = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_share_layout);
        final LinearLayout edit = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_edit_layout);
        final LinearLayout delete = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_delete_layout);

        share.setOnClickListener(view -> {
            ShareDialog shareDialog = new ShareDialog(this, card.getId());
            shareDialog.show(getActivity().getSupportFragmentManager(), "share_dialog");
        });
        edit.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", card.getId());
            bundle.putString("content", card.getContent());
            bundle.putString("format", card.getFormat());
            bundle.putString("title", card.getTitle());
            bundle.putString("imageUrl", LOYALTYCARDS_IMAGE_BASE_URL + card.getId() + "/");
            navController.navigate(R.id.action_navigation_loyalty_cards_to_loyaltyCardEditFormFragment, bundle);
            bottomSheetDialog.cancel();
        });
        delete.setOnClickListener(view -> {
            ProgressBar progressBar = bottomSheetDialog.findViewById(R.id.loyalty_cards_settings_bottom_sheet_dialog_progress_bar);
            delete.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            loyaltyCardsViewModel.deleteLoyaltyCard(card.getId(), CredentialsSingleton.getInstance().getToken(), bottomSheetDialog);
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onShare(String username, int cardID) {
        bottomSheetDialog.dismiss();
        loyaltyCardsViewModel.shareLoyaltyCard(cardID, username, CredentialsSingleton.getInstance().getToken());
        Toast.makeText(getContext(), "Sending...", Toast.LENGTH_SHORT).show();
    }

}