package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Chooserole extends AppCompatActivity {

    private Button buttonInstructor, buttonStudent;  // better names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chooserole);

        buttonStudent = findViewById(R.id.studentButton);
        buttonInstructor = findViewById(R.id.instructorButton);

        // Example: You can now add click listeners
        buttonStudent.setOnClickListener(v -> {
            startActivity(new Intent(Chooserole.this, LoginActivity.class));
        });

        buttonInstructor.setOnClickListener(v -> {
            startActivity(new Intent(Chooserole.this, LoginInstructor.class));
        });
    }
}
