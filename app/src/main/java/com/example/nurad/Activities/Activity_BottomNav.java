package com.example.nurad.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.nurad.Fragments.Fragment_Account;
import com.example.nurad.Fragments.Fragment_AboutUs;
import com.example.nurad.Fragments.Fragment_Booking;
import com.example.nurad.Fragments.Fragment_Search;
import com.example.nurad.Models.RoomModel;
import com.example.nurad.R;
import com.example.nurad.databinding.ActivityBottomNavBinding;

public class Activity_BottomNav extends AppCompatActivity implements Fragment_Search.OnRoomSelectedListener {

    private ActivityBottomNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if the intent extra indicates a specific fragment to navigate to
        if (getIntent().hasExtra("navigateTo") && "account".equals(getIntent().getStringExtra("navigateTo"))) {
            replaceFragment(new Fragment_Account());
            updateSelectedNavItem(new Fragment_Account());
        } else {
            // Set the initial fragment (default) to display when the activity is created
            replaceFragment(new Fragment_Search());
        }

        // Set listener for bottom navigation view to handle item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.search) {
                replaceFragment(new Fragment_Search());
            } else if (item.getItemId() == R.id.booking) {
                replaceFragment(new Fragment_Booking());
            } else if (item.getItemId() == R.id.aboutus) {
                replaceFragment(new Fragment_AboutUs());
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new Fragment_Account());
            }
            return true;
        });
    }

    // Replaces the currently displayed fragment with the specified fragment.
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    @Override
    public void onRoomSelected(RoomModel room) {
        Fragment_Booking fragmentBooking = Fragment_Booking.newInstance(room);
        replaceFragment(fragmentBooking);
        updateSelectedNavItem(fragmentBooking);
    }

    // Method to update the selected item in the BottomNavigationView based on the current fragment
    public void updateSelectedNavItem(Fragment fragment) {
        if (fragment instanceof Fragment_Search) {
            binding.bottomNavigationView.setSelectedItemId(R.id.search);
        } else if (fragment instanceof Fragment_Booking) {
            binding.bottomNavigationView.setSelectedItemId(R.id.booking);
        } else if (fragment instanceof Fragment_AboutUs) {
            binding.bottomNavigationView.setSelectedItemId(R.id.aboutus);
        } else if (fragment instanceof Fragment_Account) {
            binding.bottomNavigationView.setSelectedItemId(R.id.account);
        }
    }
}