package com.example.nurad.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurad.Activities.Activity_BottomNav;
import com.example.nurad.Models.RoomModel;
import com.example.nurad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Fragment_Booking extends Fragment {

    private RoomModel selectedRoom;
    private TextView roomDetailsTextView;
    private TextView roomNameTextView;
    private TextView roomTitleTextView;
    private TextView roomTypeTextView;
    private TextView roomPriceTextView;
    private CheckBox applyVoucherCheckBox;
    private EditText voucherEditText;
    private Button nextStepButton;
    private Button checkInButton;
    private Button checkOutButton;
    private TextView checkInDateTextView;
    private TextView checkOutDateTextView;
    private Button guestOptionsButton;
    private TextView guestsTextView;
    private int adultCount = 1;
    private int childrenCount = 0;

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
        if (getArguments() != null) {
            selectedRoom = (RoomModel) getArguments().getSerializable("selectedRoom");
        }
        Log.d("Fragment_Booking", "Selected Room: " + selectedRoom);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__booking, container, false);

        initializeViews(view);

        // Check if selectedRoom is null
        if (selectedRoom == null || !isRoomSelected(selectedRoom.getRoomName())) {
            // Disable the next step button
            nextStepButton.setEnabled(false);
            // Show a message indicating that a room needs to be selected first
            Toast.makeText(getContext(), "Please choose a room before proceeding.", Toast.LENGTH_SHORT).show();
            return view;
        }

        populateRoomDetails();
        setupEventListeners();

        return view;
    }

    private void initializeViews(View view) {
        roomTitleTextView = view.findViewById(R.id.roomTitle);
        roomDetailsTextView = view.findViewById(R.id.roomDetailsTextView);
        roomNameTextView = view.findViewById(R.id.roomNameTextView);
        roomTypeTextView = view.findViewById(R.id.roomTypeTextView);
        roomPriceTextView = view.findViewById(R.id.roomPriceTextView);
        applyVoucherCheckBox = view.findViewById(R.id.applyVoucherCheckBox);
        voucherEditText = view.findViewById(R.id.voucherEditText);
        nextStepButton = view.findViewById(R.id.nextStepButton);
        checkInButton = view.findViewById(R.id.checkInButton);
        checkOutButton = view.findViewById(R.id.checkOutButton);
        checkInDateTextView = view.findViewById(R.id.checkInDateTextView);
        checkOutDateTextView = view.findViewById(R.id.checkOutDateTextView);
        guestOptionsButton = view.findViewById(R.id.guestOptionsButton);
        guestsTextView = view.findViewById(R.id.guestsTextView);
    }

    private void populateRoomDetails() {
        // Check if roomNameTextView is not null and room name is not empty
        if (roomNameTextView != null && selectedRoom != null && selectedRoom.getRoomName() != null && !selectedRoom.getRoomName().isEmpty()) {
            roomNameTextView.setText("Room Number: " + selectedRoom.getRoomName() + "\n");
        }

        roomTitleTextView.setText(String.valueOf(selectedRoom.getTitle()));

        // Add line breaks between each piece of information
        roomTypeTextView.setText("Room Type: "+String.valueOf(selectedRoom.getRoomType()) + "\n");
        roomPriceTextView.setText("Price: "+String.valueOf(selectedRoom.getPrice()) + "\n");
        roomDetailsTextView.setText("Description: "+String.valueOf(selectedRoom.getDescription() + "\n"));
    }

    private void setupEventListeners() {
        applyVoucherCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                CompoundButtonCompat.setButtonTintList(applyVoucherCheckBox, ColorStateList.valueOf(Color.parseColor("#882065")));
                voucherEditText.setVisibility(View.VISIBLE);
            } else {
                CompoundButtonCompat.setButtonTintList(applyVoucherCheckBox, ColorStateList.valueOf(Color.parseColor("#000000")));
                voucherEditText.setVisibility(View.GONE);
            }
        });

        checkInButton.setOnClickListener(v -> showDatePickerDialog((date, month, year) -> {
            String checkInDate = (month + 1) + "/" + date + "/" + year;
            checkInDateTextView.setText(checkInDate);
        }));

        checkOutButton.setOnClickListener(v -> showDatePickerDialog((date, month, year) -> {
            String checkOutDate = (month + 1) + "/" + date + "/" + year;
            checkOutDateTextView.setText(checkOutDate);
        }));

        nextStepButton.setOnClickListener(v -> {
            if (validateStep1()) {
                updateStepIndicators(2);
            } else {
                Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            }
        });

        guestOptionsButton.setOnClickListener(v -> showGuestOptionsDialog());
    }

    private void showDatePickerDialog(DateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth);
            if (selectedDate.before(Calendar.getInstance())) {
                Toast.makeText(getContext(), "Please select a valid date.", Toast.LENGTH_SHORT).show();
            } else {
                listener.onDateSet(dayOfMonth, month1, year1);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private boolean isRoomSelected(String roomName) {
        DatabaseReference recommRoomsRef = FirebaseDatabase.getInstance().getReference("RecommRooms").child(roomName);
        DatabaseReference allRoomsRef = FirebaseDatabase.getInstance().getReference("AllRooms").child(roomName);

        // Check if the room exists in RecommRooms or AllRooms
        return (recommRoomsRef != null) || (allRoomsRef != null);
    }

    private void showGuestOptionsDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.guest_option_dialog);

        TextView adultCountTextView = dialog.findViewById(R.id.adultCountTextView);
        TextView childrenCountTextView = dialog.findViewById(R.id.childrenCountTextView);
        Button increaseAdultsButton = dialog.findViewById(R.id.increaseAdultsButton);
        Button decreaseAdultsButton = dialog.findViewById(R.id.decreaseAdultsButton);
        Button increaseChildrenButton = dialog.findViewById(R.id.increaseChildrenButton);
        Button decreaseChildrenButton = dialog.findViewById(R.id.decreaseChildrenButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button applyButton = dialog.findViewById(R.id.applyButton);

        adultCountTextView.setText(String.valueOf(adultCount));
        childrenCountTextView.setText(String.valueOf(childrenCount));

        increaseAdultsButton.setOnClickListener(v -> {
            adultCount++;
            adultCountTextView.setText(String.valueOf(adultCount));
        });

        decreaseAdultsButton.setOnClickListener(v -> {
            if (adultCount > 1) {
                adultCount--;
                adultCountTextView.setText(String.valueOf(adultCount));
            }
        });

        increaseChildrenButton.setOnClickListener(v -> {
            childrenCount++;
            childrenCountTextView.setText(String.valueOf(childrenCount));
        });

        decreaseChildrenButton.setOnClickListener(v -> {
            if (childrenCount > 0) {
                childrenCount--;
                childrenCountTextView.setText(String.valueOf(childrenCount));
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        applyButton.setOnClickListener(v -> {
            guestsTextView.setText("Adults: " + adultCount + ", Children: " + childrenCount);
            dialog.dismiss();
        });

        dialog.show();
    }

    private interface DateSetListener {
        void onDateSet(int date, int month, int year);
    }

    private boolean validateStep1() {
        return !checkInDateTextView.getText().toString().isEmpty() &&
                !checkOutDateTextView.getText().toString().isEmpty();
    }

    private void updateStepIndicators(int step) {
        // Update visibility of step indicators based on the current step
        TextView step1 = getView().findViewById(R.id.step1);
        TextView step2 = getView().findViewById(R.id.step2);
        TextView step3 = getView().findViewById(R.id.step3);
        TextView step4 = getView().findViewById(R.id.step4);
        TextView step5 = getView().findViewById(R.id.step5);

        // Reset all steps to be invisible
        step1.setVisibility(View.INVISIBLE);
        step2.setVisibility(View.INVISIBLE);
        step3.setVisibility(View.INVISIBLE);
        step4.setVisibility(View.INVISIBLE);
        step5.setVisibility(View.INVISIBLE);

        // Set steps based on the current step
        switch (step) {
            case 5:
                step5.setVisibility(View.VISIBLE);
                break;
            case 4:
                step4.setVisibility(View.VISIBLE);
                break;
            case 3:
                step3.setVisibility(View.VISIBLE);
                break;
            case 2:
                step2.setVisibility(View.VISIBLE);
                break;
            case 1:
                step1.setVisibility(View.VISIBLE);
                break;
            default:
                // Handle invalid step
                break;
        }
    }
}