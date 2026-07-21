package com.example.mobilecomp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewScoreActivity extends AppCompatActivity {

    private RecyclerView scoresRecyclerView;
    private ScoreAdapter scoresAdapter;
    private List<StudentScore> scoreList;

    private String instructorUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewscore);

        instructorUsername = getIntent().getStringExtra("username");

        scoresRecyclerView = findViewById(R.id.scoresRecyclerView);
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        scoreList = new ArrayList<>();
        scoresAdapter = new ScoreAdapter(scoreList);
        scoresRecyclerView.setAdapter(scoresAdapter);

        ImageButton backBtn = findViewById(R.id.back_image_button);
        backBtn.setOnClickListener(v -> finish());

        fetchScoresForInstructor();
    }

    private void fetchScoresForInstructor() {
        DatabaseReference recordsRef = FirebaseDatabase.getInstance().getReference("records");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        recordsRef.orderByChild("instructor").equalTo(instructorUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot recordsSnapshot) {
                        scoreList.clear();

                        for (DataSnapshot recordSnap : recordsSnapshot.getChildren()) {
                            String studentId = recordSnap.child("studentId").getValue(String.class);
                            Integer score = recordSnap.child("score").getValue(Integer.class);

                            if (studentId != null && score != null) {
                                usersRef.child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        String fullName = userSnapshot.child("fullname").getValue(String.class);
                                        if (fullName != null) {
                                            scoreList.add(new StudentScore(fullName, score));
                                            scoresAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(ViewScoreActivity.this, "Failed to load student name.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        if (recordsSnapshot.getChildrenCount() == 0) {
                            Toast.makeText(ViewScoreActivity.this, "No scores found for this instructor.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewScoreActivity.this, "Failed to load scores.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
