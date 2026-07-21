package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Addsection extends AppCompatActivity {

    private ImageButton back;
    private Button btnAddSection, btnDeleteSection, btnAddSubject, btnEditSubject, btnDeleteSubject;
    private EditText sectionNameInput;
    private EditText subjectEditInput;  // For subject editing
    private LinearLayout sectionListContainer;
    private DatabaseReference sectionsRef;
    private String selectedSectionName = null;
    private String selectedSubjectName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsection);

        back = findViewById(R.id.btnBack);


        // Subject buttons
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnEditSubject = findViewById(R.id.btnEditSubject);
        btnDeleteSubject = findViewById(R.id.btnDeleteSubject);
        sectionNameInput = findViewById(R.id.sectionNameInput);


        subjectEditInput = findViewById(R.id.subjectedit);
        sectionListContainer = findViewById(R.id.sectionListContainer);

        sectionsRef = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                .getReference("sections");

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Addsection.this, Superadmin.class);
            startActivity(intent);
            finish();
        });



        // Subject buttons
        btnAddSubject.setOnClickListener(v -> addSubject());
        btnEditSubject.setOnClickListener(v -> editSubject());
        btnDeleteSubject.setOnClickListener(v -> deleteSubject());

        loadExistingSections();
    }

    private void loadExistingSections() {
        sectionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                sectionListContainer.removeAllViews(); // clear old list

                for (DataSnapshot sectionSnap : snapshot.getChildren()) {
                    String sectionName = sectionSnap.getKey();

                    LinearLayout sectionLayout = new LinearLayout(Addsection.this);
                    sectionLayout.setOrientation(LinearLayout.VERTICAL);
                    sectionLayout.setPadding(0, 16, 0, 16);

                    TextView sectionView = new TextView(Addsection.this);
                    sectionView.setText(sectionName + " ▼");
                    sectionView.setTextSize(18);
                    sectionView.setPadding(24, 16, 24, 16);
                    sectionView.setBackgroundResource(R.drawable.spinner_bg);
                    sectionView.setTextColor(getResources().getColor(android.R.color.black));

                    LinearLayout subjectsLayout = new LinearLayout(Addsection.this);
                    subjectsLayout.setOrientation(LinearLayout.VERTICAL);
                    subjectsLayout.setVisibility(View.GONE);

                    DataSnapshot subjectsSnapshot = sectionSnap.child("subjects");
                    if (subjectsSnapshot.exists()) {
                        for (DataSnapshot subjectSnap : subjectsSnapshot.getChildren()) {
                            String subjectName = subjectSnap.getKey();

                            TextView subjectView = new TextView(Addsection.this);
                            subjectView.setText("• " + subjectName);
                            subjectView.setTextSize(14);
                            subjectView.setPadding(48, 8, 24, 8);
                            subjectView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            subjectView.setClickable(true);
                            subjectView.setOnClickListener(v -> {
                                // Populate subjectEditInput and select subject
                                subjectEditInput.setText(subjectName);
                                selectedSubjectName = subjectName;
                            });

                            subjectsLayout.addView(subjectView);
                        }
                    } else {
                        TextView noSubjects = new TextView(Addsection.this);
                        noSubjects.setText("• No subjects yet");
                        noSubjects.setTextSize(14);
                        noSubjects.setPadding(48, 8, 24, 8);
                        noSubjects.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        subjectsLayout.addView(noSubjects);
                    }

                    sectionView.setOnClickListener(v -> {
                        if (subjectsLayout.getVisibility() == View.VISIBLE) {
                            subjectsLayout.setVisibility(View.GONE);
                            sectionView.setText(sectionName + " ▼");
                        } else {
                            subjectsLayout.setVisibility(View.VISIBLE);
                            sectionView.setText(sectionName + " ▲");
                        }

                        sectionNameInput.setText(sectionName);
                        selectedSectionName = sectionName;

                        // Clear subject selection when switching sections
                        subjectEditInput.setText("");
                        selectedSubjectName = null;
                    });

                    sectionLayout.addView(sectionView);
                    sectionLayout.addView(subjectsLayout);
                    sectionListContainer.addView(sectionLayout);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Addsection.this, "Failed to load sections", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Subject methods
    private void addSubject() {
        if (selectedSectionName == null) {
            Toast.makeText(this, "Please select a section first", Toast.LENGTH_SHORT).show();
            return;
        }

        String newSubject = subjectEditInput.getText().toString().trim();
        if (newSubject.isEmpty()) {
            Toast.makeText(this, "Subject name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference subjectsRef = sectionsRef.child(selectedSectionName).child("subjects");

        subjectsRef.child(newSubject).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Toast.makeText(this, "Subject already exists", Toast.LENGTH_SHORT).show();
                } else {
                    subjectsRef.child(newSubject).setValue(true).addOnCompleteListener(addTask -> {
                        if (addTask.isSuccessful()) {
                            Toast.makeText(this, "Subject added", Toast.LENGTH_SHORT).show();
                            subjectEditInput.setText("");
                            selectedSubjectName = null;
                        } else {
                            Toast.makeText(this, "Failed to add subject", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Error checking subject", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editSubject() {
        if (selectedSectionName == null) {
            Toast.makeText(this, "Please select a section first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedSubjectName == null) {
            Toast.makeText(this, "Please select a subject to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        String newSubjectName = subjectEditInput.getText().toString().trim();
        if (newSubjectName.isEmpty()) {
            Toast.makeText(this, "New subject name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newSubjectName.equals(selectedSubjectName)) {
            Toast.makeText(this, "Subject name is unchanged", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference subjectsRef = sectionsRef.child(selectedSectionName).child("subjects");

        subjectsRef.child(newSubjectName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    Toast.makeText(this, "Subject name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Add new subject, then remove old
                    subjectsRef.child(newSubjectName).setValue(true).addOnCompleteListener(addTask -> {
                        if (addTask.isSuccessful()) {
                            subjectsRef.child(selectedSubjectName).removeValue().addOnCompleteListener(removeTask -> {
                                if (removeTask.isSuccessful()) {
                                    Toast.makeText(this, "Subject renamed successfully", Toast.LENGTH_SHORT).show();
                                    subjectEditInput.setText("");
                                    selectedSubjectName = null;
                                } else {
                                    Toast.makeText(this, "Failed to remove old subject", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, "Failed to rename subject", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Error checking subject", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSubject() {
        if (selectedSectionName == null) {
            Toast.makeText(this, "Please select a section first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedSubjectName == null) {
            Toast.makeText(this, "Please select a subject to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference subjectsRef = sectionsRef.child(selectedSectionName).child("subjects");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete the subject \"" + selectedSubjectName + "\"?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    subjectsRef.child(selectedSubjectName).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Subject deleted", Toast.LENGTH_SHORT).show();
                            subjectEditInput.setText("");
                            selectedSubjectName = null;
                        } else {
                            Toast.makeText(this, "Failed to delete subject", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }
}
