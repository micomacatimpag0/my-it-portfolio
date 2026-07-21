package com.example.mobilecomp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickSection extends AppCompatActivity {
    private ImageButton backButton;
    private Button create;
    private Spinner sectionSpinner, subjectSpinner;
    private EditText activityNameEditText, passwordInput;

    private DatabaseReference dbRef;
    private DatabaseReference usersRef;

    private String selectedSection = "";
    private String selectedSubject = "";
    private String activityName = "";
    private String instructorUsername;
    private Map<String, List<String>> instructorSections = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pick_section);

        sectionSpinner = findViewById(R.id.sectionSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        create = findViewById(R.id.createquestion);
        backButton = findViewById(R.id.back);
        activityNameEditText = findViewById(R.id.activityName);
        passwordInput = findViewById(R.id.passwordInput);

        dbRef = FirebaseDatabase.getInstance().getReference("sections");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        instructorUsername = getIntent().getStringExtra("username");

        // First load the instructor's assigned sections and subjects
        loadInstructorAssignments();

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSection = parent.getItemAtPosition(position).toString();
                updateSubjectSpinner(selectedSection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        backButton.setOnClickListener(v -> onBackPressed());

        create.setOnClickListener(v -> {
            activityName = activityNameEditText.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (selectedSection.isEmpty() || selectedSubject.isEmpty() || activityName.isEmpty() || password.isEmpty()) {
                Toast.makeText(PickSection.this, "Please select section, subject, and fill in both activity name and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if activity already exists
            DatabaseReference activityRef = dbRef.child(selectedSection)
                    .child("subjects")
                    .child(selectedSubject)
                    .child(activityName);

            activityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(PickSection.this, "Activity already exists. Please choose a different name.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Get current date and time in a readable format (ISO 8601)
                        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());

                        // Create activity data with date and time
                        HashMap<String, Object> activityData = new HashMap<>();
                        activityData.put("password", password);
                        activityData.put("instructor", instructorUsername);
                        activityData.put("created_at", currentDateTime);

                        activityRef.setValue(activityData).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(PickSection.this, "Activity created successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PickSection.this, AddActivity.class);
                                intent.putExtra("section", selectedSection);
                                intent.putExtra("subject", selectedSubject);
                                intent.putExtra("activityName", activityName);
                                startActivity(intent);
                            } else {
                                Toast.makeText(PickSection.this, "Failed to create activity", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) { }
            });
        });
    }

    private void loadInstructorAssignments() {
        usersRef.child(instructorUsername).child("sections").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                instructorSections.clear();

                for (DataSnapshot sectionSnap : snapshot.getChildren()) {
                    String sectionName = sectionSnap.getKey();
                    List<String> subjects = new ArrayList<>();

                    for (DataSnapshot subjectSnap : sectionSnap.getChildren()) {
                        subjects.add(subjectSnap.getKey());
                    }

                    instructorSections.put(sectionName, subjects);
                }

                updateSectionSpinner();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PickSection.this, "Failed to load instructor assignments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSectionSpinner() {
        List<String> sectionList = new ArrayList<>(instructorSections.keySet());

        if (sectionList.isEmpty()) {
            Toast.makeText(this, "No sections assigned to this instructor", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sectionList);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(sectionAdapter);

        if (!sectionList.isEmpty()) {
            selectedSection = sectionList.get(0);
            updateSubjectSpinner(selectedSection);
        }
    }

    private void updateSubjectSpinner(String section) {
        List<String> subjectList = instructorSections.get(section);

        if (subjectList == null) {
            subjectList = new ArrayList<>();
        }

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjectList);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        if (!subjectList.isEmpty()) {
            selectedSubject = subjectList.get(0);
        }
    }
}