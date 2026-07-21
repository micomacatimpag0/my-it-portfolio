package com.example.mobilecomp;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gamepage extends AppCompatActivity {
    private MediaPlayer bgMusic;
    private String wordToGuess = "";
    private StringBuilder currentGuess;
    private int wrongGuesses = 0;
    private final int MAX_WRONG = 6;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questionList = new ArrayList<>();

    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private ImageView hangmanImage;
    private TextView wordDisplay;
    private GridLayout letterGrid;
    private List<Character> guessedLetters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamepage);

        bgMusic = MediaPlayer.create(this, R.raw.maintheme);
        bgMusic.setLooping(true);
        bgMusic.start();

        timerTextView = findViewById(R.id.timer);
        hangmanImage = findViewById(R.id.hangmanImage);
        wordDisplay = findViewById(R.id.wordDisplay);
        letterGrid = findViewById(R.id.letterGrid);

        fetchWordFromFirebase();

        findViewById(R.id.settingsButton).setOnClickListener(v -> showSettingsPopup());

        Button switchModeBtn = findViewById(R.id.switchModeButton);
        switchModeBtn.setOnClickListener(v -> {
            isLetterMode = !isLetterMode;
            switchModeBtn.setText(isLetterMode ? "123/?" : "ABC");
            setupLetterButtons();
        });

    }

    private void fetchWordFromFirebase() {
        String section = getIntent().getStringExtra("section");
        String subject = getIntent().getStringExtra("subject");
        String quiz = getIntent().getStringExtra("quiz");

        if (section == null || subject == null || quiz == null) {
            Toast.makeText(this, "Missing section/subject/quiz!", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionsRef = database.getReference("sections")
                .child(section)
                .child("subjects")
                .child(subject)
                .child(quiz)
                .child("questions");

        questionsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String questionText = snapshot.child("question").getValue(String.class);
                    String answer = snapshot.child("answer").getValue(String.class);
                    String timer = snapshot.child("timer").getValue(String.class);

                    if (questionText != null && answer != null && timer != null) {
                        questionList.add(new Question(questionText, answer.toUpperCase(), timer));
                    }
                }

                if (!questionList.isEmpty()) {
                    Collections.shuffle(questionList);
                    currentQuestionIndex = 0;
                    loadNextQuestion();
                } else {
                    Toast.makeText(this, "No questions found for this quiz!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Failed to load words.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question current = questionList.get(currentQuestionIndex);
            wordToGuess = current.answer;
            currentGuess = new StringBuilder("_".repeat(wordToGuess.length()));
            wordDisplay.setText(currentGuess.toString());

            TextView questionText = findViewById(R.id.question);
            questionText.setText(current.questionText);

            guessedLetters.clear();
            wrongGuesses = 0;
            updateHangmanImage();
            setupLetterButtons();

            // Cancel previous timer if it exists
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            // Setup the new timer
            int timeLimitInMinutes = 1; // default
            try {
                timeLimitInMinutes = Integer.parseInt(current.timer);
            } catch (NumberFormatException e) {
                Log.e("TIMER", "Invalid timer format. Using default of 1 minute.");
            }

            int millis = timeLimitInMinutes * 60 * 1000;

            countDownTimer = new CountDownTimer(millis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    timerTextView.setText("00:00");
                    Toast.makeText(Gamepage.this, "Time's up! The word was: " + wordToGuess, Toast.LENGTH_SHORT).show();
                    loadNextQuestion(); // Move to next question without increasing score
                }


            }.start();

            currentQuestionIndex++;
        } else {
            // End of game → record result
            if (countDownTimer != null) countDownTimer.cancel(); // Clean up
            recordGameResult();
            disableAllButtons();
        }
    }



    private boolean isLetterMode = true;  // track mode

    private void setupLetterButtons() {
        letterGrid.removeAllViews();

        // Get screen width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int buttonWidth = getResources().getDimensionPixelSize(R.dimen.button_width); // Define a reasonable width in dimens.xml
        int maxColumns = screenWidth / buttonWidth; // Calculate maximum columns based on screen width

        // Dynamically calculate column count for better fitting
        letterGrid.setColumnCount(Math.min(maxColumns, 10)); // Limit column count to 10 or the calculated maxColumns

        String[] qwertyOrder = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        List<Character> symbols = new ArrayList<>();
        for (char c = '0'; c <= '9'; c++) symbols.add(c);
        Collections.addAll(symbols, '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_');

        // Depending on mode, set the correct buttons
        if (isLetterMode) {
            for (String row : qwertyOrder) {
                for (char c : row.toCharArray()) {
                    addGridButton(c);
                }
            }
        } else {
            for (char c : symbols) {
                addGridButton(c);
            }
        }
    }


    private void addGridButton(char c) {
        Button btn = new Button(this);
        btn.setText(String.valueOf(c));
        btn.setTextSize(12f);  // Decreased text size for better fit
        btn.setAllCaps(false);

        // Use smaller padding for a more compact look
        int padding = getResources().getDimensionPixelSize(R.dimen.button_padding);
        btn.setPadding(padding, padding, padding, padding);

        // Adjust background and text color
        btn.setBackgroundResource(R.drawable.round_button);
        btn.setTextColor(getResources().getColor(android.R.color.white));

        // Set the layout params to control button size and margins
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getResources().getDimensionPixelSize(R.dimen.button_width);  // Smaller width
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.setMargins(8, 8, 8, 8); // Reduced margin for more compact look
        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> {
            if (!guessedLetters.contains(c)) {
                guessedLetters.add(c);
                btn.setEnabled(false);
                btn.setAlpha(0.5f);
                checkGuess(c);
            }
        });

        letterGrid.addView(btn);
    }



    private void checkGuess(char letterOrSymbol) {
        boolean correct = false;

        // Check if the guessed character is in the wordToGuess
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letterOrSymbol) {
                currentGuess.setCharAt(i, letterOrSymbol);
                correct = true;
            }
        }

        // Update the display
        wordDisplay.setText(currentGuess.toString());

        if (!correct) {
            wrongGuesses++;
            updateHangmanImage();

            if (wrongGuesses >= MAX_WRONG) {
                if (countDownTimer != null) countDownTimer.cancel();
                Toast.makeText(this, "Game Over for this word! The word was: " + wordToGuess, Toast.LENGTH_SHORT).show();
                loadNextQuestion();
            }
        } else if (currentGuess.toString().equals(wordToGuess)) {
            if (countDownTimer != null) countDownTimer.cancel(); // ✅ cancel timer if player guessed full word
            Toast.makeText(this, "Correct! Moving to the next question.", Toast.LENGTH_SHORT).show();
            score++; // ✅ only increase score for correct answer
            loadNextQuestion();
        }
    }




    private void updateHangmanImage() {
        int resId = getResources().getIdentifier("hangman_" + wrongGuesses, "drawable", getPackageName());
        hangmanImage.setImageResource(resId);
    }

    private void disableAllButtons() {
        for (int i = 0; i < letterGrid.getChildCount(); i++) {
            Button btn = (Button) letterGrid.getChildAt(i);
            btn.setEnabled(false);
        }
    }

    private void showSettingsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_settings, null);

        Button toggleMusicBtn = popupView.findViewById(R.id.toggleMusicBtn);
        Button restartBtn = popupView.findViewById(R.id.restartGameBtn);
        Button closeBtn = popupView.findViewById(R.id.closePopupBtn);
        Button Endgame = popupView.findViewById(R.id.endgame);

        builder.setView(popupView);
        AlertDialog dialog = builder.create();

        Endgame.setOnClickListener(view -> {
            Intent intent = new Intent(Gamepage.this, HomeFragment.class);
            startActivity(intent);
            finish();
        });

        toggleMusicBtn.setOnClickListener(v -> {
            if (bgMusic != null) {
                if (bgMusic.isPlaying()) {
                    bgMusic.pause();
                    toggleMusicBtn.setText("Play Music");
                } else {
                    bgMusic.start();
                    toggleMusicBtn.setText("Pause Music");
                }
            }
        });

        restartBtn.setOnClickListener(v -> {
            dialog.dismiss();
            recreate();
        });

        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void recordGameResult() {
        String section = getIntent().getStringExtra("section");
        String subject = getIntent().getStringExtra("subject");
        String quiz = getIntent().getStringExtra("quiz");
        String studentId = getIntent().getStringExtra("studentId");

        int totalQuestions = questionList.size();
        int percentage = (int) ((score / (double) totalQuestions) * 100);
        String status = percentage >= 60 ? "Passed" : "Failed";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = databaseReference.child("users").child(studentId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = snapshot.child("fullName").getValue(String.class);

                // Fetch instructor from quiz node
                DatabaseReference instructorRef = databaseReference.child("sections")
                        .child(section)
                        .child("subjects")
                        .child(subject)
                        .child(quiz)
                        .child("instructor");

                instructorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot instructorSnapshot) {
                        String instructorName = instructorSnapshot.getValue(String.class);

                        DatabaseReference recordRef = databaseReference.child("records").push();
                        recordRef.child("studentId").setValue(studentId);
                        recordRef.child("fullName").setValue(fullName);
                        recordRef.child("section").setValue(section);
                        recordRef.child("subject").setValue(subject);
                        recordRef.child("quiz").setValue(quiz);
                        recordRef.child("score").setValue(score);
                        recordRef.child("status").setValue(status);
                        recordRef.child("instructor").setValue(instructorName);  // added instructor here
                        recordRef.child("timestamp").setValue(System.currentTimeMillis());

                        Toast.makeText(Gamepage.this, "Game completed! Status: " + status, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Gamepage.this, "Failed to fetch instructor", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Gamepage.this, "Failed to record result", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
