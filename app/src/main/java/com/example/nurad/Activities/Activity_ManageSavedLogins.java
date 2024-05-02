package com.example.nurad.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Adapters.Adapter_Account;
import com.example.nurad.Interfaces.RecyclerViewInterface;
import com.example.nurad.Models.AccountModel;
import com.example.nurad.R;
import com.example.nurad.Utilities.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Set;

public class Activity_ManageSavedLogins extends AppCompatActivity implements RecyclerViewInterface {
    public static final String SHARED_PREFS = "sharedPrefs";
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    ArrayList<AccountModel> list;
    Adapter_Account adapter;
    private TextView LoginAcc_TxtV;
    private TextView CreateAcc_TxtV;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_saved_logins);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize views
        LoginAcc_TxtV = findViewById(R.id.LoginAcc_TxtV);
        CreateAcc_TxtV = findViewById(R.id.CreateAcc_TxtV);
        recyclerView = findViewById(R.id.recyclerView);

        populateList();
        adapter = new Adapter_Account(this, this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Set click listeners for  log in into another account text view
        LoginAcc_TxtV.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_Login.class);
            startActivity(i);
            finish();
        });

        // Set click listeners for create account text view
        CreateAcc_TxtV.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_CreateAccount.class);
            startActivity(i);
            finish();
        });
    }

    private void populateList(){
        list = new ArrayList<>();
        Set<String> savedEmails = PreferenceUtils.getSavedEmails(this);
        for (String email : savedEmails) {
            list.add(new AccountModel(email));
        }
    }

    @Override
    public void OnItemClick(int position) {
        String email = list.get(position).getEmail();
        String password = PreferenceUtils.getPasswordForEmail(Activity_ManageSavedLogins.this, email);

        progressDialog = new ProgressDialog(Activity_ManageSavedLogins.this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please wait while we check your credentials...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // If login is successful
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("account", "true"); // Store the login status of the account as "true" in SharedPreferences
                        editor.apply();

                        moveToTheNextActivity(email);
                    } else {
                        // If login fails
                        Toast.makeText(getApplicationContext(), "Failed to log in.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void moveToTheNextActivity(String email){
        Intent intent = new Intent(Activity_ManageSavedLogins.this, Activity_BottomNav.class);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "Account: " + email, Toast.LENGTH_SHORT).show();
    }
}