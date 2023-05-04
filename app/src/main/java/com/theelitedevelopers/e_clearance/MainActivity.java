package com.theelitedevelopers.e_clearance;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theelitedevelopers.e_clearance.data.local.Constants;
import com.theelitedevelopers.e_clearance.data.local.SharedPref;
import com.theelitedevelopers.e_clearance.data.models.ClearanceProgress;
import com.theelitedevelopers.e_clearance.databinding.ActivityMainBinding;
import com.theelitedevelopers.e_clearance.modules.admin.AdminClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.alumni.AlumniClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.bursary.BursaryClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.department.DepartmentClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.faculty.FacultyClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.library.LibraryClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.login.LoginActivity;
import com.theelitedevelopers.e_clearance.modules.result_clearance.ResultClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.security.SecurityClearanceActivity;
import com.theelitedevelopers.e_clearance.modules.student_affairs.StudentAffairsActivity;
import com.theelitedevelopers.e_clearance.utils.AppUtils;

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

        binding.regNumberDepartment.setText(SharedPref.getInstance(getApplicationContext()).getString(Constants.REG_NUMBER)+" "+
                SharedPref.getInstance(getApplicationContext()).getString(Constants.DEPARTMENT));
        binding.progressStatusText.setText("Hi "+SharedPref.getInstance(getApplicationContext()).getString(Constants.NAME)+
                "! Let's walk you through your Clearance.");
        binding.logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        });

        binding.resultsLayout.setOnClickListener(v-> {
            startActivity(new Intent(this, ResultClearanceActivity.class));
        });

        binding.departmentalLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedResults() != null
                        && clearanceProgress.getClearedResults()){
                    startActivity(new Intent(this, DepartmentClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.facultyLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedDepartment() != null
                        && clearanceProgress.getClearedDepartment()){
                    startActivity(new Intent(this, FacultyClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.libraryLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedFaculty() != null
                        && clearanceProgress.getClearedFaculty()){
                    startActivity(new Intent(this, LibraryClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.securityLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedLibrary() != null
                        && clearanceProgress.getClearedLibrary()){
                    startActivity(new Intent(this, SecurityClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.studentAffairsLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedSecurity() != null
                        && clearanceProgress.getClearedSecurity()){
                    startActivity(new Intent(this, StudentAffairsActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.alumniLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedStudentAffairs() != null
                        && clearanceProgress.getClearedStudentAffairs()){
                    startActivity(new Intent(this, AlumniClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.adminLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedAlumni() != null
                        && clearanceProgress.getClearedAlumni()){
                    startActivity(new Intent(this, AdminClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });

        binding.bursaryLayout.setOnClickListener(v-> {
            if(clearanceProgress != null){
                if(clearanceProgress.getClearedAdmin() != null
                        && clearanceProgress.getClearedAdmin()){
                    startActivity(new Intent(this, BursaryClearanceActivity.class));
                }else {
                    AppUtils.displayToast(getApplicationContext(), "You have to be approved in the previous stage before you can proceed.");
                }
            }
        });
    }

    private void getClearanceProgressForStudent(String uid){
//        database.collection("clearanceProgress")
//                .document(uid)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        if(task.getResult() != null){
//                            clearanceProgress = task.getResult().toObject(ClearanceProgress.class);
//                            if(clearanceProgress != null){
//                                clearanceProgress.setId(task.getResult().getId());
//                                setProgress(clearanceProgress);
//                            }
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                });

        database.collection("clearanceProgress")
                .document(uid)
                .addSnapshotListener((value, error) -> {
                    if(value != null){
                        clearanceProgress = value.toObject(ClearanceProgress.class);
                        if(clearanceProgress != null){
                            clearanceProgress.setId(value.getId());
                            setProgress(clearanceProgress);
                        }
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
            binding.resultsClearanceStatus.setText(Constants.COMPLETED);
            binding.resultsClearanceStatus.setVisibility(View.VISIBLE);
            binding.resultsClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

            //check if Departmental Clearance has been done
            if(clearanceProgress.getClearedDepartment()){
                binding.departmentalImageDone.setVisibility(View.VISIBLE);
                binding.departmentalLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                binding.departmentalClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                binding.departmentalClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                binding.departmentalIndexTextView.setVisibility(View.GONE);
                binding.departmentalClearanceStatus.setText(Constants.COMPLETED);
                binding.departmentalClearanceLocked.setVisibility(View.GONE);
                binding.departmentalClearanceStatus.setVisibility(View.VISIBLE);
                binding.departmentalClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                if(clearanceProgress.getClearedFaculty()){
                    binding.facultyImageDone.setVisibility(View.VISIBLE);
                    binding.facultyLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                    binding.facultyClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                    binding.facultyClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                    binding.facultyIndexTextView.setVisibility(View.GONE);
                    binding.facultyClearanceStatus.setText(Constants.COMPLETED);
                    binding.facultyClearanceLocked.setVisibility(View.GONE);
                    binding.facultyClearanceStatus.setVisibility(View.VISIBLE);
                    binding.facultyClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                    if(clearanceProgress.getClearedLibrary()){
                        binding.libraryImageDone.setVisibility(View.VISIBLE);
                        binding.libraryLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                        binding.libraryClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                        binding.libraryClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                        binding.libraryIndexTextView.setVisibility(View.GONE);
                        binding.libraryClearanceStatus.setText(Constants.COMPLETED);
                        binding.libraryClearanceLocked.setVisibility(View.GONE);
                        binding.libraryClearanceStatus.setVisibility(View.VISIBLE);
                        binding.libraryClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                        if(clearanceProgress.getClearedSecurity()){
                            binding.securityImageDone.setVisibility(View.VISIBLE);
                            binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                            binding.securityClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                            binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                            binding.securityIndexTextView.setVisibility(View.GONE);
                            binding.securityClearanceStatus.setText(Constants.COMPLETED);
                            binding.securityClearanceLocked.setVisibility(View.GONE);
                            binding.securityClearanceStatus.setVisibility(View.VISIBLE);
                            binding.securityClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                            if(clearanceProgress.getClearedStudentAffairs()){
                                binding.studentAffairsImageDone.setVisibility(View.VISIBLE);
                                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                                binding.studentAffairsIndexTextView.setVisibility(View.GONE);
                                binding.studentAffairsClearanceStatus.setText(Constants.COMPLETED);
                                binding.studentAffairsClearanceLocked.setVisibility(View.GONE);
                                binding.studentAffairsClearanceStatus.setVisibility(View.VISIBLE);
                                binding.studentAffairsClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                                if(clearanceProgress.getClearedAlumni()){
                                    binding.alumniImageDone.setVisibility(View.VISIBLE);
                                    binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                                    binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                                    binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                                    binding.alumniIndexTextView.setVisibility(View.GONE);
                                    binding.alumniClearanceStatus.setText(Constants.COMPLETED);
                                    binding.alumniClearanceLocked.setVisibility(View.GONE);
                                    binding.alumniClearanceStatus.setVisibility(View.VISIBLE);
                                    binding.alumniClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                                    if(clearanceProgress.getClearedAdmin()){
                                        binding.adminImageDone.setVisibility(View.VISIBLE);
                                        binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                                        binding.adminClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                                        binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                                        binding.adminIndexTextView.setVisibility(View.GONE);
                                        binding.adminClearanceStatus.setText(Constants.COMPLETED);
                                        binding.adminClearanceLocked.setVisibility(View.GONE);
                                        binding.adminClearanceStatus.setVisibility(View.VISIBLE);
                                        binding.adminClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));

                                        if(clearanceProgress.getClearedBursary()){
                                            binding.bursaryImageDone.setVisibility(View.VISIBLE);
                                            binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.completed_bg));
                                            binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.completedColor));
                                            binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.completedColor));
                                            binding.bursaryIndexTextView.setVisibility(View.GONE);
                                            binding.bursaryClearanceStatus.setText(Constants.COMPLETED);
                                            binding.bursaryClearanceLocked.setVisibility(View.GONE);
                                            binding.bursaryClearanceStatus.setVisibility(View.VISIBLE);
                                            binding.bursaryClearanceStatus.setTextColor(getResources().getColor(R.color.completedColor));
                                        }else {
                                            lockedLevels(8);
                                        }
                                    }else {
                                        lockedLevels(7);
                                    }
                                }else {
                                    lockedLevels(6);
                                }
                            }else {
                               lockedLevels(5);
                            }
                        }else {
                            lockedLevels(4);
                        }
                    }else {
                        lockedLevels(3);
                    }
                }else {
                    lockedLevels(2);
                }
            }else {
                lockedLevels(1);
            }
        }else {
            lockedLevels(0);
        }
    }

    private void lockedLevels(int currentLevel){
        switch (currentLevel){
            case 0:

                //Result Clearance is ready
                binding.resultsImageDone.setVisibility(View.GONE);
                binding.resultsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.resultsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.resultsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.resultsClearanceStatus.setText("Start");
                binding.resultsClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.resultsClearanceStatus.setVisibility(View.VISIBLE);
                binding.resultsIndexTextView.setVisibility(View.VISIBLE);

                //Department is ready
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

                //Library clearance is locked.
                binding.libraryImageDone.setVisibility(View.GONE);
                binding.libraryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.libraryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.libraryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.libraryClearanceStatus.setVisibility(View.GONE);
                binding.libraryClearanceLocked.setVisibility(View.VISIBLE);
                binding.libraryIndexTextView.setVisibility(View.VISIBLE);

                //Security clearance is locked.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setVisibility(View.GONE);
                binding.securityClearanceLocked.setVisibility(View.VISIBLE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //Security clearance is locked.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setVisibility(View.GONE);
                binding.securityClearanceLocked.setVisibility(View.VISIBLE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //StudentAffairs clearance is locked.
                binding.studentAffairsImageDone.setVisibility(View.GONE);
                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.studentAffairsClearanceStatus.setVisibility(View.GONE);
                binding.studentAffairsClearanceLocked.setVisibility(View.VISIBLE);
                binding.studentAffairsIndexTextView.setVisibility(View.VISIBLE);

                //Alumni clearance is locked.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setVisibility(View.GONE);
                binding.alumniClearanceLocked.setVisibility(View.VISIBLE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 1:
                //Department is ready
                binding.departmentalImageDone.setVisibility(View.GONE);
                binding.departmentalLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.departmentalClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.departmentalClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.departmentalClearanceStatus.setText("Start");
                binding.departmentalClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.departmentalClearanceStatus.setVisibility(View.VISIBLE);
                binding.departmentalClearanceLocked.setVisibility(View.GONE);
                binding.departmentalIndexTextView.setVisibility(View.VISIBLE);

                //Faculty is locked.
                binding.facultyImageDone.setVisibility(View.GONE);
                binding.facultyLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.facultyClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.facultyClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.facultyClearanceStatus.setVisibility(View.GONE);
                binding.facultyClearanceLocked.setVisibility(View.VISIBLE);
                binding.facultyIndexTextView.setVisibility(View.VISIBLE);

                //Library clearance is locked.
                binding.libraryImageDone.setVisibility(View.GONE);
                binding.libraryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.libraryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.libraryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.libraryClearanceStatus.setVisibility(View.GONE);
                binding.libraryClearanceLocked.setVisibility(View.VISIBLE);
                binding.libraryIndexTextView.setVisibility(View.VISIBLE);

                //Security clearance is locked.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setVisibility(View.GONE);
                binding.securityClearanceLocked.setVisibility(View.VISIBLE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //Security clearance is locked.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setVisibility(View.GONE);
                binding.securityClearanceLocked.setVisibility(View.VISIBLE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //StudentAffairs clearance is locked.
                binding.studentAffairsImageDone.setVisibility(View.GONE);
                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.studentAffairsClearanceStatus.setVisibility(View.GONE);
                binding.studentAffairsClearanceLocked.setVisibility(View.VISIBLE);
                binding.studentAffairsIndexTextView.setVisibility(View.VISIBLE);

                //Alumni clearance is locked.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setVisibility(View.GONE);
                binding.alumniClearanceLocked.setVisibility(View.VISIBLE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 2:

                //Faculty is ready.
                binding.facultyImageDone.setVisibility(View.GONE);
                binding.facultyLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.facultyClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.facultyClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.facultyClearanceStatus.setText("Start");
                binding.facultyClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.facultyClearanceStatus.setVisibility(View.VISIBLE);
                binding.facultyClearanceLocked.setVisibility(View.GONE);
                binding.facultyIndexTextView.setVisibility(View.VISIBLE);

                //Library clearance is locked.
                binding.libraryImageDone.setVisibility(View.GONE);
                binding.libraryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.libraryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.libraryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.libraryClearanceStatus.setVisibility(View.GONE);
                binding.libraryClearanceLocked.setVisibility(View.VISIBLE);
                binding.libraryIndexTextView.setVisibility(View.VISIBLE);

                //Security clearance is locked.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setVisibility(View.GONE);
                binding.securityClearanceLocked.setVisibility(View.VISIBLE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //StudentAffairs clearance is locked.
                binding.studentAffairsImageDone.setVisibility(View.GONE);
                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.studentAffairsClearanceStatus.setVisibility(View.GONE);
                binding.studentAffairsClearanceLocked.setVisibility(View.VISIBLE);
                binding.studentAffairsIndexTextView.setVisibility(View.VISIBLE);

                //Alumni clearance is locked.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setVisibility(View.GONE);
                binding.alumniClearanceLocked.setVisibility(View.VISIBLE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 3:

                //Library clearance is ready.
                binding.libraryImageDone.setVisibility(View.GONE);
                binding.libraryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.libraryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.libraryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.libraryClearanceStatus.setText("Start");
                binding.libraryClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.libraryClearanceStatus.setVisibility(View.VISIBLE);
                binding.libraryClearanceLocked.setVisibility(View.GONE);
                binding.libraryIndexTextView.setVisibility(View.VISIBLE);

                //Security clearance is locked.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setVisibility(View.GONE);
                binding.securityClearanceLocked.setVisibility(View.VISIBLE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //StudentAffairs clearance is locked.
                binding.studentAffairsImageDone.setVisibility(View.GONE);
                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.studentAffairsClearanceStatus.setVisibility(View.GONE);
                binding.studentAffairsClearanceLocked.setVisibility(View.VISIBLE);
                binding.studentAffairsIndexTextView.setVisibility(View.VISIBLE);

                //Alumni clearance is locked.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setVisibility(View.GONE);
                binding.alumniClearanceLocked.setVisibility(View.VISIBLE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 4:

                //Security clearance is ready.
                binding.securityImageDone.setVisibility(View.GONE);
                binding.securityLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.securityClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.securityClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.securityClearanceStatus.setText("Start");
                binding.securityClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.securityClearanceStatus.setVisibility(View.VISIBLE);
                binding.securityClearanceLocked.setVisibility(View.GONE);
                binding.securityIndexTextView.setVisibility(View.VISIBLE);

                //StudentAffairs clearance is locked.
                binding.studentAffairsImageDone.setVisibility(View.GONE);
                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.studentAffairsClearanceStatus.setVisibility(View.GONE);
                binding.studentAffairsClearanceLocked.setVisibility(View.VISIBLE);
                binding.studentAffairsIndexTextView.setVisibility(View.VISIBLE);

                //Alumni clearance is locked.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setVisibility(View.GONE);
                binding.alumniClearanceLocked.setVisibility(View.VISIBLE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 5:

                //StudentAffairs clearance is ready.
                binding.studentAffairsImageDone.setVisibility(View.GONE);
                binding.studentAffairsLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.studentAffairsClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.studentAffairsClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.studentAffairsClearanceStatus.setText("Start");
                binding.studentAffairsClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.studentAffairsClearanceStatus.setVisibility(View.VISIBLE);
                binding.studentAffairsClearanceLocked.setVisibility(View.GONE);
                binding.studentAffairsIndexTextView.setVisibility(View.VISIBLE);

                //Alumni clearance is locked.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setVisibility(View.GONE);
                binding.alumniClearanceLocked.setVisibility(View.VISIBLE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 6:

                //Alumni clearance is ready.
                binding.alumniImageDone.setVisibility(View.GONE);
                binding.alumniLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.alumniClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.alumniClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.alumniClearanceStatus.setText("Start");
                binding.alumniClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.alumniClearanceStatus.setVisibility(View.VISIBLE);
                binding.alumniClearanceLocked.setVisibility(View.GONE);
                binding.alumniIndexTextView.setVisibility(View.VISIBLE);

                //Admin clearance is locked.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setVisibility(View.GONE);
                binding.adminClearanceLocked.setVisibility(View.VISIBLE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 7:

                //Admin clearance is ready.
                binding.adminImageDone.setVisibility(View.GONE);
                binding.adminLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.adminClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.adminClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.adminClearanceStatus.setText("Start");
                binding.adminClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.adminClearanceStatus.setVisibility(View.VISIBLE);
                binding.adminClearanceLocked.setVisibility(View.GONE);
                binding.adminIndexTextView.setVisibility(View.VISIBLE);

                //Bursary clearance is locked.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setVisibility(View.GONE);
                binding.bursaryClearanceLocked.setVisibility(View.VISIBLE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;

            case 8:

                //Bursary clearance is ready.
                binding.bursaryImageDone.setVisibility(View.GONE);
                binding.bursaryLayout.setBackground(getResources().getDrawable(R.drawable.start_bg));
                binding.bursaryClearanceText.setTextColor(getResources().getColor(R.color.primary));
                binding.bursaryClearanceDetails.setTextColor(getResources().getColor(R.color.textColor));
                binding.bursaryClearanceStatus.setText("Start");
                binding.bursaryClearanceStatus.setTextColor(getResources().getColor(R.color.startColor));
                binding.bursaryClearanceStatus.setVisibility(View.VISIBLE);
                binding.bursaryClearanceLocked.setVisibility(View.GONE);
                binding.bursaryIndexTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    protected void onRestart() {
        getClearanceProgressForStudent(FirebaseAuth.getInstance().getUid());
        super.onRestart();
    }
}