package com.theelitedevelopers.e_clearance.modules.security;

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

import com.theelitedevelopers.e_clearance.R;
import com.theelitedevelopers.e_clearance.data.local.Constants;
import com.theelitedevelopers.e_clearance.databinding.ActivityLibraryClearanceBinding;
import com.theelitedevelopers.e_clearance.databinding.ActivitySecurityClearanceBinding;
import com.theelitedevelopers.e_clearance.modules.payment.PayStackWebViewActivity;
import com.theelitedevelopers.e_clearance.utils.AppUtils;
import com.theelitedevelopers.e_clearance.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.List;

public class SecurityClearanceActivity extends AppCompatActivity {
    ActivitySecurityClearanceBinding binding;
    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    int index;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityClearanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // populate layout field
        view_list.add(findViewById(R.id.lyt_pay_security_fee));
        view_list.add(findViewById(R.id.lyt_ask_to_get_cleared));

        // populate view step (circle in left)
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_pay_security_fee)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_ask_to_get_cleared)));

        for (View v : view_list) {
            v.setVisibility(View.GONE);
        }

        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();

        binding.btPaySecurityFee.setOnClickListener(v -> {
            index = 0;
            Intent intent = new Intent(this, PayStackWebViewActivity.class);
            intent.putExtra(Constants.CHECKOUT_AMOUNT, 300000.0);
            startActivityForResult(intent, 1);
        });

        binding.btAskToGetCleared.setOnClickListener(v -> {
            index = 1;
            mockHandler(index);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && data != null){
            if(resultCode == RESULT_OK){
                collapseAndContinue(index);
                Toast.makeText(getApplicationContext(), "Payment For Security Fee successful.", Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void mockHandler(int index){
        handler = new Handler();
        handler.postDelayed(() -> {
            if(index == 1){
                AppUtils.displayToast(getApplicationContext(), "If everything checks out in the Security department, you'll be cleared. Check back in 3 business days.");
            }
        }, 500);
    }

    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.pay_security_fee_title:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.ask_to_get_cleared_title:
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

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}