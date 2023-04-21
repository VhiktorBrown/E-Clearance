package com.theelitedevelopers.e_clearance.utils;

import android.content.Context;
import android.widget.Toast;

import com.theelitedevelopers.e_clearance.data.local.Constants;
import com.theelitedevelopers.e_clearance.data.local.SharedPref;
import com.theelitedevelopers.e_clearance.data.models.Student;

public class AppUtils {
    public static void displayToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void saveData(Context context, Student student){
        SharedPref.getInstance(context).saveString(Constants.ID, student.getId());
        SharedPref.getInstance(context).saveString(Constants.UID, student.getUid());
        SharedPref.getInstance(context).saveString(Constants.NAME, student.getName());
        SharedPref.getInstance(context).saveString(Constants.EMAIL, student.getEmail());
        SharedPref.getInstance(context).saveString(Constants.REG_NUMBER, student.getRegNumber());
        SharedPref.getInstance(context).saveString(Constants.DEPARTMENT, student.getDepartment());
    }
}
