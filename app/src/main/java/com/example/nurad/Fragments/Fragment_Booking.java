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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

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

    private TextView checkTimeTextView;
    private DatabaseReference priceRules_DBref;
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
    private Context mContext;
    private int adultCount = 1;
    private int childrenCount = 0;
    private Spinner checkInTimeSpinner;
    private List<String> availableCheckInTimes;
    private double fetchedPrice;

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

        initializeViews(view);

        // Check if selectedRoom is null
        if (selectedRoom == null || selectedRoom.getRoomName() == null || selectedRoom.getRoomName().isEmpty()) {
            nextStepButton.setEnabled(false);
            Toast.makeText(getContext(), "Please make sure to choose a room before proceeding.", Toast.LENGTH_SHORT).show();
            return view;
        }

        populateRoomDetails();
        fetchAvailableCheckInTimes(); // Fetch available check-in times before setting the spinner adapter
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
        applyVoucherCheckBox = view.findViewById(R.id.applyVoucherCheckBox);
        voucherEditText = view.findViewById(R.id.voucherEditText);
        nextStepButton = view.findViewById(R.id.nextStepButton);
        checkInButton = view.findViewById(R.id.checkInButton);
        checkOutButton = view.findViewById(R.id.checkOutButton);
        checkInDateTextView = view.findViewById(R.id.checkInDateTextView);
        checkOutDateTextView = view.findViewById(R.id.checkOutSetTimeTextView);
        checkTimeTextView = view.findViewById(R.id.checkTimeTextView);
        guestOptionsButton = view.findViewById(R.id.guestOptionsButton);
        guestsTextView = view.findViewById(R.id.guestsTextView);
        checkInTimeSpinner = view.findViewById(R.id.checkInTimeSpinner);
        availableCheckInTimes = new ArrayList<>();
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

            // Get the selected check-in time
            String selectedCheckInTime = availableCheckInTimes.get(checkInTimeSpinner.getSelectedItemPosition());

            // Parse the selected check-in time to extract hour and minute
            String[] checkInTimeParts = selectedCheckInTime.split(":");
            int checkInHour = Integer.parseInt(checkInTimeParts[0]);
            int checkInMinute = Integer.parseInt(checkInTimeParts[1].split(" ")[0]);

            // Set check-out date and time
            setCheckOutDateTime(year, month, date, checkInHour, checkInMinute);
        }));

        // Inside setupEventListeners() method
        checkInTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!availableCheckInTimes.isEmpty()) {
                    String selectedTime = availableCheckInTimes.get(position);
                    // Do something with the selected time if needed
                    // For example, you can display it or use it in further processing
                    Log.d("Fragment_Booking", "Selected time: " + selectedTime);

                    // Update checkTimeTextView with the selected time
                    checkTimeTextView.setText(selectedTime);

                    // Update check-out time based on selected check-in time
                    updateCheckOutTime(selectedTime);
                } else {
                    // Handle case where no time is available
                    // You might want to inform the user or take appropriate action
                    Log.d("Fragment_Booking", "No available check-in times.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no time is selected if necessary
                // For example, you might want to display a message or take some action
                Log.d("Fragment_Booking", "No time selected.");
            }
        });

        checkOutButton.setOnClickListener(v -> showDatePickerDialog((date, month, year) -> {
            String checkOutDate = (month + 1) + "/" + date + "/" + year;
            checkOutDateTextView.setText(checkOutDate);

            // Get the selected check-in time
            String selectedCheckInTime = availableCheckInTimes.get(checkInTimeSpinner.getSelectedItemPosition());

            // Set the checkout time to be the same as the selected check-in time
            String checkOutTime = selectedCheckInTime;
            checkTimeTextView.setText(checkOutDate + "; " + checkOutTime);
        }));

        nextStepButton.setOnClickListener(v -> {
            if (validateStep1()) {
                updateStepIndicators(2); // Change 2 to the next step number as needed
            } else {
                Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            }
        });

        guestOptionsButton.setOnClickListener(v -> showGuestOptionsDialog());
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

    private List<String> getAvailableCheckInTimes() {
        return Arrays.asList("12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM",
                "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
                "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM",
                "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM");
    }

    private void fetchAvailableCheckInTimes() {
        availableCheckInTimes = getAvailableCheckInTimes();
        // Once the available check-in times are fetched, set up the spinner adapter
        setupCheckInTimeSpinnerAdapter();
    }

    private void updateCheckOutTime(String selectedCheckInTime) {
        String checkOutDate = checkOutDateTextView.getText().toString(); // Get the checkout date
        checkTimeTextView.setText(checkOutDate + "; " + selectedCheckInTime); // Concatenate with selected check-in time
    }

    private void setupCheckInTimeSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, availableCheckInTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkInTimeSpinner.setAdapter(adapter);
    }

    private void showDatePickerDialog(DatePickerListener listener) {
        Calendar calendar = Calendar.getInstance();
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            if (selectedDate.before(Calendar.getInstance())) {
                Toast.makeText(requireContext(), "Please select a valid date.", Toast.LENGTH_SHORT).show();
            } else {
                // Set the check-in date text
                String checkInDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                checkInDateTextView.setText(checkInDate);

                // Extract year from check-in date
                String[] selectedDateParts = checkInDate.split("/");
                int selectedYear = Integer.parseInt(selectedDateParts[2]);

                // Pass the date components to the listener
                listener.onDateSet(dayOfMonth, month, year);
            }
        }, initialYear, initialMonth, initialDay);
        datePickerDialog.show();
    }

    private void setCheckOutDateTime(int year, int month, int date, int checkInHour, int checkInMinute) {
        // Set the checkout date to be the next day
        Calendar checkOutCalendar = Calendar.getInstance();
        checkOutCalendar.set(year, month, date, checkInHour, checkInMinute); // Set check-out time to check-in time
        checkOutCalendar.add(Calendar.DAY_OF_YEAR, 1); // Add one day
        String checkOutDate = (checkOutCalendar.get(Calendar.MONTH) + 1) + "/" + checkOutCalendar.get(Calendar.DAY_OF_MONTH) + "/" + checkOutCalendar.get(Calendar.YEAR);
        checkOutDateTextView.setText(checkOutDate);
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
            if (adultCount < 10 && (adultCount + childrenCount) < 10) { // Check if total guest count is less than 10
                adultCount++;
                adultCountTextView.setText(String.valueOf(adultCount));
            } else {
                Toast.makeText(getContext(), "Maximum limit reached for adults.", Toast.LENGTH_SHORT).show();
            }
        });

        decreaseAdultsButton.setOnClickListener(v -> {
            if (adultCount > 1) {
                adultCount--;
                adultCountTextView.setText(String.valueOf(adultCount));
            }
        });

        increaseChildrenButton.setOnClickListener(v -> {
            if (childrenCount < 10 && (adultCount + childrenCount) < 10) { // Check if total guest count is less than 10
                childrenCount++;
                childrenCountTextView.setText(String.valueOf(childrenCount));
            } else {
                Toast.makeText(getContext(), "Maximum limit reached for children.", Toast.LENGTH_SHORT).show();
            }
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

    private boolean validateStep1() {
        String checkInDate = checkInDateTextView.getText().toString();
        String checkOutDate = checkOutDateTextView.getText().toString();
        String guests = guestsTextView.getText().toString();

        if (checkInDate.isEmpty()) {
            Toast.makeText(getContext(), "Please select a check-in date.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkOutDate.isEmpty()) {
            Toast.makeText(getContext(), "Please select a check-out date.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (guests.isEmpty()) {
            Toast.makeText(getContext(), "Please select the number of guests.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Extract the number of adults and children from the guests string
        int totalGuests = adultCount + childrenCount;

        if (totalGuests > 10) {
            Toast.makeText(getContext(), "Total number of guests cannot exceed 10.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updateStepIndicators(int step) {
        TextView step1 = getView().findViewById(R.id.step1);
        TextView step2 = getView().findViewById(R.id.step2);
        TextView step3 = getView().findViewById(R.id.step3);
        TextView step4 = getView().findViewById(R.id.step4);
        TextView step5 = getView().findViewById(R.id.step5);

        step1.setVisibility(View.VISIBLE);
        step2.setVisibility(View.VISIBLE);
        step3.setVisibility(View.VISIBLE);
        step4.setVisibility(View.VISIBLE);
        step5.setVisibility(View.VISIBLE);

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
                break;
        }

        updateStepIndicatorBackground(step);
    }

    private void updateStepIndicatorBackground(int step) {
        TextView[] steps = {
                getView().findViewById(R.id.step1),
                getView().findViewById(R.id.step2),
                getView().findViewById(R.id.step3),
                getView().findViewById(R.id.step4),
                getView().findViewById(R.id.step5)
        };

        for (int i = 0; i < steps.length; i++) {
            if (i < step) {
                steps[i].setBackgroundResource(R.drawable.circle_background_selected);
            } else {
                steps[i].setBackgroundResource(R.drawable.circle_background_unselected);
            }
        }
    }

    private interface DatePickerListener {
        void onDateSet(int date, int month, int year);
    }
}