package com.theelitedevelopers.e_clearance.modules.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.theelitedevelopers.e_clearance.R;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        //Library
        //Security
        // Student Affairs - CHeck for NYSC temporal ID card - Pay 1,000
        // Alumni - Pay 5,000 for sticker, etc
        //Bursary - Collect form to confirm school fees payment
        //Admin - Confirm school fees receipt at Admin - Give letter to take to HOD in department. Sign letter
        //Take school fees, convocation receipt to Bursary
    }
}