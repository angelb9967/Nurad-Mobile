package com.example.nurad.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.R;

public class Activity_SplashScreen extends AppCompatActivity {
    private static final String TAG = "Activity_SplashScreen";
    Animation rotateAnimation;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logo = findViewById(R.id.logo);
        rotateAnimation();

        // Log the start of the splash screen
        Log.d(TAG, "Splash screen started");

        new Handler().postDelayed(() -> {
            Log.d(TAG, "Redirecting to SignUp activity");
            Intent i = new Intent(Activity_SplashScreen.this, Activity_SignUp.class);
            startActivity(i);
            finish();
        }, 6000);
    }

    private void rotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        logo.startAnimation(rotateAnimation);
        // Log the start of the rotation animation
        Log.d(TAG, "Logo rotation animation started");
    }
}