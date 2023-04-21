package com.theelitedevelopers.e_clearance.modules.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.theelitedevelopers.e_clearance.MainActivity;
import com.theelitedevelopers.e_clearance.data.models.ClearanceProgress;
import com.theelitedevelopers.e_clearance.data.models.Student;
import com.theelitedevelopers.e_clearance.databinding.ActivityLoginBinding;
import com.theelitedevelopers.e_clearance.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String firebaseToken;
    Student student;
    ClearanceProgress clearanceProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logInButton.setOnClickListener(v -> {

            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();

            if(email.length() > 0 && password.length() > 0){
                binding.progressBar.setVisibility(View.VISIBLE);

                Student student = new Student();
                student.setEmail(email);
                student.setPassword(password);

                loginStudent(student);
            }
        });

    }

    private void loginStudent(Student student){

        firebaseAuth.signInWithEmailAndPassword(student.getEmail(), student.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //fetch Clearance Details from Database
                            getClearanceProgressForStudent(student, user.getUid());

                        } else {
                            binding.progressBar.setVisibility(View.GONE);

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveProgressIfDoesNotExist(String uid){

        Map<String, Object> clearanceProgress = new HashMap<>();
        clearanceProgress.put("clearedResults", false);
        clearanceProgress.put("clearedDepartment", false);
        clearanceProgress.put("clearedFaculty", false);
        clearanceProgress.put("clearedLibrary", false);
        clearanceProgress.put("clearedSecurity", false);
        clearanceProgress.put("clearedStudentAffairs", false);
        clearanceProgress.put("clearedAlumni", false);
        clearanceProgress.put("clearedAdmin", false);
        clearanceProgress.put("clearedBursary", false);

        // Add a new document with a generated ID
        database.collection("clearanceProgress")
                .document(uid)
                .set(clearanceProgress, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Added New Student Data to Database.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                });
    }


    private void getClearanceProgressForStudent(Student student, String uid){
        getStudent(uid);
        database.collection("clearanceProgress")
                .document( uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        if(task.getResult() != null){
                            clearanceProgress = task.getResult().toObject(ClearanceProgress.class);
                            if(clearanceProgress != null){
                                clearanceProgress.setId(task.getResult().getId());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }else {
                            saveProgressIfDoesNotExist(uid);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void getStudent(String uid){
        database.collection("students")
                .document( uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult() != null){
                            student = task.getResult().toObject(Student.class);
                            if(student != null){
                                student.setId(task.getResult().getId());
                                AppUtils.saveData(LoginActivity.this, student);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


}