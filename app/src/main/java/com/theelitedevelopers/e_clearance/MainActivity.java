package com.theelitedevelopers.e_clearance;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theelitedevelopers.e_clearance.data.models.ClearanceProgress;
import com.theelitedevelopers.e_clearance.databinding.ActivityMainBinding;
import com.theelitedevelopers.e_clearance.modules.admin.AdminClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.alumni.AlumniClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.bursary.BursaryClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.department.DepartmentClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.faculty.FacultyClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.library.LibraryClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.result_clearance.ResultClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.security.SecurityClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.student_affairs.StudentAffairsActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    ActivityMainBinding  binding;
    ClearanceProgress clearanceProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            getClearanceProgressForStudent(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        binding.resultsLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, ResultClearanceActivity.class));
        });

        binding.departmentalLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, DepartmentClearanceActivity.class));
        });

        binding.facultyLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, FacultyClearanceActivity.class));
        });

        binding.libraryLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, LibraryClearanceActivity.class));
        });

        binding.securityLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, SecurityClearanceActivity.class));
        });

        binding.studentAffairsLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, StudentAffairsActivity.class));
        });

        binding.alumniLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, AlumniClearanceActivity.class));
        });

        binding.adminLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, AdminClearanceActivity.class));
        });

        binding.bursaryLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, BursaryClearanceActivity.class));
        });
    }

    private void getClearanceProgressForStudent(String uid){
        database.collection("clearanceProgress")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        clearanceProgress = task.getResult().getDocuments().get(0).toObject(ClearanceProgress.class);
                        if(clearanceProgress != null){
                            clearanceProgress.setId(task.getResult().getDocuments().get(0).getId());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setProgress(ClearanceProgress clearanceProgress){
        if(clearanceProgress.getClearedResults()){
            binding.resultsImageDone.setVisibility(View.VISIBLE);
            binding.resultsLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
            binding.resultsClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
            binding.resultsClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
            binding.resultsIndexTextView.setVisibility(View.GONE);

            //check if Departmental Clearance has been done
            if(clearanceProgress.getClearedDepartment()){
                binding.departmentalImageDone.setVisibility(View.VISIBLE);
                binding.departmentalLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                binding.departmentalClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                binding.departmentalClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                binding.departmentalIndexTextView.setVisibility(View.GONE);
            }else {

            }
        }else {
            binding.resultsImageDone.setVisibility(View.GONE);
            binding.resultsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
            binding.resultsClearanceText.setTextColor(getResources().getColor(R.color.primary));
            binding.resultsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
            binding.resultsClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
            binding.resultsIndexTextView.setVisibility(View.VISIBLE);

            //show that the other levels are locked

            //Department is locked.
            binding.departmentalImageDone.setVisibility(View.GONE);
            binding.departmentalLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
            binding.departmentalClearanceText.setTextColor(getResources().getColor(R.color.primary));
            binding.departmentalClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
            binding.departmentalClearanceStatus.setVisibility(View.GONE);
            binding.departmentalClearanceLocked.setVisibility(View.VISIBLE);
            binding.departmentalIndexTextView.setVisibility(View.VISIBLE);

            //Faculty is locked.
            binding.facultyImageDone.setVisibility(View.GONE);
            binding.facultyLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
            binding.facultyClearanceText.setTextColor(getResources().getColor(R.color.primary));
            binding.facultyClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
            binding.facultyClearanceStatus.setVisibility(View.GONE);
            binding.facultyClearanceLocked.setVisibility(View.VISIBLE);
            binding.facultyIndexTextView.setVisibility(View.VISIBLE);
        }
    }
}