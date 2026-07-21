package com.example.mobilecomp;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    ImageButton backbutton;
    Button play;

    String quiz;
    String section;
    String subject;
    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame_activity);
        backbutton = findViewById(R.id.back_image_button);
        play = findViewById(R.id.start);

        // just assign here — don't redeclare
        quiz = getIntent().getStringExtra("quiz");
        section = getIntent().getStringExtra("section");
        subject = getIntent().getStringExtra("subject");
        studentId = getIntent().getStringExtra("studentId");

        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        play.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, Gamepage.class);
            intent.putExtra("section", section);
            intent.putExtra("subject", subject);
            intent.putExtra("quiz", quiz);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
            finish();
        });
    }
}
