package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    private LinearLayout questionContainer;
    private Button addMoreBtn, submitBtn;
    private ImageButton backBtn;

    private String section, subject, activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Get data from intent
        Intent intent = getIntent();
        section = intent.getStringExtra("section");
        subject = intent.getStringExtra("subject");
        activityName = intent.getStringExtra("activityName");

        // Initialize Views
        questionContainer = findViewById(R.id.questionContainer);
        addMoreBtn = findViewById(R.id.addMoreBtn);
        submitBtn = findViewById(R.id.submitBtn);
        backBtn = findViewById(R.id.backBtn);

        // Back Button
        backBtn.setOnClickListener(v -> onBackPressed());

        // Add initial question block
        addNewQuestionFields();

        // Add More Button
        addMoreBtn.setOnClickListener(v -> addNewQuestionFields());

        // Submit Button
        submitBtn.setOnClickListener(v -> submitActivity());
    }

    private void addNewQuestionFields() {
        View questionCard = getLayoutInflater().inflate(R.layout.item_question_card, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 20;
        questionCard.setLayoutParams(params);

        Button removeButton = questionCard.findViewById(R.id.removequestion);
        removeButton.setOnClickListener(v -> {
            questionContainer.removeView(questionCard);
            Toast.makeText(AddActivity.this, "Removed a question!", Toast.LENGTH_SHORT).show();
        });

        questionContainer.addView(questionCard);
        Toast.makeText(AddActivity.this, "Added another question!", Toast.LENGTH_SHORT).show();
    }

    private void submitActivity() {
        Toast.makeText(AddActivity.this, "Submitting activity...", Toast.LENGTH_SHORT).show();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("sections")
                .child(section)
                .child("subjects")
                .child(subject)
                .child(activityName);

        int totalQuestions = questionContainer.getChildCount();
        if (totalQuestions == 0) {
            Toast.makeText(this, "Please add at least one question.", Toast.LENGTH_SHORT).show();
            return;
        }

        final int[] successCount = {0};
        final int[] failCount = {0};

        for (int i = 0; i < totalQuestions; i++) {
            View questionCard = questionContainer.getChildAt(i);

            EditText qInput = questionCard.findViewById(R.id.questionInput);
            EditText aInput = questionCard.findViewById(R.id.answerInput);
            EditText tInput = questionCard.findViewById(R.id.timerInput);

            if (qInput == null || aInput == null || tInput == null) {
                continue; // Skip views that are not question cards
            }

            String question = qInput.getText().toString().trim();
            String answer = aInput.getText().toString().trim();
            String timer = tInput.getText().toString().trim();

            if (question.isEmpty() || answer.isEmpty() || timer.isEmpty()) {
                Toast.makeText(this, "Please fill in all question fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String questionId = dbRef.child("questions").push().getKey();
            QuestionModel qModel = new QuestionModel(question, answer, timer);

            dbRef.child("questions").child(questionId).setValue(qModel)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            successCount[0]++;
                        } else {
                            failCount[0]++;
                        }

                        if (successCount[0] + failCount[0] == totalQuestions) {
                            if (failCount[0] == 0) {
                                Toast.makeText(AddActivity.this, "All questions uploaded successfully!", Toast.LENGTH_SHORT).show();
                                questionContainer.removeAllViews();
                                addNewQuestionFields(); // Add a fresh empty block
                            } else {
                                Toast.makeText(AddActivity.this, failCount[0] + " question(s) failed to upload.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
