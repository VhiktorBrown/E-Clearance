package com.theelitedevelopers.e_clearance.modules.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import com.theelitedevelopers.e_clearance.databinding.ActivityAdminClearanceBinding;
import com.theelitedevelopers.e_clearance.databinding.ActivityResultClearanceBinding;
import com.theelitedevelopers.e_clearance.modules.result_clearance.ResultClearanceActivity;
import com.theelitedevelopers.e_clearance.utils.AppUtils;
import com.theelitedevelopers.e_clearance.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminClearanceActivity extends AppCompatActivity {
    ActivityAdminClearanceBinding binding;
    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    int index;
    Handler handler;
    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    int position = 0;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CompletedUpload completedUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminClearanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // populate layout field
        view_list.add(findViewById(R.id.lyt_school_fees_conf_form));
        view_list.add(findViewById(R.id.lyt_upload_receipts));
        view_list.add(findViewById(R.id.lyt_hod_letter));

        // populate view step (circle in left)
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_school_fees_conf_form)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_upload_receipts)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_hod_letter)));

        for (View v : view_list) {
            v.setVisibility(View.GONE);
        }

        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();
        fetchStatusOfUpload(FirebaseAuth.getInstance().getUid());

        binding.btSchoolFeesConfForm.setOnClickListener(v -> {
            index = 0;
            binding.schoolFeesConfFormProgressBar.setVisibility(View.VISIBLE);
            mockHandler(index);
        });

        binding.uploadReceiptsDetails.setOnClickListener(v -> {
            // initialising intent
            Intent intent = new Intent();

            // setting type to select to be image
            intent.setType("image/*");

            // allowing multiple image to be selected
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
        });

        binding.btUploadReceipts.setOnClickListener(v -> {
            index = 1;
            binding.uploadReceiptsProgressBar.setVisibility(View.VISIBLE);
            mockHandler(index);
        });

        binding.btHodLetter.setOnClickListener(v -> {
            index = 2;
            binding.hodLetterProgressBar.setVisibility(View.VISIBLE);
            mockHandler(index);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                }
                // setting 1st selected image into image switcher
                binding.uploadReceiptsImage.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                binding.uploadReceiptsImage.setImageURI(mArrayUri.get(0));
                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked any Image", Toast.LENGTH_LONG).show();
        }
    }

    private void mockHandler(int index){
        handler = new Handler();
        handler.postDelayed(() -> {
            if(index == 0){
                collapseAndContinue(index);
                AppUtils.displayToast(getApplicationContext(), "School Fees Confirmation form downloaded successfully.");
                binding.schoolFeesConfFormProgressBar.setVisibility(View.GONE);
            }else if(index == 1){
                collapseAndContinue(index);
                AppUtils.displayToast(getApplicationContext(), "School Fees Receipts submitted successfully");
                binding.uploadReceiptsProgressBar.setVisibility(View.GONE);
            }else {
                binding.hodLetterProgressBar.setVisibility(View.GONE);
                AppUtils.displayToast(getApplicationContext(), "HOD's letter downloaded successfully.");
                markResultClearanceAsCompleted(FirebaseAuth.getInstance().getUid(), true);
            }

        }, 500);
    }

    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.school_fees_conf_form_title:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.upload_receipts_title:
                if (success_step >= 1 && current_step != 1) {
                    current_step = 1;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(1));
                }
                break;
            case R.id.hod_letter_title:
                if (success_step >= 2 && current_step != 2) {
                    current_step = 2;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(2));
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
        clearanceProgress.put("completedUploadForAdmin", status);
        // Add a new document with a custom ID
        database.collection(Constants.UPLOADED)
                .document(uid)
                .set(clearanceProgress, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    if(status){
                        Toast.makeText(getApplicationContext(), Constants.ADMIN_CLEARANCE_COMPLETED, Toast.LENGTH_SHORT).show();
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
                            if(completedUpload.getCompletedUploadForAdmin() != null &&
                                    completedUpload.getCompletedUploadForAdmin()){
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