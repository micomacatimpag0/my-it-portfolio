package com.example.mobilecomp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class Superadmin extends AppCompatActivity {

    private EditText editInstructorFullName, editInstructorUsername, editInstructorPassword, editInstructorConfirmPassword;
    private Button btnAddInstructor, assignButton, addsection;
    private ImageButton btnLogout, clear;
    private DatabaseReference databaseReference;

    private LinearLayout selectionCancelToast;
    private ImageView btnDismissToast;
    private TextView toastText;

    private Map<String, List<String>> sectionSubjectsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superadmin);

        addsection = findViewById(R.id.btnAddsection);
        assignButton = findViewById(R.id.assign);
        editInstructorFullName = findViewById(R.id.editInstructorFullName);
        editInstructorUsername = findViewById(R.id.editInstructorUsername);
        editInstructorPassword = findViewById(R.id.editInstructorPassword);
        editInstructorConfirmPassword = findViewById(R.id.editInstructorConfirmPassword);
        btnAddInstructor = findViewById(R.id.btnAddInstructor);
        btnLogout = findViewById(R.id.btnLogout);
        selectionCancelToast = findViewById(R.id.selection_cancel_toast);
        btnDismissToast = findViewById(R.id.btn_dismiss_toast);
        clear = findViewById(R.id.btnClearUsername);
        toastText = findViewById(R.id.toast_text);
        Button btnRemoveSections = findViewById(R.id.removesections);
        btnRemoveSections.setOnClickListener(v -> showRemoveSectionsDialog());
        Button btnDeleteInstructor = findViewById(R.id.btndeleteInstructor);

        databaseReference = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                .getReference("users");

        assignButton.setOnClickListener(v -> showSectionDialog());

        addsection.setOnClickListener(v -> {
            Intent intent = new Intent(Superadmin.this, Addsection.class);
            startActivity(intent);
            finish();
        });

        clear.setOnClickListener(v -> clearFields());

        Button editSectionAssignBtn = findViewById(R.id.editsectionassign);
        editSectionAssignBtn.setOnClickListener(v -> {
            databaseReference.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, "Failed to get instructors", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (task.getResult() == null || !task.getResult().hasChildren()) {
                    Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> instructorList = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if ("instructor".equalsIgnoreCase(role)) {
                        String fullname = snapshot.child("fullname").getValue(String.class);
                        String username = snapshot.getKey();
                        instructorList.add(fullname != null ? fullname : username);
                    }
                }

                if (instructorList.isEmpty()) {
                    Toast.makeText(this, "No instructors found", Toast.LENGTH_SHORT).show();
                } else {
                    showInstructorsDialog(instructorList);
                }
            });
        });

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        btnDeleteInstructor.setOnClickListener(v -> {
            String studentId = editInstructorUsername.getText().toString().trim();

            if (studentId.isEmpty()) {
                Toast.makeText(this, "Please enter Student ID or Username", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Confirmation")
                    .setMessage("Do you really want to delete instructor with ID: " + studentId + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteUserFromFirebase(studentId);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnAddInstructor.setOnClickListener(view -> {
            String fullName = editInstructorFullName.getText().toString().trim();
            String username = editInstructorUsername.getText().toString().trim();
            String password = editInstructorPassword.getText().toString().trim();
            String confirmPassword = editInstructorConfirmPassword.getText().toString().trim();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.toLowerCase().contains("admin")) {
                Toast.makeText(this, "The username cannot contain 'admin'", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sectionSubjectsMap.isEmpty()) {
                Toast.makeText(this, "Please assign at least one section and subjects", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("fullname", fullName);
                    userData.put("name", fullName);
                    userData.put("password", password);
                    userData.put("role", "instructor");

                    Map<String, Object> sectionsMap = new HashMap<>();
                    for (Map.Entry<String, List<String>> entry : sectionSubjectsMap.entrySet()) {
                        Map<String, Object> subjectsMap = new HashMap<>();
                        List<String> subjects = entry.getValue();
                        for (int i = 0; i < subjects.size(); i++) {
                            subjectsMap.put(subjects.get(i), "subject" + (i + 1));
                        }
                        sectionsMap.put(entry.getKey(), subjectsMap);
                    }
                    userData.put("sections", sectionsMap);

                    databaseReference.child(username).setValue(userData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Superadmin.this, snapshot.exists() ?
                                    "Instructor updated successfully!" : "Instructor added successfully!", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(Superadmin.this, "Failed to add/update instructor.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Superadmin.this, "Error accessing database.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnLogout.setOnClickListener(view -> {
            startActivity(new Intent(Superadmin.this, Chooserole.class));
            finish();
        });

        btnDismissToast.setOnClickListener(v -> selectionCancelToast.setVisibility(View.GONE));
    }

    private void showInstructorsDialog(ArrayList<String> instructors) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_instructors);

        TextView title = dialog.findViewById(R.id.dialog_title);
        ListView listView = dialog.findViewById(R.id.instructors_list_view);
        Button btnClose = dialog.findViewById(R.id.btn_close_instructors_dialog);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, instructors);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedInstructorName = instructors.get(position);
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot userSnap : snapshot.getChildren()) {
                        String role = userSnap.child("role").getValue(String.class);
                        String fullName = userSnap.child("fullname").getValue(String.class);

                        if ("instructor".equalsIgnoreCase(role) &&
                                selectedInstructorName.equals(fullName)) {

                            String username = userSnap.getKey();
                            String password = userSnap.child("password").getValue(String.class);

                            editInstructorFullName.setText(fullName);
                            editInstructorUsername.setText(username);
                            editInstructorUsername.setEnabled(false);
                            editInstructorPassword.setText(password);
                            editInstructorConfirmPassword.setText(password);

                            sectionSubjectsMap.clear();

                            DataSnapshot sectionsSnapshot = userSnap.child("sections");
                            for (DataSnapshot sectionSnap : sectionsSnapshot.getChildren()) {
                                String sectionName = sectionSnap.getKey();
                                List<String> subjects = new ArrayList<>();

                                for (DataSnapshot subjectSnap : sectionSnap.getChildren()) {
                                    subjects.add(subjectSnap.getKey());
                                }

                                sectionSubjectsMap.put(sectionName, subjects);
                            }

                            showToastMessage("Assigned Sections", new ArrayList<>(sectionSubjectsMap.keySet()));
                            dialog.dismiss();
                            return;
                        }
                    }
                    Toast.makeText(Superadmin.this, "Instructor data not found", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(Superadmin.this, "Error retrieving instructor data", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.copyFrom(window.getAttributes());
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setAttributes(params);
        }
    }

    private void showSectionDialog() {
        Dialog secDialog = new Dialog(this);
        secDialog.setContentView(R.layout.dialog);
        LinearLayout sectionsContainer = secDialog.findViewById(R.id.subjects_container);
        TextView title = secDialog.findViewById(R.id.section_name);
        ImageView btnClose = secDialog.findViewById(R.id.btn_close);

        title.setText("Select Section");
        btnClose.setOnClickListener(x -> secDialog.dismiss());

        DatabaseReference sectionsRef = FirebaseDatabase.getInstance().getReference("sections");

        sectionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(Superadmin.this, "No sections found.", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot sectionSnap : snapshot.getChildren()) {
                    String sectionName = sectionSnap.getKey();
                    Button btn = new Button(Superadmin.this);
                    btn.setText(sectionName);
                    btn.setAllCaps(false);
                    btn.setOnClickListener(v -> {
                        secDialog.dismiss();
                        showAssignSubjectsDialog(sectionName);
                    });
                    sectionsContainer.addView(btn);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Superadmin.this, "Failed to load sections.", Toast.LENGTH_SHORT).show();
            }
        });

        secDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        secDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        secDialog.show();
    }

    private void showAssignSubjectsDialog(String sectionName) {
        Dialog subjectDialog = new Dialog(this);
        subjectDialog.setContentView(R.layout.dialog_assign_subjects);
        subjectDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subjectDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout subjectsContainer = subjectDialog.findViewById(R.id.subjects_container);
        Button confirmButton = subjectDialog.findViewById(R.id.btn_confirm);
        ImageView close = subjectDialog.findViewById(R.id.btn_close);
        TextView sectionText = subjectDialog.findViewById(R.id.section_name);
        sectionText.setText("Subjects for: " + sectionName);

        close.setOnClickListener(v -> subjectDialog.dismiss());

        DatabaseReference subjectsRef = FirebaseDatabase.getInstance()
                .getReference("sections").child(sectionName).child("subjects");

        subjectsContainer.removeAllViews();

        subjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(Superadmin.this, "No subjects found for " + sectionName, Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DataSnapshot subjectSnap : snapshot.getChildren()) {
                    String subjectName = subjectSnap.getKey();
                    Object value = subjectSnap.getValue();

                    // Check if the subject should be shown (either Boolean true or String)
                    boolean shouldShow = false;

                    if (value instanceof Boolean) {
                        shouldShow = (Boolean) value;
                    } else if (value instanceof String) {
                        shouldShow = !((String) value).isEmpty();
                    }

                    if (shouldShow) {
                        CheckBox checkBox = new CheckBox(Superadmin.this);
                        checkBox.setText(subjectName);
                        checkBox.setChecked(false);
                        checkBox.setTextSize(16f);
                        checkBox.setPadding(8, 16, 8, 16);

                        // Check if this subject was previously selected
                        List<String> previouslySelected = sectionSubjectsMap.get(sectionName);
                        if (previouslySelected != null && previouslySelected.contains(subjectName)) {
                            checkBox.setChecked(true);
                        }

                        subjectsContainer.addView(checkBox);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Superadmin.this, "Error loading subjects", Toast.LENGTH_SHORT).show();
            }
        });

        confirmButton.setOnClickListener(v -> {
            List<String> selected = new ArrayList<>();
            for (int i = 0; i < subjectsContainer.getChildCount(); i++) {
                View child = subjectsContainer.getChildAt(i);
                if (child instanceof CheckBox && ((CheckBox) child).isChecked()) {
                    selected.add(((CheckBox) child).getText().toString());
                }
            }

            if (!selected.isEmpty()) {
                sectionSubjectsMap.put(sectionName, selected);
                showToastMessage(sectionName, selected);
                subjectDialog.dismiss();
            } else {
                Toast.makeText(Superadmin.this, "Select at least one subject", Toast.LENGTH_SHORT).show();
            }
        });

        subjectDialog.show();
    }

    private void showToastMessage(String title, List<String> selectedSubjects) {
        String subjectsStr = String.join(", ", selectedSubjects);
        toastText.setText(title + ":\n" + subjectsStr);
        selectionCancelToast.setVisibility(View.VISIBLE);
    }

    private void clearFields() {
        editInstructorFullName.setText("");
        editInstructorUsername.setText("");
        editInstructorUsername.setEnabled(true);
        editInstructorPassword.setText("");
        editInstructorConfirmPassword.setText("");
        sectionSubjectsMap.clear();
        selectionCancelToast.setVisibility(View.GONE);
    }

    private void showRemoveSectionsDialog() {
        if (sectionSubjectsMap.isEmpty()) {
            Toast.makeText(this, "No sections assigned to this instructor.", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_remove_sections);

        LinearLayout container = dialog.findViewById(R.id.sections_container);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_remove);
        ImageView btnClose = dialog.findViewById(R.id.btn_close_remove);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        Map<String, CheckBox> sectionCheckBoxMap = new HashMap<>();

        for (String section : sectionSubjectsMap.keySet()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(section);
            checkBox.setTextSize(16f);
            checkBox.setPadding(8, 16, 8, 16);
            sectionCheckBoxMap.put(section, checkBox);
            container.addView(checkBox);
        }

        btnConfirm.setOnClickListener(v -> {
            List<String> toRemove = new ArrayList<>();
            for (Map.Entry<String, CheckBox> entry : sectionCheckBoxMap.entrySet()) {
                if (entry.getValue().isChecked()) {
                    toRemove.add(entry.getKey());
                }
            }

            if (toRemove.isEmpty()) {
                Toast.makeText(this, "Select at least one section to remove.", Toast.LENGTH_SHORT).show();
                return;
            }

            for (String section : toRemove) {
                sectionSubjectsMap.remove(section);
            }

            String username = editInstructorUsername.getText().toString().trim();
            if (!editInstructorUsername.isEnabled()) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(username).child("sections");
                for (String section : toRemove) {
                    userRef.child(section).removeValue();
                }
            }

            showToastMessage("Remaining Sections", new ArrayList<>(sectionSubjectsMap.keySet()));
            dialog.dismiss();
        });

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.copyFrom(window.getAttributes());
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            window.setAttributes(params);
        }
    }

    private void deleteUserFromFirebase(String studentId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(studentId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userRef.removeValue().addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}