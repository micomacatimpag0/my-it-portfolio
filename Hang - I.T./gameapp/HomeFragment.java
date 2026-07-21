package com.example.mobilecomp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.*;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("sections");

        CardView card1A = view.findViewById(R.id.bsit1a);
        CardView card1B = view.findViewById(R.id.bsit1b);
        CardView card1C = view.findViewById(R.id.bsit1c);
        CardView card1D = view.findViewById(R.id.bsit1d);

        card1A.setOnClickListener(v -> loadSubjects("BSIT1A"));
        card1B.setOnClickListener(v -> loadSubjects("BSIT1B"));
        card1C.setOnClickListener(v -> loadSubjects("BSIT1C"));
        card1D.setOnClickListener(v -> loadSubjects("BSIT1D"));
    }

    private void loadSubjects(String section) {
        databaseReference.child(section).child("subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    showSubjectsDialog(section, snapshot);
                } else {
                    showNoActivityDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load subjects.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSubjectsDialog(String section, DataSnapshot subjectsSnapshot) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogView);

        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        TextView sectionName = dialogView.findViewById(R.id.section_name);
        LinearLayout subjectsContainer = dialogView.findViewById(R.id.subjects_container);

        sectionName.setText("Subjects for " + section);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        for (DataSnapshot subjectSnap : subjectsSnapshot.getChildren()) {
            String subjectName = subjectSnap.getKey();

            Button subjectBtn = new Button(getContext());
            subjectBtn.setText(subjectName);
            subjectBtn.setAllCaps(false);

            subjectBtn.setOnClickListener(v -> {
                dialog.dismiss();
                showQuizzesDialog(section, subjectName, subjectSnap);
            });

            subjectsContainer.addView(subjectBtn);
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showQuizzesDialog(String section, String subject, DataSnapshot subjectSnap) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogView);

        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        TextView sectionName = dialogView.findViewById(R.id.section_name);
        LinearLayout subjectsContainer = dialogView.findViewById(R.id.subjects_container);

        sectionName.setText(subject + " Quizzes");
        btnClose.setOnClickListener(v -> dialog.dismiss());

        for (DataSnapshot quizSnap : subjectSnap.getChildren()) {
            String quizName = quizSnap.getKey();

            Button quizBtn = new Button(getContext());
            quizBtn.setText(quizName);
            quizBtn.setAllCaps(false);

            quizBtn.setOnClickListener(v -> {
                dialog.dismiss();
                showPasswordDialog(section, subject, quizName, quizSnap);
            });

            subjectsContainer.addView(quizBtn);
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showPasswordDialog(String section, String subject, String quiz, DataSnapshot quizSnap) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pass, null);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogView);

        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String inputPassword = editPassword.getText().toString();
            String correctPassword = quizSnap.child("password").getValue(String.class);

            if (inputPassword.equals(correctPassword)) {
                dialog.dismiss();

                // Get studentId
                String studentId = requireActivity()
                        .getSharedPreferences("UserSession", getContext().MODE_PRIVATE)
                        .getString("username", "");

                // Check if record exists
                DatabaseReference recordsRef = FirebaseDatabase.getInstance()
                        .getReference("records");

                recordsRef.orderByChild("studentId").equalTo(studentId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean alreadyTaken = false;

                                for (DataSnapshot recordSnap : snapshot.getChildren()) {
                                    String recordSection = recordSnap.child("section").getValue(String.class);
                                    String recordSubject = recordSnap.child("subject").getValue(String.class);
                                    String recordQuiz = recordSnap.child("quiz").getValue(String.class);

                                    if (section.equals(recordSection) && subject.equals(recordSubject) && quiz.equals(recordQuiz)) {
                                        alreadyTaken = true;
                                        break;
                                    }
                                }

                                if (alreadyTaken) {
                                    Toast.makeText(getContext(), "You have already taken this quiz.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Allow access
                                    Toast.makeText(getContext(), "Access granted!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getContext(), StartActivity.class);
                                    intent.putExtra("section", section);
                                    intent.putExtra("subject", subject);
                                    intent.putExtra("quiz", quiz);
                                    intent.putExtra("studentId", studentId);

                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Failed to check quiz attempt: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    private void showNoActivityDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.no_activity_dialog, null);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(dialogView);

        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
