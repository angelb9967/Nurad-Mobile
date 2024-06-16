package com.example.nurad.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Adapters.BookingsAdapter;
import com.example.nurad.Models.Model_Booking;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_BookingsInvoice extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingsAdapter adapter;
    private List<Model_Booking> bookingList;
    private DatabaseReference bookingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_invoice);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingList = new ArrayList<>();

        // Initialize Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            bookingsRef = FirebaseDatabase.getInstance().getReference("Booking");

            // Query bookings for the current user
            bookingsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model_Booking booking = dataSnapshot.getValue(Model_Booking.class);
                        if (booking != null) {
                            bookingList.add(booking);
                        }
                    }
                    // Update RecyclerView
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error if needed
                    Toast.makeText(Activity_BookingsInvoice.this, "Failed to retrieve bookings.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        adapter = new BookingsAdapter(bookingList, this);
        recyclerView.setAdapter(adapter);
    }
}