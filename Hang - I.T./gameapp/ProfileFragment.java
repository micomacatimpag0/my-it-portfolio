package com.example.mobilecomp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.activity_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnAchievements = view.findViewById(R.id.achievements);
        ImageButton btnEditProfile = view.findViewById(R.id.btnEditProfile);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnAchievements.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AchievementActivity.class));
        });



        btnEditProfile.setOnClickListener(v -> {
            // Pass username from SharedPreferences to EditActivityUser
            String username = requireActivity()
                    .getSharedPreferences("UserSession", requireContext().MODE_PRIVATE)
                    .getString("username", null);
            if (username != null) {
                Intent intent = new Intent(requireContext(), EditActivityUser.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(v -> {
            requireActivity()
                    .getSharedPreferences("UserSession", requireContext().MODE_PRIVATE)
                    .edit().clear().apply();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finishAffinity();
        });

        loadUserProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfile(); // Refresh profile when returning
    }

    private void loadUserProfile() {
        String username = requireActivity()
                .getSharedPreferences("UserSession", requireContext().MODE_PRIVATE)
                .getString("username", null);

        if (username == null) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance("https://hangman-13ddf-default-rtdb.firebaseio.com/")
                .getReference("users").child(username);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullName = snapshot.child("fullname").getValue(String.class);
                    String studentId = snapshot.child("studentId").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);
                    String profileImageUri = snapshot.child("profileImageUri").getValue(String.class);

                    TextView profileName = getView().findViewById(R.id.profileName);
                    TextView profileId = getView().findViewById(R.id.profileId);

                    ImageView profilePic = getView().findViewById(R.id.profilePic);

                    profileName.setText(fullName != null ? fullName : "N/A");
                    profileId.setText(studentId != null ? studentId : "N/A");


                    if (profileImageUri != null && !profileImageUri.isEmpty()) {
                        Uri localUri = Uri.parse(profileImageUri);
                        Glide.with(requireContext())
                                .load(localUri)
                                .placeholder(R.drawable.profileholder)
                                .into(profilePic);
                    } else {
                        profilePic.setImageResource(R.drawable.profileholder);
                    }
                } else {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}