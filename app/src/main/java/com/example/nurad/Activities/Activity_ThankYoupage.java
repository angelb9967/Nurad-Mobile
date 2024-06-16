package com.example.nurad.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.R;

public class Activity_ThankYoupage extends AppCompatActivity {

    private static final long REDIRECT_DELAY = 5000; // 5 seconds

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_youpage);

        // Handle window insets for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Use a Handler to navigate to Activity_BottomNav after 6 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Activity_ThankYoupage.this, Activity_BottomNav.class);
            intent.putExtra("navigateTo", "account");
            startActivity(intent);
            finish();
        }, REDIRECT_DELAY);
    }
}