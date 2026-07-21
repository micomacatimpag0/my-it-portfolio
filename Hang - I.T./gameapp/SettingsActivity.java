package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout helpLayout, aboutLayout;
    ImageButton backButton;
    TextView helpText, aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        helpLayout = findViewById(R.id.setting_help);
        aboutLayout = findViewById(R.id.setting_about);
       backButton = findViewById(R.id.back_image_button);
        helpText = findViewById(R.id.text_help);
        aboutText = findViewById(R.id.text_about);


        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AdminProfileActivity.class);
            startActivity(intent);
            finish();
        });



        helpLayout.setOnClickListener(v -> {

        });


        aboutLayout.setOnClickListener(v -> {

        });
    }
}
