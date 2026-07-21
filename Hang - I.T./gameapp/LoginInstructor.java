package com.example.mobilecomp;

import android.text.InputType;
import android.widget.ImageView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginInstructor extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private ImageView passwordToggle;
    private boolean isPasswordVisible = false;


    private DatabaseReference databaseReference; // Firebase ref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_instructor);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        databaseReference = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/").getReference("users");

        passwordToggle = findViewById(R.id.password_toggle);

        passwordToggle.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Hide password
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordToggle.setImageResource(R.drawable.hidden); // your closed-eye icon
                isPasswordVisible = false;
            } else {
                // Show password
                passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordToggle.setImageResource(R.drawable.eye); // your open-eye icon
                isPasswordVisible = true;
            }
            passwordInput.setSelection(passwordInput.length()); // move cursor to end
        });


        loginButton.setOnClickListener(view -> {
            String username = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginInstructor.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Now check inside database
            databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String dbPassword = snapshot.child("password").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);
                        String studentId = snapshot.child("studentId").getValue(String.class);

                        // Check if the user is an admin based on studentId
                        if ("admin".equals(studentId)) {
                            Toast.makeText(LoginInstructor.this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                            // Go to Superadmin Activity
                            startActivity(new Intent(LoginInstructor.this, Superadmin.class));
                            finish();
                        } else if (dbPassword != null && dbPassword.equals(password)) {
                            if ("instructor".equals(role)) {
                                Toast.makeText(LoginInstructor.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginInstructor.this, InstructorActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginInstructor.this, "Invalid role", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginInstructor.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginInstructor.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(LoginInstructor.this, "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}