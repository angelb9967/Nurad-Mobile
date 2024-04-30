package com.example.nurad.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nurad.Activities.Activity_CreateAccount;
import com.example.nurad.Activities.Activity_Login;
import com.example.nurad.Activities.Activity_SignUp;
import com.example.nurad.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button logoutBtn;
    public static final String SHARED_PREFS = "sharedPrefs";
    public Fragment_Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Account.
     */
    // TODO: Rename and change types and number of parameters
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

        // Set click listener for logout button
        logoutBtn.setOnClickListener(view_ -> logoutUser());

        return view;
    }

    private void logoutUser() {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // Get SharedPreferences editor
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
}