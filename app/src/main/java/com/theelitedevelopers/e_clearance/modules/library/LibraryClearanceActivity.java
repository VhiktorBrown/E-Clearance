package com.theelitedevelopers.e_clearance.modules.library;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.theelitedevelopers.e_clearance.R;
import com.theelitedevelopers.e_clearance.data.local.Constants;
import com.theelitedevelopers.e_clearance.data.models.CompletedUpload;
import com.theelitedevelopers.e_clearance.databinding.ActivityLibraryClearanceBinding;
import com.theelitedevelopers.e_clearance.modules.faculty.FacultyClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.payment.PayStackWebViewActivity;
import com.theelitedevelopers.e_clearance.utils.AppUtils;
import com.theelitedevelopers.e_clearance.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryClearanceActivity extends AppCompatActivity {
    ActivityLibraryClearanceBinding binding;
    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    int index;
    Handler handler;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CompletedUpload completedUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLibraryClearanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // populate layout field
        view_list.add(findViewById(R.id.lyt_pay_for_temporal_card));
        view_list.add(findViewById(R.id.lyt_download_temporal_card));

        // populate view step (circle in left)
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_pay_for_temporal_card)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_ask_to_get_cleared)));

        for (View v : view_list) {
            v.setVisibility(View.GONE);
        }

        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();
        fetchStatusOfUpload(FirebaseAuth.getInstance().getUid());

        binding.btPayForTemporalCard.setOnClickListener(v -> {
            index = 0;
            Intent intent = new Intent(this, PayStackWebViewActivity.class);
            intent.putExtra(Constants.CHECKOUT_AMOUNT, 100000.0);
            startActivityForResult(intent, 1);
        });

        binding.btDownloadTemporalCard.setOnClickListener(v -> {
            index = 1;
            mockHandler(index);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && data != null){
            if(resultCode == RESULT_OK){
                collapseAndContinue(index);
                Toast.makeText(getApplicationContext(), "Payment For Library Card successful.", Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void mockHandler(int index){
        handler = new Handler();
        handler.postDelayed(() -> {
            if(index == 1){
                AppUtils.displayToast(getApplicationContext(), "Library ID Card downloaded successfully");
                markResultClearanceAsCompleted(FirebaseAuth.getInstance().getUid(), true);
            }
        }, 500);
    }

    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.pay_for_temporal_card_title:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.download_temporal_card_title:
                if (success_step >= 1 && current_step != 1) {
                    current_step = 1;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(1));
                }
                break;
        }
    }

    private void collapseAndContinue(int index) {
        ViewAnimation.collapse(view_list.get(index));
        setCheckedStep(index);
        index++;
        current_step = index;
        success_step = index > success_step ? index : success_step;
        ViewAnimation.expand(view_list.get(index));
    }

    private void collapseAll() {
        for (View v : view_list) {
            ViewAnimation.collapse(v);
        }
    }

    private void setCheckedStep(int index) {
        RelativeLayout relative = step_view_list.get(index);
        relative.removeAllViews();
        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.ic_done);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        relative.addView(img);
    }

    private void markResultClearanceAsCompleted(String uid, boolean status){
        Map<String, Object> clearanceProgress = new HashMap<>();
        clearanceProgress.put("completedUploadForLibrary", status);
        // Add a new document with a custom ID
        database.collection(Constants.UPLOADED)
                .document(uid)
                .set(clearanceProgress, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    if(status){
                        Toast.makeText(getApplicationContext(), Constants.LIBRARY_CLEARANCE_COMPLETED, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void fetchStatusOfUpload(String uid){
        database.collection(Constants.UPLOADED)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        completedUpload = documentSnapshot.toObject(CompletedUpload.class);
                        if(completedUpload != null){
                            if(completedUpload.getCompletedUploadForLibrary() != null &&
                                    completedUpload.getCompletedUploadForLibrary()){
                                AppUtils.displayToast(getApplicationContext(), Constants.STAGE_COMPLETED);
                            }else {
                                markResultClearanceAsCompleted(FirebaseAuth.getInstance().getUid(), false);
                                AppUtils.displayToast(getApplicationContext(), Constants.STAGE_INCOMPLETE);
                            }
                        }
                    }else {
                        markResultClearanceAsCompleted(FirebaseAuth.getInstance().getUid(), false);
                    }
                }).addOnFailureListener(e -> {

        });
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}