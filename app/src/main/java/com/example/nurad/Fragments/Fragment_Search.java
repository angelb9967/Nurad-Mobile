package com.example.nurad.Fragments;

import android.content.Context;
import android.os.Bundle;
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

import com.example.nurad.Activities.Activity_BottomNav;
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
    private OnRoomSelectedListener listener;

    public interface OnRoomSelectedListener {
        void onRoomSelected(RoomModel room);
    }

    public Fragment_Search() {
        // Required empty public constructor
    }

    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRoomSelectedListener) {
            listener = (OnRoomSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRoomSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomList = new ArrayList<>();
        roomTitles = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recommendedTextView = view.findViewById(R.id.recommended);
        viewAllTextView = view.findViewById(R.id.viewAll);
        searchEditText = view.findViewById(R.id.search);
        searchIcon = view.findViewById(R.id.search_icon);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_Room(getContext(), roomList, listener);
        recyclerView.setAdapter(adapter);

        recommendedTextView.setOnClickListener(v -> showRecommendedRooms());
        viewAllTextView.setOnClickListener(v -> showAllRooms());
        searchIcon.setOnClickListener(v -> searchRooms());

        suggestionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, roomTitles);
        searchEditText.setAdapter(suggestionsAdapter);

        fetchRoomData();
    }

    private void fetchRoomData() {
        fetchRoomsFromDatabase("RecommRooms");
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

    private void searchRooms() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            List<RoomModel> searchResults = new ArrayList<>();
            for (RoomModel room : roomList) {
                if (room.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(room);
                }
            }
            if (searchResults.isEmpty()) {
                Toast.makeText(getContext(), "No rooms found matching your query.", Toast.LENGTH_SHORT).show();
            } else {
                adapter.updateData(searchResults);
            }
        }
    }
}