package com.example.mobilecomp;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Editcards extends AppCompatActivity {

    private String section, subject, quizId;
    private LinearLayout questionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcards);

        // Get section, subject, and quiz from Intent
        section = getIntent().getStringExtra("section");
        subject = getIntent().getStringExtra("subject");
        quizId = getIntent().getStringExtra("quiz");

        // Set up breadcrumb text
        TextView quizTitleTextView = findViewById(R.id.quiz_title);
        String breadcrumb = section + " > " + subject + " > " + quizId;
        SpannableString spannable = new SpannableString(breadcrumb);
        int start = breadcrumb.lastIndexOf(quizId);
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, breadcrumb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        quizTitleTextView.setText(spannable);

        questionsContainer = findViewById(R.id.questions_container);
        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(v -> saveChanges());

        ImageButton backBtn = findViewById(R.id.back_image_button);
        backBtn.setOnClickListener(v -> onBackPressed());

        // Populate questions for the selected quiz
        populateQuestions();
    }

    private void populateQuestions() {
        // Reference to the quiz questions in Firebase
        FirebaseDatabase.getInstance().getReference("sections")
                .child(section)
                .child("subjects")
                .child(subject)
                .child(quizId)
                .child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        questionsContainer.removeAllViews();

                        for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                            String questionId = questionSnapshot.getKey();
                            String questionText = questionSnapshot.child("question").getValue(String.class);
                            String answerText = questionSnapshot.child("answer").getValue(String.class);
                            String timerText = questionSnapshot.child("timer").getValue(String.class);

                            View questionView = LayoutInflater.from(Editcards.this).inflate(R.layout.card_question, questionsContainer, false);

                            EditText questionEditText = questionView.findViewById(R.id.question_edit);
                            EditText answerEditText = questionView.findViewById(R.id.answer_edit);
                            EditText timerEditText = questionView.findViewById(R.id.timer_edit);

                            questionEditText.setText(questionText);
                            answerEditText.setText(answerText);
                            timerEditText.setText(timerText);

                            questionView.setTag(questionId);
                            questionsContainer.addView(questionView);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Editcards.this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveChanges() {
        EditText passwordEditText = findViewById(R.id.edit_password);
        String newPassword = passwordEditText.getText().toString();

        DatabaseReference quizRef = FirebaseDatabase.getInstance()
                .getReference("sections")
                .child(section)
                .child("subjects")
                .child(subject)
                .child(quizId);

        // Update password
        quizRef.child("password").setValue(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Editcards.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Editcards.this, "Failed to update password.", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference quizQuestionsRef = quizRef.child("questions");

        // Update each question
        for (int i = 0; i < questionsContainer.getChildCount(); i++) {
            View questionView = questionsContainer.getChildAt(i);

            EditText questionEditText = questionView.findViewById(R.id.question_edit);
            EditText answerEditText = questionView.findViewById(R.id.answer_edit);
            EditText timerEditText = questionView.findViewById(R.id.timer_edit);

            String updatedQuestion = questionEditText.getText().toString();
            String updatedAnswer = answerEditText.getText().toString();
            String updatedTimer = timerEditText.getText().toString();

            String questionId = (String) questionView.getTag();

            if (questionId != null) {
                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("question", updatedQuestion);
                updatedData.put("answer", updatedAnswer);
                updatedData.put("timer", updatedTimer);

                quizQuestionsRef.child(questionId).updateChildren(updatedData);
            }
        }

        Toast.makeText(Editcards.this, "All changes saved!", Toast.LENGTH_SHORT).show();
    }
}
