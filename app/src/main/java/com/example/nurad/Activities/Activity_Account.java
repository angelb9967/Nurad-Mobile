package com.example.nurad.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nurad.Models.UserModel;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Activity_Account extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private ImageView profileImageView;
    private Button updateButton;
    private Uri selectedImageUri;
    private boolean isPasswordVisible = false;
    private String currentProfileImageUrl;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private static final int REQUEST_CODE_PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        firstNameEditText = findViewById(R.id.FirstN_Etxt);
        lastNameEditText = findViewById(R.id.LastN_Etxt);
        emailEditText = findViewById(R.id.Email_Etxt);
        passwordEditText = findViewById(R.id.Password_Etxt);
        profileImageView = findViewById(R.id.profileImages);
        updateButton = findViewById(R.id.submitRatingButton);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages").child(auth.getCurrentUser().getUid());

        loadUserData();

        profileImageView.setOnClickListener(v -> openImagePicker());
        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = passwordEditText.getCompoundDrawables()[2];
                if (drawableEnd != null && event.getRawX() >= (passwordEditText.getRight() - drawableEnd.getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
        updateButton.setOnClickListener(v -> updateUserData());
    }

    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                if (user != null) {
                    firstNameEditText.setText(user.getFirstName());
                    lastNameEditText.setText(user.getLastName());
                    emailEditText.setText(user.getEmail());
                    passwordEditText.setText(user.getPassword());

                    currentProfileImageUrl = snapshot.child("profileImage").getValue(String.class);
                    if (currentProfileImageUrl != null && !currentProfileImageUrl.isEmpty()) {
                        Glide.with(Activity_Account.this)
                                .load(currentProfileImageUrl)
                                .placeholder(R.drawable.user)
                                .error(R.drawable.user)
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_Account.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri); // Preview selected image
        }
    }

    private void uploadProfileImage(Uri imageUri, OnImageUploadListener listener) {
        if (imageUri != null) {
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        listener.onSuccess(uri.toString());
                    }))
                    .addOnFailureListener(e -> {
                        runOnUiThread(() -> Toast.makeText(Activity_Account.this, "Failed to upload profile image.", Toast.LENGTH_SHORT).show());
                        listener.onFailure();
                    });
        } else {
            listener.onSuccess(currentProfileImageUrl); // Use existing image URL if no new image is selected
        }
    }

    private void updateUserData() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        uploadProfileImage(selectedImageUri, new OnImageUploadListener() {
            @Override
            public void onSuccess(String imageUrl) {
                UserModel user = new UserModel(firstName, lastName, email, password);
                databaseReference.setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        databaseReference.child("profileImage").setValue(imageUrl);
                        runOnUiThread(() -> Toast.makeText(Activity_Account.this, "User data updated successfully", Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(Activity_Account.this, "Failed to update user data", Toast.LENGTH_SHORT).show());
                    }
                });
            }

            @Override
            public void onFailure() {
                runOnUiThread(() -> Toast.makeText(Activity_Account.this, "Failed to update user data", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye, 0);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_crossed, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.length());
    }

    private interface OnImageUploadListener {
        void onSuccess(String imageUrl);
        void onFailure();
    }
}