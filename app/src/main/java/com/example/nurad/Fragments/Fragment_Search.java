package com.example.nurad.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Adapters.Adapter_Room;
import com.example.nurad.Models.RoomModel;
import com.example.nurad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search extends Fragment {
    
    private RecyclerView recyclerView;
    private List<RoomModel> roomList;
    private Adapter_Room adapter;
    private TextView recommendedTextView;
    private TextView viewAllTextView;
    private AutoCompleteTextView searchEditText;
    private ImageView searchIcon;
    private ArrayAdapter<String> suggestionsAdapter;
    private List<String> roomTitles;
    private boolean showingRecommended = true;

    public Fragment_Search() {
        // Required empty public constructor
    }

    public static Fragment_Search newInstance(String param1, String param2) {
        Fragment_Search fragment = new Fragment_Search();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle arguments if needed
        }
        roomList = new ArrayList<>();
        roomTitles = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Room(getContext(), roomList);
        recyclerView.setAdapter(adapter);

        recommendedTextView = view.findViewById(R.id.recommended);
        viewAllTextView = view.findViewById(R.id.viewAll);
        searchEditText = view.findViewById(R.id.search);
        searchIcon = view.findViewById(R.id.search_icon);

        recommendedTextView.setOnClickListener(v -> showRecommendedRooms());
        viewAllTextView.setOnClickListener(v -> showAllRooms());

        suggestionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, roomTitles);
        searchEditText.setAdapter(suggestionsAdapter);
        searchEditText.setThreshold(1);

        searchEditText.setOnItemClickListener((parent, view1, position, id) -> {
            searchEditText.setText(suggestionsAdapter.getItem(position));
        });

        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchSuggestions(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        searchIcon.setOnClickListener(v -> performSearch(searchEditText.getText().toString()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showRecommendedRooms();
    }

    private void showRecommendedRooms() {
        showingRecommended = true;
        updateTextViewColors();
        fetchRoomsFromDatabase("RecommRooms");
    }

    private void showAllRooms() {
        showingRecommended = false;
        updateTextViewColors();
        fetchRoomsFromDatabase("AllRooms", "RecommRooms");
    }

    private void updateTextViewColors() {
        if (showingRecommended) {
            recommendedTextView.setTextColor(getResources().getColor(R.color.purple));
            viewAllTextView.setTextColor(getResources().getColor(R.color.grey));
        } else {
            recommendedTextView.setTextColor(getResources().getColor(R.color.grey));
            viewAllTextView.setTextColor(getResources().getColor(R.color.purple));
        }
    }

    private void fetchRoomsFromDatabase(String... referencePaths) {
        roomList.clear();
        roomTitles.clear();
        for (String referencePath : referencePaths) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(referencePath);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RoomModel room = dataSnapshot.getValue(RoomModel.class);
                        if (room != null) {
                            roomList.add(room);
                            roomTitles.add(room.getTitle());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    suggestionsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load rooms.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchSuggestions(String query) {
        List<String> filteredTitles = new ArrayList<>();
        for (String title : roomTitles) {
            if (title.toLowerCase().contains(query.toLowerCase())) {
                filteredTitles.add(title);
            }
        }
        suggestionsAdapter.clear();
        suggestionsAdapter.addAll(filteredTitles);
        suggestionsAdapter.notifyDataSetChanged();
    }

    private void performSearch(String query) {
        List<RoomModel> filteredRooms = new ArrayList<>();
        for (RoomModel room : roomList) {
            if (room.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredRooms.add(room);
            }
        }
        if (filteredRooms.isEmpty()) {
            // No results found, display a Toast
            Toast.makeText(getContext(), "No results found for your search.", Toast.LENGTH_SHORT).show();
        }
        adapter.updateData(filteredRooms);
    }
}