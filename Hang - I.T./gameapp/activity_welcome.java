package com.example.mobilecomp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class activity_welcome extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView loaderImage;
    private FrameLayout progressContainer;
    private Handler handler = new Handler(Looper.getMainLooper());

    private static final String TEST_URL = "https://www.google.com/favicon.ico";
    private static final int TOTAL_PROGRESS = 100;

    private volatile int progressStatus = 0;
    private volatile boolean internetAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        loaderImage = findViewById(R.id.loaderImage);
        progressContainer = findViewById(R.id.progressContainer);

        progressContainer.post(() -> {
            float maxTravelDistance = progressContainer.getWidth() - loaderImage.getWidth();
            startLoadingLoop(maxTravelDistance);
        });
    }

    private void startLoadingLoop(float maxTravelDistance) {
        new Thread(() -> {
            long offlineStart = -1;

            while (progressStatus < TOTAL_PROGRESS) {
                internetAvailable = isInternetAvailable();

                if (!internetAvailable) {
                    if (offlineStart == -1) offlineStart = System.currentTimeMillis();

                    handler.post(() -> Toast.makeText(activity_welcome.this, "Waiting for internet...", Toast.LENGTH_SHORT).show());

                    if (System.currentTimeMillis() - offlineStart > 10000) {
                        // Timeout after 10 seconds offline
                        break;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                    continue;
                }

                offlineStart = -1; // Reset offline timer

                int loadTime = measureDownloadSpeedAndCalculateLoadTime();

                progressStatus = Math.min(progressStatus + 2, TOTAL_PROGRESS); // Faster increment

                int currentProgress = progressStatus;
                handler.post(() -> {
                    progressBar.setProgress(currentProgress);
                    float translation = (currentProgress / 100f) * maxTravelDistance;
                    loaderImage.setTranslationX(translation);
                });

                try {
                    Thread.sleep(Math.max(10, loadTime / TOTAL_PROGRESS)); // Faster response
                } catch (InterruptedException ignored) {}
            }

            handler.post(() -> {
                Intent intent = new Intent(activity_welcome.this, Chooserole.class);
                startActivity(intent);
                finish();
            });

        }).start();
    }

    private int measureDownloadSpeedAndCalculateLoadTime() {
        try {
            long startTime = System.currentTimeMillis();

            URL url = new URL(TEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.connect();

            InputStream input = connection.getInputStream();
            byte[] buffer = new byte[512];
            int totalBytes = 0;
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1 && totalBytes < 2048) {
                totalBytes += bytesRead;
            }
            input.close();

            long duration = Math.max(1, System.currentTimeMillis() - startTime);

            float speed = totalBytes / (duration / 1000f); // bytes/sec

            int loadTime = (int) (3000 - speed / 15);
            return Math.max(500, Math.min(loadTime, 3000)); // Clamp 500ms–3000ms
        } catch (Exception e) {
            return 3000;
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }
}
