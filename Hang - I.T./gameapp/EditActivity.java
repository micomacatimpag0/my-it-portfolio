package com.example.mobilecomp;

import android.content.Intent;
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

public class EditActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_profile_instruc);

        imageProfile = findViewById(R.id.image_profile);
        btnPickImage = findViewById(R.id.btn_pick_image);
        editName = findViewById(R.id.edit_name);
        editPassword = findViewById(R.id.edit_password);
        btnSave = findViewById(R.id.btn_save);

        username = getIntent().getStringExtra("username");

        btnPickImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String imageUriString = imageUri != null ? imageUri.toString() : "";

            DatabaseReference ref = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                    .getReference("users").child(username);

            ref.child("name").setValue(name);
            ref.child("password").setValue(password);
            ref.child("profileImageUri").setValue(imageUriString);

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}