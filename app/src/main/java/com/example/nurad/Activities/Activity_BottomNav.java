
package com.example.nurad.Activities;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurad.Fragments.Fragment_AboutUs;
import com.example.nurad.Fragments.Fragment_Account;
import com.example.nurad.Fragments.Fragment_Booking;
import com.example.nurad.Fragments.Fragment_Search;
import com.example.nurad.R;
import com.example.nurad.databinding.ActivityBottomNavBinding;

public class Activity_BottomNav extends AppCompatActivity {

    private ActivityBottomNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the initial fragment (default) to display when the activity is created
        replaceFragment(new Fragment_Search());

        // Set listener for bottom navigation view to handle item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // Check which menu item is selected and replace the current fragment accordingly
            if (item.getItemId() == R.id.search) {
                replaceFragment(new Fragment_Search());
            } else if (item.getItemId() == R.id.booking){
                replaceFragment(new Fragment_Booking());
            } else if (item.getItemId() == R.id.aboutus){
                replaceFragment(new Fragment_AboutUs());
            } else if (item.getItemId() == R.id.account){
                replaceFragment(new Fragment_Account());
            }
            return true;
        });
    }

    // Replaces the currently displayed fragment with the specified fragment.
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
