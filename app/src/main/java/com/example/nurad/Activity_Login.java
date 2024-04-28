package com.example.nurad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_Login extends AppCompatActivity {
    TextView SignUp_TxtV;
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

        SignUp_TxtV = findViewById(R.id.textView7);

        // Change the color of some text within 1 textview
        String text = "Don't have an account yet? Sign Up";
        SpannableString spannableString = new SpannableString(text);
        int startIndex = text.indexOf("Sign Up");

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#882065")), startIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SignUp_TxtV.setText(spannableString);


        SignUp_TxtV.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_CreateAccount.class);
            startActivity(i);
            finish();
        });
    }
}