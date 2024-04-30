package com.example.nurad.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_ForgotPassword extends AppCompatActivity {
    private Button ResetButton;
    private EditText logemail;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize views
        ResetButton = findViewById(R.id.ResetBtn);
        logemail = findViewById(R.id.Email_Etxt);

        // Set click listener for the Reset Password Button
        ResetButton.setOnClickListener(v -> {
            // Implement the logic to handle forgot password using Firebase
            sendPasswordResetEmail();
        });
    }

    private void sendPasswordResetEmail() {
        String email = logemail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email to reset the password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent. Check your email inbox.", Toast.LENGTH_SHORT).show();
                        // Redirect to another activity after successfully sending Password reset email
                        Intent intent = new Intent(Activity_ForgotPassword.this, Activity_Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to send password reset email. Please check your email address.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}