package com.example.nurad.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nurad.Adapters.Adapter_AboutUs;
import com.example.nurad.Adapters.Adapter_Board;
import com.example.nurad.Models.AboutUsModel;
import com.example.nurad.Models.BoardModel;
import com.example.nurad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Fragment_AboutUs extends Fragment {

    private RecyclerView recyclerViewAboutUs;
    private Adapter_AboutUs aboutUsAdapter;
    private List<AboutUsModel> aboutUsList;

    private RecyclerView recyclerViewBoard;
    private Adapter_Board boardAdapter;
    private List<BoardModel> boardList;
    private Button bookNow;

    public Fragment_AboutUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__about_us, container, false);

        // About Us RecyclerView
        recyclerViewAboutUs = view.findViewById(R.id.aboutUsRecyclerView);
        recyclerViewAboutUs.setLayoutManager(new LinearLayoutManager(getContext()));
        aboutUsList = new ArrayList<>();
        aboutUsAdapter = new Adapter_AboutUs(aboutUsList);
        recyclerViewAboutUs.setAdapter(aboutUsAdapter);
        bookNow = view.findViewById(R.id.bookNowButton);

        // Set OnClickListener for the "Book Now" button
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with Fragment_Search
                Fragment_Search fragmentSearch = Fragment_Search.newInstance();
                if (fragmentSearch != null && getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, fragmentSearch) // Replace R.id.container with the ID of your container
                            .addToBackStack(null)
                            .commit();
                } else {
                    // Log an error or show a toast if fragmentSearch or getActivity() is null
                    Log.e("Fragment_AboutUs", "Fragment_Search instance or getActivity() is null");
                    Toast.makeText(getContext(), "Error: Cannot navigate to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Fetch data from Firebase for About Us section
        DatabaseReference aboutUsReference = FirebaseDatabase.getInstance().getReference().child("About");
        aboutUsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aboutUsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AboutUsModel aboutUsModel = snapshot.getValue(AboutUsModel.class);
                    aboutUsList.add(aboutUsModel);
                }
                aboutUsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                String errorMessage = databaseError.getMessage();
                // Display an error message to the user
                Toast.makeText(getContext(), "Failed to retrieve About Us data: " + errorMessage, Toast.LENGTH_SHORT).show();
                // Optionally, log the error for debugging purposes
                Log.e("Firebase Error", errorMessage);
            }
        });

        // Board of Directors RecyclerView
        recyclerViewBoard = view.findViewById(R.id.boardRecyclerView);
        recyclerViewBoard.setLayoutManager(new LinearLayoutManager(getContext()));
        boardList = new ArrayList<>();
        boardAdapter = new Adapter_Board(boardList);
        recyclerViewBoard.setAdapter(boardAdapter);

        // Fetch data from Firebase for Board of Directors section
        DatabaseReference boardReference = FirebaseDatabase.getInstance().getReference().child("Board");
        boardReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boardList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BoardModel boardModel = snapshot.getValue(BoardModel.class);
                    boardList.add(boardModel);
                }
                boardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                String errorMessage = databaseError.getMessage();
                // Display an error message to the user
                Toast.makeText(getContext(), "Failed to retrieve Board of Directors data: " + errorMessage, Toast.LENGTH_SHORT).show();
                // Optionally, log the error for debugging purposes
                Log.e("Firebase Error", errorMessage);
            }
        });

        return view;
    }
}