package com.example.nurad.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.Activities.Activity_CreateAccount;
import com.example.nurad.Activities.Activity_Login;
import com.example.nurad.R;

public class Activity_SignUp extends AppCompatActivity {
    private TextView Login_TxtV;
    private Button SignUp_Btn;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if the user is already logged in
        keepUserLoggedIn();

        // Initialize views
        SignUp_Btn = findViewById(R.id.LoginBtn);
        Login_TxtV = findViewById(R.id.textView4);

        // Change the color of some text within 1 textview
        String text = "Already have an account? Log in";
        SpannableString spannableString = new SpannableString(text);
        int startIndex = text.indexOf("Log in");

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#882065")), startIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Login_TxtV.setText(spannableString);

        // Set click listeners for sign up button
        SignUp_Btn.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_CreateAccount.class);
            startActivity(i);
            finish();
        });

        // Set click listeners for login text view
        Login_TxtV.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_Login.class);
            startActivity(i);
            finish();
        });
    }

    // Check if the user is already logged in
    private void keepUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String check = sharedPreferences.getString("account", "");
        if(check.equals("true")){
            // If user is logged in, directly navigate to the main activity
            Intent intent = new Intent(Activity_SignUp.this, Activity_BottomNav.class);
            startActivity(intent);
            finish();
        }
    }
}