package com.example.mobilecomp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.*;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView name, studentId, emailRole;
    private Button logoutButton;
    private ImageButton btnFaqs, btnSettings, btnEditProfile;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.profileName);
        studentId = findViewById(R.id.profileId);
        emailRole = findViewById(R.id.profileEmailRole); // You forgot to initialize this in your original code
        logoutButton = findViewById(R.id.btnLogout);
        btnFaqs = findViewById(R.id.achievements);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Retrieve username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        username = prefs.getString("username", null);

        if (username == null) {
            Toast.makeText(this, "User not specified", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Load and display user data
        loadUserProfile();

        // Edit profile
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditActivityUser.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Show achievements
        btnFaqs.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AchievementActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });



        // Logout
        logoutButton.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finishAffinity();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(); // Refresh profile info when returning to this screen
    }

    private void loadUserProfile() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                .getReference("users").child(username);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameStr = snapshot.child("name").getValue(String.class);
                    String studentIdStr = snapshot.child("studentId").getValue(String.class);
                    String roleStr = snapshot.child("role").getValue(String.class);
                    String profileUri = snapshot.child("profileImageUri").getValue(String.class);

                    name.setText(nameStr != null ? nameStr : "N/A");
                    studentId.setText(studentIdStr != null ? studentIdStr : "N/A");
                    emailRole.setText(roleStr != null ? roleStr : "N/A");

                    // Save studentId to SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("UserSession", MODE_PRIVATE).edit();
                    editor.putString("studentId", studentIdStr);
                    editor.apply();

                    if (profileUri != null && !profileUri.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(profileUri)
                                .placeholder(R.drawable.profileholder)
                                .into(profilePic);
                    } else {
                        profilePic.setImageResource(R.drawable.profileholder);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
