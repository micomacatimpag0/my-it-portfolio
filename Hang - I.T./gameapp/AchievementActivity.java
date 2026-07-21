    package com.example.mobilecomp;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.ImageButton;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.database.*;

    public class AchievementActivity extends AppCompatActivity {

        private LinearLayout achievementsList;
        private DatabaseReference databaseReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_achievements);

            achievementsList = findViewById(R.id.achievementsList);
            ImageButton backBtn = findViewById(R.id.back_button);
            backBtn.setOnClickListener(v -> finish());

            // ✅ Initialize reference first
            databaseReference = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                    .getReference("records");

            // ✅ Always use studentId from SharedPreferences
            String studentId = getSharedPreferences("UserSession", MODE_PRIVATE).getString("username", "");

            if (!studentId.isEmpty()) {
                fetchAchievements(studentId);
            } else {
                Toast.makeText(this, "No student ID found in session", Toast.LENGTH_SHORT).show();
            }
        }


        private void fetchAchievements(String studentId) {
            databaseReference.orderByChild("studentId").equalTo(studentId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                achievementsList.removeAllViews();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    try {
                                        String quiz = dataSnapshot.child("quiz").getValue(String.class);
                                        String subject = dataSnapshot.child("subject").getValue(String.class);

                                        Object scoreObj = dataSnapshot.child("score").getValue();
                                        String score;
                                        if (scoreObj instanceof Long || scoreObj instanceof Double || scoreObj instanceof Integer) {
                                            score = String.valueOf(scoreObj);
                                        } else if (scoreObj instanceof String) {
                                            score = (String) scoreObj;
                                        } else {
                                            score = "N/A";
                                        }

                                        String status = dataSnapshot.child("status").getValue(String.class);
                                        String formattedStatus = (status != null && status.equalsIgnoreCase("Passed")) ? "✅ Passed"
                                                : (status != null && status.equalsIgnoreCase("Failed")) ? "❌ Failed"
                                                : "Unknown";

                                        View achievementView = getLayoutInflater().inflate(R.layout.item_achievement, null);

                                        TextView activityTitle = achievementView.findViewById(R.id.activityTitle);
                                        TextView activityScore = achievementView.findViewById(R.id.activityScore);
                                        TextView activityStatus = achievementView.findViewById(R.id.activityStatus);
                                        TextView activityCompletion = achievementView.findViewById(R.id.activityCompletion);

                                        activityTitle.setText((quiz != null ? quiz : "Unknown") + " - " + (subject != null ? subject : "Unknown"));
                                        activityScore.setText("Score: " + score);
                                        activityStatus.setText("Status: " + formattedStatus);
                                        activityCompletion.setText("Completed");

                                        achievementsList.addView(achievementView);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(AchievementActivity.this, "Error loading an achievement item", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(AchievementActivity.this, "No achievements found for this student", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(AchievementActivity.this, "Failed to load achievements: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
