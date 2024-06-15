package com.example.nurad.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nurad.Activities.Activity_SignUp;
import com.example.nurad.Adapters.Adapter_ClaimedVouchers;
import com.example.nurad.Adapters.Adapter_Vouchers;
import com.example.nurad.Models.ClaimedVouchersModel;
import com.example.nurad.Models.VoucherModel;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Account extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button logoutBtn;
    private ImageView profileImage;
    public static final String SHARED_PREFS = "sharedPrefs";
    private RecyclerView recyclerView;
    private TextView userNameTextView, placeholderTextView;
    private Adapter_Vouchers voucherAdapter;
    private List<VoucherModel> voucherList;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private Uri selectedImageUri;
    private RecyclerView claimedRecyclerView;
    private Adapter_ClaimedVouchers claimedVouchersAdapter;
    private List<ClaimedVouchersModel> claimedVouchersList;

    public Fragment_Account() {
        // Required empty public constructor
    }

    public static Fragment_Account newInstance(String param1, String param2) {
        Fragment_Account fragment = new Fragment_Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__account, container, false);

        // Initialize views
        logoutBtn = view.findViewById(R.id.LogoutBtn);
        profileImage = view.findViewById(R.id.profileImages);
        recyclerView = view.findViewById(R.id.recycler_view_vouchers);
        userNameTextView = view.findViewById(R.id.user_name);
        claimedRecyclerView = view.findViewById(R.id.claimed_recycler_view);
        placeholderTextView = view.findViewById(R.id.placeholder_textview2); // Find the placeholder TextView

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        claimedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        voucherList = new ArrayList<>();
        claimedVouchersList = new ArrayList<>();
        voucherAdapter = new Adapter_Vouchers(voucherList);
        claimedVouchersAdapter = new Adapter_ClaimedVouchers(claimedVouchersList, placeholderTextView); // Pass placeholderTextView
        recyclerView.setAdapter(voucherAdapter);
        claimedRecyclerView.setAdapter(claimedVouchersAdapter);

        // Load vouchers from Firebase
        loadVouchersFromFirebase();

        // Load claimed vouchers from Firebase
        loadClaimedVouchersFromFirebase();

        // Load user data from Firebase and display name
        loadUserDataAndDisplayName();

        // Set click listener for logout button
        logoutBtn.setOnClickListener(view_ -> showLogoutConfirmationDialog());

        // Set click listener for profile image
        profileImage.setOnClickListener(view_ -> openImagePicker());

        // Load profile image
        loadProfileImage();

        return view;
    }

    // Open image picker to choose a profile image
    private void openImagePicker() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
    }

    // Handle result of picking an image from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            uploadProfileImage(selectedImageUri);
        }
    }

    // Upload profile image to Firebase Storage
    private void uploadProfileImage(Uri imageUri) {
        if (imageUri != null) {
            // Define the storage reference
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child("profileImages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            // Upload image to Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the uploaded image URL
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the image URL to the database
                            saveImageToDatabase(uri);
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle image upload failure
                        Toast.makeText(getContext(), "Failed to upload profile image.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Load claimed vouchers from Firebase
    private void loadClaimedVouchersFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference claimedVouchersRef = FirebaseDatabase.getInstance().getReference()
                .child("UserVouchers").child(userId);

        claimedVouchersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                claimedVouchersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String status = snapshot.child("status").getValue(String.class);
                    if (status != null && status.equals("Used")) {
                        String code = snapshot.child("code").getValue(String.class);
                        String usedDate = snapshot.child("claimedDate").getValue(String.class);
                        if (code != null && usedDate != null) {
                            ClaimedVouchersModel claimedVoucher = new ClaimedVouchersModel(code, status, usedDate);
                            claimedVouchersList.add(claimedVoucher);
                        }
                    }
                }

                if (claimedVouchersList.isEmpty()) {
                    placeholderTextView.setVisibility(View.VISIBLE);
                } else {
                    placeholderTextView.setVisibility(View.GONE);
                }

                claimedVouchersAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load claimed vouchers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save the image URL to the database
    private void saveImageToDatabase(Uri imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .child("profileImage").setValue(imageUrl.toString())
                .addOnSuccessListener(aVoid -> {
                    // Reload profile image
                    loadProfileImage();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(getContext(), "Failed to save image to database", Toast.LENGTH_SHORT).show();
                });
    }

    // Load profile image from Firebase Storage and display in ImageView
    private void loadProfileImage() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String profileImageUrl = snapshot.child("profileImage").getValue(String.class);
                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(requireContext())
                                        .load(profileImageUrl)
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(profileImage);
                            } else {
                                profileImage.setImageResource(R.drawable.user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(getContext(), "Failed to retrieve profile image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Load vouchers from Firebase
    private void loadVouchersFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Vouchers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                voucherList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VoucherModel voucher = snapshot.getValue(VoucherModel.class);
                    if (voucher != null) {
                        voucher.setKey(snapshot.getKey()); // Set the key
                        voucherList.add(voucher);
                    }
                }
                voucherAdapter.setVoucherList(voucherList); // Update the adapter with the new list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load vouchers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the user account information from SharedPreferences
        editor.putString("account", "");
        editor.apply();

        // Go back to Sign up Activity
        Intent intent = new Intent(getActivity(), Activity_SignUp.class);
        startActivity(intent);

        // Finish the current activity (fragment's hosting activity)
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    // Load user data from Firebase and display name
    private void loadUserDataAndDisplayName() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String firstName = snapshot.child("firstName").getValue(String.class);
                            String lastName = snapshot.child("lastName").getValue(String.class);
                            String fullName = firstName + " " + lastName;
                            userNameTextView.setText(fullName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(getContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}