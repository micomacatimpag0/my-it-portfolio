package com.example.mobilecomp;

import android.content.Intent;
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

public class AdminProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView name, studentId;
    private Button logoutButton;
    private ImageButton btnFaqs, btnSettings, btnEditProfile;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminprofile);

        ImageButton backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> finish());
        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.textViewName);
        studentId = findViewById(R.id.textViewId);
        logoutButton = findViewById(R.id.btnLogout);


        btnEditProfile = findViewById(R.id.btnEditProfile);

        username = getIntent().getStringExtra("username");

        loadUserProfile(); // Initial load

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AdminProfileActivity.this, EditActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(AdminProfileActivity.this, LoginInstructor.class));
            finishAffinity();
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile(); // Reload profile when coming back
    }

    private void loadUserProfile() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                .getReference("users").child(username);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name.setText(snapshot.child("name").getValue(String.class));
                    studentId.setText(snapshot.child("studentId").getValue(String.class));
                    String profileUri = snapshot.child("profileImageUri").getValue(String.class);
                    if (profileUri != null && !profileUri.isEmpty()) {
                        Glide.with(AdminProfileActivity.this).load(Uri.parse(profileUri)).into(profilePic);
                    } else {
                        profilePic.setImageResource(R.drawable.profileholder);
                    }
                } else {
                    Toast.makeText(AdminProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}