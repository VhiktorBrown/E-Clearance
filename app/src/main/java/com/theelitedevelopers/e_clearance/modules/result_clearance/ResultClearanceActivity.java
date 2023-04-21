package com.theelitedevelopers.e_clearance.modules.result_clearance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.theelitedevelopers.e_clearance.R;
import com.theelitedevelopers.e_clearance.data.local.Constants;
import com.theelitedevelopers.e_clearance.data.models.CompletedUpload;
import com.theelitedevelopers.e_clearance.databinding.ActivityResultClearanceBinding;
import com.theelitedevelopers.e_clearance.utils.AppUtils;
import com.theelitedevelopers.e_clearance.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultClearanceActivity extends AppCompatActivity {

    ActivityResultClearanceBinding binding;
    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    int index;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Handler handler;
    CompletedUpload completedUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultClearanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // populate layout field
        view_list.add(findViewById(R.id.lyt_clearance_form));
        view_list.add(findViewById(R.id.lyt_result_status));

        // populate view step (circle in left)
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_clearance_form)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_result_status)));

        for (View v : view_list) {
            v.setVisibility(View.GONE);
        }

        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();

        fetchStatusOfUpload(FirebaseAuth.getInstance().getUid());

        binding.btClearanceForm.setOnClickListener(v -> {
            index = 0;
            binding.clearanceFormProgressBar.setVisibility(View.VISIBLE);
            mockHandler(index);
        });

        binding.btResultStatus.setOnClickListener(v -> {
            index = 1;
            binding.resultStatusProgressBar.setVisibility(View.VISIBLE);
            mockHandler(index);
        });
    }

    private void mockHandler(int index){
        handler = new Handler();
        handler.postDelayed(() -> {
            if(index == 0){
                Toast.makeText(ResultClearanceActivity.this, "Clearance Form downloaded successfully.", Toast.LENGTH_SHORT).show();
                binding.clearanceFormProgressBar.setVisibility(View.GONE);
                collapseAndContinue(index);
            }else {
                markResultClearanceAsCompleted(FirebaseAuth.getInstance().getUid(), true);
                Toast.makeText(ResultClearanceActivity.this, "Check back for your result status in the next 3 business days.", Toast.LENGTH_SHORT).show();
                binding.resultStatusProgressBar.setVisibility(View.GONE);
            }

        }, 500);
    }

    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.clearance_form_title:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.result_status_title:
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
        clearanceProgress.put("completedUploadForResult", status);
        // Add a new document with a custom ID
        database.collection(Constants.UPLOADED)
                .document(uid)
                .set(clearanceProgress, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    if(status){
                        Toast.makeText(getApplicationContext(), Constants.RESULT_CLEARANCE_COMPLETED, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void fetchStatusOfUpload(String uid){
        database.collection("uploaded")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        completedUpload = documentSnapshot.toObject(CompletedUpload.class);
                        if(completedUpload != null){
                            if(completedUpload.getCompletedUploadForResult() != null &&
                                    completedUpload.getCompletedUploadForResult()){
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