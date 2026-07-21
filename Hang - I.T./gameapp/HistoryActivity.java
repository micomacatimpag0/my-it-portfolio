package com.example.mobilecomp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.*;

import java.util.*;

public class HistoryActivity extends AppCompatActivity {

    private String instructorUsername;
    private LinearLayout sectionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        instructorUsername = getIntent().getStringExtra("username");
        sectionsLayout = findViewById(R.id.sectionsLayout);

        ImageButton backBtn = findViewById(R.id.back_image_button);
        backBtn.setOnClickListener(v -> onBackPressed());

        fetchInstructorSections();
    }

    private void fetchInstructorSections() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(instructorUsername);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                for (DataSnapshot sectionSnap : snapshot.child("sections").getChildren()) {
                    String sectionName = sectionSnap.getKey();
                    List<String> subjectList = new ArrayList<>();

                    for (DataSnapshot subjectSnap : sectionSnap.getChildren()) {
                        // Add all subjects regardless of their value
                        subjectList.add(subjectSnap.getKey());
                    }

                    addSectionCard(sectionName, subjectList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load assigned sections.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSectionCard(String sectionName, List<String> assignedSubjects) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
        );
        params.setMargins(0, 0, 0, 32);
        cardView.setLayoutParams(params);
        cardView.setRadius(20);
        cardView.setCardElevation(8);
        cardView.setClickable(true);

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setGravity(Gravity.CENTER);
        innerLayout.setPadding(16, 16, 16, 16);
        innerLayout.setBackgroundResource(R.drawable.purple_g); // You can add switch logic here for colors

        TextView sectionTitle = new TextView(this);
        sectionTitle.setText(sectionName);
        sectionTitle.setTextColor(getResources().getColor(android.R.color.white));
        sectionTitle.setTextSize(20);
        sectionTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        innerLayout.addView(sectionTitle);
        cardView.addView(innerLayout);

        cardView.setOnClickListener(v -> showSubjectsDialog(sectionName, assignedSubjects));

        sectionsLayout.addView(cardView);
    }

    private void showSubjectsDialog(String section, List<String> assignedSubjects) {
        DatabaseReference subjectsRef = FirebaseDatabase.getInstance()
                .getReference("sections")
                .child(section)
                .child("subjects");

        subjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                View dialogView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.dialog, null);
                Dialog dialog = new Dialog(HistoryActivity.this);
                dialog.setContentView(dialogView);

                ImageView btnClose = dialogView.findViewById(R.id.btn_close);
                TextView sectionName = dialogView.findViewById(R.id.section_name);
                LinearLayout subjectsContainer = dialogView.findViewById(R.id.subjects_container);

                sectionName.setText("Subjects for " + section);
                btnClose.setOnClickListener(v -> dialog.dismiss());

                // Show all subjects that exist in the section's subjects node
                for (DataSnapshot subjectSnap : snapshot.getChildren()) {
                    String subjectName = subjectSnap.getKey();

                    // Check if this subject is assigned to the instructor
                    if (assignedSubjects.contains(subjectName)) {
                        Button subjectBtn = new Button(HistoryActivity.this);
                        subjectBtn.setText(subjectName);
                        subjectBtn.setAllCaps(false);

                        subjectBtn.setOnClickListener(v -> {
                            dialog.dismiss();
                            showQuizzesDialog(section, subjectName, subjectSnap);
                        });

                        subjectsContainer.addView(subjectBtn);
                    }
                }

                if (subjectsContainer.getChildCount() == 0) {
                    TextView noSubjects = new TextView(HistoryActivity.this);
                    noSubjects.setText("No subjects assigned");
                    noSubjects.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    subjectsContainer.addView(noSubjects);
                }

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load subjects.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuizzesDialog(String section, String subject, DataSnapshot subjectSnap) {
        View dialogView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.dialog, null);
        Dialog dialog = new Dialog(HistoryActivity.this);
        dialog.setContentView(dialogView);

        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        TextView sectionName = dialogView.findViewById(R.id.section_name);
        LinearLayout subjectsContainer = dialogView.findViewById(R.id.subjects_container);

        sectionName.setText(subject + " Quizzes");
        btnClose.setOnClickListener(v -> dialog.dismiss());

        for (DataSnapshot quizSnap : subjectSnap.getChildren()) {
            String quizName = quizSnap.getKey();

            Button quizBtn = new Button(HistoryActivity.this);
            quizBtn.setText(quizName);
            quizBtn.setAllCaps(false);

            quizBtn.setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(HistoryActivity.this, Editcards.class);
                intent.putExtra("section", section);
                intent.putExtra("subject", subject);
                intent.putExtra("quiz", quizName);
                startActivity(intent);
            });

            subjectsContainer.addView(quizBtn);
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
