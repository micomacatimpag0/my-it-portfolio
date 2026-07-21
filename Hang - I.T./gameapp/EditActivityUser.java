package com.example.mobilecomp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivityUser extends AppCompatActivity {

    private ImageView imageProfile;
    private ImageButton btnPickImage;
    private EditText editName, editPassword;
    private Button btnSave;

    private Uri imageUri = null;
    private String username;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    Glide.with(this)
                            .load(uri)
                            .placeholder(R.drawable.profileholder)
                            .into(imageProfile);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageProfile = findViewById(R.id.image_profile);
        btnPickImage = findViewById(R.id.btn_pick_image);
        editName = findViewById(R.id.edit_name);
        editPassword = findViewById(R.id.edit_password);
        btnSave = findViewById(R.id.btn_save);

        username = getIntent().getStringExtra("username");
        if (username == null) {
            Toast.makeText(this, "No user to edit", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserProfile();

        btnPickImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the imageUri as string to Realtime DB, or empty string if null
            String imageUriString = (imageUri != null) ? imageUri.toString() : "";

            DatabaseReference ref = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                    .getReference("users").child(username);

            ref.child("fullname").setValue(name);
            ref.child("password").setValue(password);
            ref.child("profileImageUri").setValue(imageUriString);

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadUserProfile() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                .getReference("users").child(username);

        ref.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String nameStr = snapshot.child("fullname").getValue(String.class);
                String passwordStr = snapshot.child("password").getValue(String.class);
                String profileUri = snapshot.child("profileImageUri").getValue(String.class);

                editName.setText(nameStr != null ? nameStr : "");
                editPassword.setText(passwordStr != null ? passwordStr : "");

                if (profileUri != null && !profileUri.isEmpty()) {
                    imageUri = Uri.parse(profileUri);
                    Glide.with(EditActivityUser.this)
                            .load(imageUri)
                            .placeholder(R.drawable.profileholder)
                            .into(imageProfile);
                } else {
                    imageProfile.setImageResource(R.drawable.profileholder);
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(EditActivityUser.this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }
}