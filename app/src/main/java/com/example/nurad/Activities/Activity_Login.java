package com.example.nurad.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Login extends AppCompatActivity {
    private TextView SignUp_TxtV, forgotPasswordTextView;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private EditText logemail, logpassword;
    public static final String SHARED_PREFS = "sharedPrefs";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize views
        logemail = findViewById(R.id.Email_Etxt);
        logpassword = findViewById(R.id.Password_Etxt);
        SignUp_TxtV = findViewById(R.id.textView7);
        forgotPasswordTextView = findViewById(R.id.textView6);

        // Change the color of some text within 1 textview
        String text = "Don't have an account yet? Sign Up";
        SpannableString spannableString = new SpannableString(text);
        int startIndex = text.indexOf("Sign Up");

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#882065")), startIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SignUp_TxtV.setText(spannableString);

        // Set password transformation method for the password EditText field
        logpassword.setTransformationMethod(new PasswordTransformationMethod());

        // Set click listener for the login button
        Button loginBtn = findViewById(R.id.LoginBtn);
        loginBtn.setOnClickListener(view -> loginUser());

        // Set click listener for the Sign Up text view
        SignUp_TxtV.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_CreateAccount.class);
            startActivity(i);
            finish();
        });

        // Set click listener for the Forgot Password text view
        forgotPasswordTextView.setOnClickListener(v -> {
            // Redirect to another activity that will handle forgot password
            Intent i = new Intent(this, Activity_ForgotPassword.class);
            startActivity(i);
            finish();
        });

        // Set click listener for the password visibility toggle button (eye icon)
        logpassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (logpassword.getRight() - logpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void loginUser() {
        String email = logemail.getText().toString().trim();
        String password = logpassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(Activity_Login.this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please wait while we check your credentials...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // If login is successful, Get SharedPreferences instance
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        // Store the login status as "true" in SharedPreferences
                        editor.putString("account", "true");
                        editor.apply();

                        // An intent to navigate to the main activity
                        Intent intent = new Intent(Activity_Login.this, Activity_BottomNav.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If login fails
                        Toast.makeText(getApplicationContext(), "Failed to log in. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (logpassword.getTransformationMethod() == null) {

            logpassword.setTransformationMethod(new PasswordTransformationMethod());
            logpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_crossed, 0);
        } else {

            logpassword.setTransformationMethod(null);
            logpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye, 0);
        }

        logpassword.setSelection(logpassword.getText().length());
    }
}