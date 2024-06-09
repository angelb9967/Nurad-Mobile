package com.example.nurad.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Adapters.Adapter_AddOn;
import com.example.nurad.Models.Model_AddOns;
import com.example.nurad.Models.Model_PriceRule;
import com.example.nurad.Models.RoomModel;
import com.example.nurad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Fragment_Booking extends Fragment {

    private RoomModel selectedRoom;
    private DatabaseReference priceRules_DBref;
    private TextView roomDetailsTextView;
    private TextView roomNameTextView;
    private TextView roomTitleTextView;
    private TextView roomTypeTextView;
    private TextView roomPriceTextView;
    private Context mContext;
    private List<String> availableCheckInTimes;
    private double fetchedPrice;
    private DatabaseReference addOnsDBRef;
    private RecyclerView recyclerView;
    private Adapter_AddOn adapter;

    private Button nextStepButton;

    public Fragment_Booking() {
        // Required empty public constructor
    }

    public static Fragment_Booking newInstance(RoomModel room) {
        Fragment_Booking fragment = new Fragment_Booking();
        Bundle args = new Bundle();
        args.putSerializable("selectedRoom", room);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addOnsDBRef = FirebaseDatabase.getInstance().getReference("AddOns");

        fetchAddOnsFromDatabase();

        priceRules_DBref = FirebaseDatabase.getInstance().getReference("Price Rules");
        if (getArguments() != null) {
            selectedRoom = (RoomModel) getArguments().getSerializable("selectedRoom");
            if (selectedRoom != null) {
                checkRoomInFirebase(selectedRoom.getRoomName());
                if (selectedRoom.getPriceRule() != null) {
                    fetchPriceRule(selectedRoom.getPriceRule());
                }
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__booking, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        displayAddOns(new ArrayList<>());

        initializeViews(view);

        // Check if selectedRoom is null
        if (selectedRoom == null || selectedRoom.getRoomName() == null || selectedRoom.getRoomName().isEmpty()) {
            nextStepButton.setEnabled(false);
            Toast.makeText(getContext(), "Please make sure to choose a room before proceeding.", Toast.LENGTH_SHORT).show();
            return view;
        }

        populateRoomDetails();
        setupEventListeners();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Store the context when the Fragment is attached
        mContext = context;
    }

    private void initializeViews(View view) {
        roomTitleTextView = view.findViewById(R.id.roomTitle);
        roomDetailsTextView = view.findViewById(R.id.roomDetailsTextView);
        roomNameTextView = view.findViewById(R.id.roomNameTextView);
        roomTypeTextView = view.findViewById(R.id.roomTypeTextView);
        roomPriceTextView = view.findViewById(R.id.roomPriceTextView);
        availableCheckInTimes = new ArrayList<>();
        nextStepButton = view.findViewById(R.id.nextStepButton);
    }

    private void fetchAddOnsFromDatabase() {
        addOnsDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Model_AddOns> addOnList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model_AddOns addOn = dataSnapshot.getValue(Model_AddOns.class);
                    if (addOn != null) {
                        addOnList.add(addOn);
                    }
                }
                displayAddOns(addOnList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(mContext, "Could Not Fetch the Add Ons", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAddOns(List<Model_AddOns> addOnList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter_AddOn(getContext(), addOnList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchPriceRule(String priceRuleName) {
        priceRules_DBref.child(priceRuleName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Model_PriceRule priceRule = snapshot.getValue(Model_PriceRule.class);
                    if (priceRule != null) {
                        // Get the appropriate price based on the current day of the week
                        double price = getPriceForCurrentDay(priceRule);
                        String formattedPrice = formatPrice(price);
                        roomPriceTextView.setText("Price: ₱" + formattedPrice);
                    } else {
                        // Handle null price rule scenario
                        roomPriceTextView.setText("Price: ₱" + formatPrice(0));
                    }
                } else {
                    // Handle non-existent price rule scenario
                    roomPriceTextView.setText("Price: ₱" + formatPrice(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error scenario
                roomPriceTextView.setText("Price: ₱" + formatPrice(0));
            }
        });
    }

    private void populateRoomDetails() {
        if (roomNameTextView != null && selectedRoom != null && selectedRoom.getRoomName() != null && !selectedRoom.getRoomName().isEmpty()) {
            roomNameTextView.setText("Room Number: " + selectedRoom.getRoomName() + "\n");
        }

        roomTitleTextView.setText(selectedRoom.getTitle());
        roomTypeTextView.setText("Room Type: " + selectedRoom.getRoomType() + "\n");
        roomDetailsTextView.setText("Description: " + selectedRoom.getDescription() + "\n");
    }

    private void setupEventListeners() {


    }

    private double getPriceForCurrentDay(Model_PriceRule priceRule) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.FRIDAY:
                return priceRule.getFriday_price();
            case Calendar.SATURDAY:
                return priceRule.getSaturday_price();
            case Calendar.SUNDAY:
                return priceRule.getSunday_price();
            default:
                return priceRule.getPrice();
        }
    }

    private String formatPrice(double price) {
        return String.format(Locale.US, "%.2f", price);
    }

    private void checkRoomInFirebase(String roomName) {
        DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference();

        roomsRef.child("AllRooms").child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("Fragment_Booking", "Selected Room Found in AllRooms: " + roomName);
                    // Use mContext to display the Toast message
                    if (mContext != null) {
                        Toast.makeText(mContext, roomName + " is now chosen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Check in "RecommRooms"
                    roomsRef.child("RecommRooms").child(roomName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.d("Fragment_Booking", "Selected Room Found in RecommRooms: " + roomName);
                                // Use mContext to display the Toast message
                                if (mContext != null) {
                                    Toast.makeText(mContext, roomName + " is now chosen", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("Fragment_Booking", "Selected Room Not Found: " + roomName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("Fragment_Booking", "loadPost:onCancelled", error.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Fragment_Booking", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private interface DatePickerListener {
        void onDateSet(int date, int month, int year);
    }
}