package com.example.nurad.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.Models.UserModel;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_CreateAccount extends AppCompatActivity {

    TextView LogIn_TxtV;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox termsAndConditionsCheckbox;
    private Button signUpButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize progress dialog for account creation
        progressDialog = new ProgressDialog(Activity_CreateAccount.this); // Initialize progressDialog here
        progressDialog.setTitle("Creating your Account");
        progressDialog.setMessage("Your account is creating");

        LogIn_TxtV = findViewById(R.id.changeToLog);
        termsAndConditionsCheckbox = findViewById(R.id.termsAndConditionsCheckbox);
        firstNameEditText = findViewById(R.id.FirstN_Etxt);
        lastNameEditText = findViewById(R.id.LastN_Etxt);
        emailEditText = findViewById(R.id.Email_Etxt);
        passwordEditText = findViewById(R.id.Password_Etxt);
        confirmPasswordEditText = findViewById(R.id.ConfirmPassw_Etxt);
        signUpButton = findViewById(R.id.LoginBtn);

        // Change the color of some text within 1 textview
        String text = "Already have an account? Log in";
        SpannableString spannableString = new SpannableString(text);
        int startIndex = text.indexOf("Log in");

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#882065")), startIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        LogIn_TxtV.setText(spannableString);


        LogIn_TxtV.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_Login.class);
            startActivity(i);
            finish();
        });
        termsAndConditionsCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsAndConditionsCheckbox.isChecked()) {
                    showTermsAndConditionsDialog();
                }
            }
        });

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final int DRAWABLE_RIGHT = 2;
                    // Check if touch event is inside the drawable area
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        togglePasswordVisibility(passwordEditText);
                        return true;
                    }
                }
                return false;
            }
        });
        confirmPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final int DRAWABLE_RIGHT = 2;
                    // Check if touch event is inside the drawable area
                    if (event.getRawX() >= (confirmPasswordEditText.getRight() - confirmPasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle password visibility
                        togglePasswordVisibility(confirmPasswordEditText);
                        return true;
                    }
                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void showTermsAndConditionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NURAD's TnC & Privacy Notice");

        // Concatenating Terms and Conditions and Privacy Notice
        String termsAndPrivacyMessage =
                "Terms and Conditions:\n\n" +
                        "1. By using 'The NURAD Manila' mobile application, you agree to abide by the following terms and conditions.\n\n" +
                        "2. The application is intended for the purpose of facilitating hotel bookings, accessing services, and related functionalities.\n\n" +
                        "3. Users are responsible for the accuracy of the information provided during registration and booking processes.\n\n" +
                        "4. 'The NURAD Manila' reserves the right to modify or terminate services provided through the application at any time without prior notice.\n\n" +
                        "5. Users must not engage in any unlawful or unauthorized activities while using the application.\n\n" +
                        "6. The application may contain links to third-party websites or services, and users acknowledge that 'The NURAD Manila' is not responsible for the content or practices of such third parties.\n\n" +
                        "7. Users agree to indemnify and hold harmless 'The NURAD Manila' and its affiliates from any claims or liabilities arising out of their use of the application.\n\n" +
                        "8. These terms and conditions constitute the entire agreement between the user and 'The NURAD Manila' regarding the use of the mobile application.\n\n" +

                        "Privacy Notice:\n\n" +
                        "1. 'The NURAD Manila' respects the privacy of its users and is committed to protecting their personal information.\n\n" +
                        "2. Information collected through the application, such as name, contact details, and booking preferences, is used solely for the purpose of providing and improving services.\n\n" +
                        "3. Personal information provided by users will not be shared with third parties except as necessary to fulfill bookings or as required by law.\n\n" +
                        "4. The application may use cookies or similar technologies to enhance user experience and track usage patterns.\n\n" +
                        "5. Users have the right to access, update, or delete their personal information stored within the application's database.\n\n" +
                        "6. 'The NURAD Manila' implements appropriate security measures to safeguard user data against unauthorized access or disclosure.\n\n" +
                        "7. By using the application, users consent to the collection and processing of their personal information in accordance with this privacy notice.";

        // Set the concatenated message to the dialog
        builder.setMessage(termsAndPrivacyMessage)
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Agree
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Disagree
                        termsAndConditionsCheckbox.setChecked(false);
                        dialog.dismiss();
                    }
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();

        // Enable scrolling for long messages
        TextView messageTextView = dialog.findViewById(android.R.id.message);
        if (messageTextView != null) {
            messageTextView.setMovementMethod(new ScrollingMovementMethod());
        }

        dialog.show();

    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Hide password
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_crossed, 0);
        } else {
            // Show password
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye, 0);
        }
        // Move cursor to end of text
        editText.setSelection(editText.getText().length());
    }

    private void signUp() {
        final String firstName = firstNameEditText.getText().toString().trim();
        final String lastName = lastNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(Activity_CreateAccount.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(Activity_CreateAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if checkbox is checked
        if (!termsAndConditionsCheckbox.isChecked()) {
            Toast.makeText(Activity_CreateAccount.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        progressDialog.show();

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Dismiss progress dialog regardless of success or failure
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        // Sign up success, save user data to Firebase Realtime Database
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String userId = user.getUid();
                        UserModel userModel = new UserModel(firstName, lastName, email, password);

                        // Save user data to Firebase Realtime Database
                        databaseReference.child(userId).setValue(userModel)
                                .addOnCompleteListener(databaseTask -> {
                                    if (databaseTask.isSuccessful()) {
                                        Toast.makeText(Activity_CreateAccount.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        // Redirect to another activity after successful sign up
                                        Intent intent = new Intent(Activity_CreateAccount.this, Activity_Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If saving user data fails, display a message to the user
                                        Toast.makeText(Activity_CreateAccount.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // If sign up fails, display a message to the users
                        Toast.makeText(Activity_CreateAccount.this, "Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}