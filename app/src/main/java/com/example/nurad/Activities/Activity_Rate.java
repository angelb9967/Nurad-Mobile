package com.example.nurad.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.CompoundButtonCompat;

import com.example.nurad.Models.Model_Feedback;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity_Rate extends AppCompatActivity {

    private EditText feedbackEditText;
    private Button submitRatingButton;
    private DatabaseReference feedbacksRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        feedbacksRef = FirebaseDatabase.getInstance().getReference().child("Customer Feedback");

        // Find views
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        RadioGroup choicesRadioGroup = findViewById(R.id.choicesRadioGroup);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        submitRatingButton = findViewById(R.id.submitRatingButton);

        // Set default rating to 5
        ratingBar.setRating(5);

        // Set RatingBar progress and background tint colors
        int filledColor = getResources().getColor(R.color.rating_selected_color);
        int unfilledColor = getResources().getColor(R.color.rating_unfilled_color);
        ratingBar.setProgressTintList(ColorStateList.valueOf(filledColor));
        ratingBar.setSecondaryProgressTintList(ColorStateList.valueOf(filledColor));
        ratingBar.setProgressBackgroundTintList(ColorStateList.valueOf(unfilledColor));

        // Set RadioButton color on selection
        for (int i = 0; i < choicesRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) choicesRadioGroup.getChildAt(i);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(Color.parseColor("#882065")));
                } else {
                    CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(Color.parseColor("#000000")));
                }
            });
        }

        // Handle submit button click
        submitRatingButton.setOnClickListener(v -> {
            int selectedRating = (int) ratingBar.getRating();
            int selectedRadioButtonId = choicesRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedOption = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";
            String feedbackMessage = feedbackEditText.getText().toString().trim();

            // Validate input
            if (selectedRating < 1 || TextUtils.isEmpty(selectedOption)) {
                Toast.makeText(Activity_Rate.this, "Please select a rating and option", Toast.LENGTH_SHORT).show();
                return;
            }

            // If feedback message is empty, set default value "N/A"
            if (TextUtils.isEmpty(feedbackMessage)) {
                feedbackMessage = "N/A";
            }

            // Obtain current user ID
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(Activity_Rate.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                return;
            }
            String userId = currentUser.getUid();

            // Check if user has already submitted feedback today
            checkFeedbackSubmission(userId, selectedRating, selectedOption, feedbackMessage);
        });
    }

    private void checkFeedbackSubmission(String userId, int selectedRating, String selectedOption, String feedbackMessage) {
        // Get today's date as yyyyMMdd format
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String todayString = dateFormat.format(today);

        // Query to check if there's already a feedback for today by the current user
        DatabaseReference userFeedbackRef = feedbacksRef.child(userId);
        userFeedbackRef.child(todayString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User has already submitted feedback today
                    showFeedbackLimitDialog();
                } else {
                    // No feedback submitted today, proceed to save feedback
                    saveFeedback(userId, selectedRating, selectedOption, feedbackMessage, todayString);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if any
                Toast.makeText(Activity_Rate.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFeedback(String userId, int selectedRating, String selectedOption, String feedbackMessage, String todayString) {
        // Create Feedback object
        String feedbackId = feedbacksRef.push().getKey(); // Generate unique feedback ID
        Model_Feedback feedback = new Model_Feedback(feedbackId, userId, selectedRating, selectedOption, feedbackMessage, todayString);

        // Save feedback to Firebase Realtime Database
        feedbacksRef.child(userId).child(todayString).setValue(feedback)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Activity_Rate.this, "Feedback Sent", Toast.LENGTH_SHORT).show();
                    finish(); // Finish activity after successful submission
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Activity_Rate.this, "Feedback Not Submitted: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showFeedbackLimitDialog() {
        // Create custom alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_feedback_limit, null);
        builder.setView(dialogView);
        builder.setCancelable(false); // Dialog is not cancelable

        // Find views in dialog layout
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        AppCompatButton okButton = dialogView.findViewById(R.id.btn_ok);

        // Set dialog text
        dialogTitle.setText("Thank you for your feedback!");
        dialogMessage.setText(" A feedback has already been submitted today. You can only submit feedback once per day.");

        // Create the AlertDialog instance
        AlertDialog alertDialog = builder.create();

        // Set click listener for OK button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss dialog
                alertDialog.dismiss();
                finish(); // Close current activity or perform necessary action
            }
        });

        // Show the dialog
        alertDialog.show();
    }
}