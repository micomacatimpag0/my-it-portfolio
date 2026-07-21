package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class InstructorActivity extends AppCompatActivity {

    private ImageButton settingsBtn;
    private CardView manageSectionsBtn, viewScoresBtn, viewHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        settingsBtn = findViewById(R.id.settingsBtn);
        manageSectionsBtn = findViewById(R.id.manageSectionsBtn);
        viewScoresBtn = findViewById(R.id.viewScoresBtn);
        viewHistoryBtn = findViewById(R.id.viewHistoryBtn);
        String instructorUsername = getIntent().getStringExtra("username");



        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorActivity.this, AdminProfileActivity.class);
                intent.putExtra("username", instructorUsername);
                startActivity(intent);
            }
        });

        manageSectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorActivity.this, PickSection.class);
                intent.putExtra("username", instructorUsername);
                startActivity(intent);
            } 
        });

        viewScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorActivity.this, ViewScoreActivity.class);
                intent.putExtra("username", instructorUsername);
                startActivity(intent);
            }
        });


        // Setting up the click listener for View History Card
        viewHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to View History screen
                Intent intent = new Intent(InstructorActivity.this, HistoryActivity.class);
                intent.putExtra("username", instructorUsername);
                startActivity(intent);
            }
        });
    }
}
