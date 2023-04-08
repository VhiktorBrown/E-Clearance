package com.theelitedevelopers.e_clearance.utils;

import android.content.Context;
import android.widget.Toast;

public class AppUtils {
    public static void displayToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
