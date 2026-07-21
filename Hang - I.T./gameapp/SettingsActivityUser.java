package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivityUser extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_user);


        ImageButton backBtn = findViewById(R.id.back_image_button);
        backBtn.setOnClickListener(v -> {
            finish(); // Simply go back to previous activity (MainActivity -> ProfileFragment)
        });
    }
}
