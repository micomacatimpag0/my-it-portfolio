package com.example.mobilecomp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private String username;
    private long backPressedTime; // To track time of the last back press
    private Toast backToast; // To show exit message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Get the username passed from login
        username = getIntent().getStringExtra("username");

        // Check if coming from AchievementActivity
        boolean openProfile = getIntent().getBooleanExtra("openProfile", false);

        // If openProfile is true, load the ProfileFragment; otherwise, load HomeFragment
        if (openProfile) {
            loadFragment(new ProfileFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                selectedFragment.setArguments(bundle);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // If there's music playing, pause it
        // This is optional if you're handling media in your app
    }

    @Override
    protected void onStop() {
        super.onStop();
        // If there's music playing, stop it and release resources
        // This is optional if you're handling media in your app
    }

    @Override
    public void onBackPressed() {
        // Check if there's any fragment in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // Handle the double back press to exit
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }
}
