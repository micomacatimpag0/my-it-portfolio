package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullnameInput, StudentIdSignup, passwordSignup, confirmPassword;
    private Button signupButton;
    private TextView loginRedirect;

    private DatabaseReference databaseReference; // Firebase DB reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullnameInput = findViewById(R.id.fullnameInput);
        StudentIdSignup = findViewById(R.id.StudentIdSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);
        loginRedirect = findViewById(R.id.loginRedirect);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/").getReference("users");

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullnameInput.getText().toString().trim();
                String studentId = StudentIdSignup.getText().toString().trim();
                String password = passwordSignup.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (fullName.isEmpty() || studentId.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPass)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if student ID already exists
                databaseReference.child(studentId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Toast.makeText(SignUpActivity.this, "Student ID already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Proceed with account creation
                            User user = new User(fullName, studentId, password, "student", null);


                            databaseReference.child(studentId).setValue(user)
                                    .addOnCompleteListener(saveTask -> {
                                        if (saveTask.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to access database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
