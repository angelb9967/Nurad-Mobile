package com.example.nurad.Fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.example.nurad.Activities.Activity_BookingSummary;
import com.example.nurad.Activities.CheckOutDateValidator;
import com.example.nurad.Activities.FutureDateValidator;
import com.example.nurad.Activities.MyCompositeDateValidator;
import com.example.nurad.Activities.UnavailableDateValidator;
import com.example.nurad.Adapters.Adapter_AddOn;
import com.example.nurad.Models.Model_AddOns;
import com.example.nurad.Models.Model_Booking;
import com.example.nurad.Models.Model_PriceRule;
import com.example.nurad.Models.RoomModel;
import com.example.nurad.Models.VoucherModel;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.datepicker.CompositeDateValidator;

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
    private double fetchedPrice, subtotal, vat, price;
    private DatabaseReference addOnsDBRef;
    private RecyclerView recyclerView;
    private Adapter_AddOn adapter;
    private Button nextStepButton;
    private Spinner prefixSpinner, regionSpinner, citySpinner;
    private EditText firstNameEditText, lastNameEditText, phoneEditText, mobilePhoneEditText, emailEditText, countryEditText,
            address1EditText, address2EditText, zipCodeEditText;

    private static final Map<String, List<String>> cities = new HashMap<>();
    private EditText expirationDateEditText;
    private ImageView expirationCalendarPickerImg;
    private EditText cardNumberEditText;
    private EditText cvvEditText;
    private EditText nameOnCardEditText;
    private EditText checkInDateEditText, checkOutDateEditText;
    private ImageView calendarPickerInImg, calendarPickerOutImg;
    private Spinner checkInTimeSpinner;
    private TextView checkOutTimeTextView, voucherPrice, selectedAddOnsPrice;
    private String status = "Booked";
    private TextView adultGuestPrice, childGuestPrice, bookStatus, bookSubTotal, bookVatVal;
    private double extraChildPrice, extraAdultPrice;
    private Calendar checkInDateCalendar, checkOutDateCalendar;
    private String checkInDate;
    private String checkOutDate;
    private Map<Long, Long> unavailableDateRanges = new HashMap<>();
    private EditText adultEditText;
    private ImageView adultPlusImg;
    private ImageView adultMinusImg;
    private EditText childEditText;
    private ImageView childPlusImg;
    private ImageView childMinusImg;
    private CheckBox applyVoucherCheckBox;
    private EditText voucherEditText;
    private boolean isVoucherValid = false;
    private boolean isVoucherChecked = false;
    private EditText notesEditText;

    //for region and city spinner
    static {
        cities.put("ARMM", Arrays.asList("Lamitan City", "Marawi City"));
        cities.put("CAR", Arrays.asList("Baguio City", "Tabuk City"));
        cities.put("NCR", Arrays.asList(
                "Caloocan City", "Las Piñas City", "Makati City", "Malabon City",
                "Mandaluyong City", "Manila City", "Marikina City", "Muntinlupa City",
                "Navotas City", "Parañaque City", "Pasay City", "Pasig City", "Quezon City",
                "San Juan City", "Taguig City", "Valenzuela City"
        ));
        cities.put("Region 1", Arrays.asList(
                "Batac City", "Laoag City", "Candon City", "Vigan City", "San Fernando City",
                "Alaminos City", "Dagupan City", "San Carlos City", "Urdaneta City"
        ));
        cities.put("Region 2", Arrays.asList(
                "Tuguegarao City", "Cauayan City", "Ilagan City", "Santiago City"
        ));
        cities.put("Region 3", Arrays.asList(
                "Balanga City", "Malolos City", "Meycauayan City", "San Jose del Monte City",
                "Cabanatuan City", "Gapan City", "Muñoz City", "Palayan City", "Angeles City",
                "Mabalacat City", "San Fernando City", "Tarlac City", "Olongapo City",
                "San Jose City"
        ));
        cities.put("Region 4A", Arrays.asList(
                "Batangas City", "Lipa City", "Tanauan City", "Bacoor City", "Cavite City",
                "Dasmariñas City", "Imus City", "Tagaytay City", "Trece Martires City",
                "Biñan City", "Cabuyao City", "San Pablo City", "Santa Rosa City",
                "Lucena City", "Tayabas City", "Antipolo City", "Calamba City"
        ));
        cities.put("Region 4B", Arrays.asList("Calapan City", "Puerto Princesa City"));
        cities.put("Region 5", Arrays.asList(
                "Legazpi City", "Ligao City", "Tabaco City", "Iriga City", "Naga City",
                "Masbate City", "Sorsogon City"
        ));
        cities.put("Region 6", Arrays.asList(
                "Roxas City", "Iloilo City", "Passi City", "Bacolod City", "Bago City",
                "Cadiz City", "Escalante City", "Himamaylan City", "Kabankalan City",
                "La Carlota City", "Sagay City", "San Carlos City", "Silay City", "Sipalay City",
                "Talisay City", "Victorias City"
        ));
        cities.put("Region 7", Arrays.asList(
                "Tagbilaran City", "Bogo City", "Carcar City", "Cebu City", "Danao City",
                "Lapu-Lapu City", "Mandaue City", "Naga City", "Talisay City", "Bais City",
                "Bayawan City", "Canlaon City", "Dumaguete City", "Guihulngan City",
                "Tanjay City", "Toledo City"
        ));
        cities.put("Region 8", Arrays.asList(
                "Borongan City", "Baybay City", "Ormoc City", "Tacloban City", "Calbayog City",
                "Catbalogan City", "Maasin City"
        ));
        cities.put("Region 9", Arrays.asList(
                "Isabela City", "Dapitan City", "Dipolog City", "Pagadian City", "Zamboanga City"
        ));
        cities.put("Region 10", Arrays.asList(
                "Malaybalay City", "Valencia City", "Iligan City", "Oroquieta City", "Ozamiz City",
                "Tangub City", "Cagayan de Oro City", "El Salvador City", "Gingoog City"
        ));
        cities.put("Region 11", Arrays.asList(
                "Panabo City", "Samal City", "Tagum City", "Davao City", "Digos City", "Mati City"
        ));
        cities.put("Region 12", Arrays.asList(
                "Kidapawan City", "Cotabato City", "General Santos City", "Koronadal City",
                "Tacurong City"
        ));
        cities.put("Region 13", Arrays.asList(
                "Butuan City", "Cabadbaran City", "Bayugan City", "Surigao City", "Bislig City",
                "Tandag City"
        ));
    }

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

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs() && (!isVoucherChecked || (isVoucherChecked && isVoucherValid))) {
                    // All conditions including voucher validation are met
                    continueBookingProcess();
                } else {
                    // Handle invalid inputs, such as displaying error messages or alerts
                    Toast.makeText(mContext, "Please ensure all fields are filled correctly and voucher code is valid.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Store the context when the Fragment is attached
        mContext = context;
    }

    private void initializeViews(View view) {
        //room
        roomTitleTextView = view.findViewById(R.id.roomTitle);
        roomDetailsTextView = view.findViewById(R.id.roomDetailsTextView);
        roomNameTextView = view.findViewById(R.id.roomNameTextView);
        roomTypeTextView = view.findViewById(R.id.roomTypeTextView);
        roomPriceTextView = view.findViewById(R.id.roomPriceTextView);
        availableCheckInTimes = new ArrayList<>();
        nextStepButton = view.findViewById(R.id.nextStepButton);

        //contact
        firstNameEditText = view.findViewById(R.id.FirstName_Etxt);
        lastNameEditText = view.findViewById(R.id.LastName_Etxt);
        phoneEditText = view.findViewById(R.id.Phone_Etxt);
        mobilePhoneEditText = view.findViewById(R.id.MobilePhone_Etxt);
        emailEditText = view.findViewById(R.id.Email_Etxt);

        countryEditText = view.findViewById(R.id.Country_Etxt);
        countryEditText.setText("Philippines");
        countryEditText.setEnabled(false);

        prefixSpinner = view.findViewById(R.id.Prefix_Spinner);
        List<String> prefixes = Arrays.asList("Mr", "Ms", "Mrs");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, prefixes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefixSpinner.setAdapter(adapter);

        List<String> regions = Arrays.asList(
                "ARMM",
                "CAR",
                "NCR",
                "Region 1",
                "Region 2",
                "Region 3",
                "Region 4A",
                "Region 4B",
                "Region 5",
                "Region 6",
                "Region 7",
                "Region 8",
                "Region 9",
                "Region 10",
                "Region 11",
                "Region 12",
                "Region 13"
        );

        // Initialize region spinner
        regionSpinner = view.findViewById(R.id.Region_Spinner);
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, new ArrayList<>(cities.keySet()));
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);

        // Initialize city spinner with empty list, will be updated based on selected region
        citySpinner = view.findViewById(R.id.City_Spinner);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, new ArrayList<>());
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // Set up region spinner listener to update city spinner
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = parent.getItemAtPosition(position).toString();
                List<String> citiesList = cities.get(selectedRegion);
                if (citiesList != null) {
                    // Clear the existing city list
                    cityAdapter.clear();
                    // Add the cities for the selected region
                    cityAdapter.addAll(citiesList);
                    // Notify the adapter that the data set has changed
                    cityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Clear the existing city list when no region is selected
                cityAdapter.clear();
                // Notify the adapter that the data set has changed
                cityAdapter.notifyDataSetChanged();
            }
        });

        //address
        address1EditText = view.findViewById(R.id.Address1_Etxt);
        address2EditText = view.findViewById(R.id.Address2_Etxt);
        regionSpinner = view.findViewById(R.id.Region_Spinner);
        citySpinner = view.findViewById(R.id.City_Spinner);
        zipCodeEditText = view.findViewById(R.id.ZipCode_Etxt);

        //payment
        expirationDateEditText = view.findViewById(R.id.ExpirationDate_Etxt);
        expirationCalendarPickerImg = view.findViewById(R.id.ExpirationCalendarPicker_Img);
        cardNumberEditText = view.findViewById(R.id.CardNumber_Etxt);
        cvvEditText = view.findViewById(R.id.CVV_Etxt);
        nameOnCardEditText = view.findViewById(R.id.NameOntheCard_Etxt);

        //date and time
        checkInDateEditText = view.findViewById(R.id.CheckInDate_Etxt);
        checkOutDateEditText = view.findViewById(R.id.CheckOutDate_Etxt);
        calendarPickerInImg = view.findViewById(R.id.CalendarPicker_In_Img);
        calendarPickerOutImg = view.findViewById(R.id.CalendarPicker_Out_Img);
        checkInTimeSpinner = view.findViewById(R.id.checkInTimeSpinner);
        checkOutTimeTextView = view.findViewById(R.id.checkTimeTextView);

        //guest inclusion
        childGuestPrice = view.findViewById(R.id.childGuestPrice);
        adultGuestPrice = view.findViewById(R.id.adultGuestPrice);
        adultEditText = view.findViewById(R.id.Adult_Etxt);
        adultPlusImg = view.findViewById(R.id.Adult_Plus_Img);
        adultMinusImg = view.findViewById(R.id.Adult_Minus_Img);
        childEditText = view.findViewById(R.id.Child_Etxt);
        childPlusImg = view.findViewById(R.id.Child_Plus_Img);
        childMinusImg = view.findViewById(R.id.Child_Minus_Img);

        // Set initial values
        adultEditText.setText("1");
        childEditText.setText("0");

        //voucher
        voucherPrice = view.findViewById(R.id.voucherPrice);
        applyVoucherCheckBox = view.findViewById(R.id.applyVoucherCheckBox);
        voucherEditText = view.findViewById(R.id.voucherEditText);

        bookStatus = view.findViewById(R.id.bookStatus);
        bookSubTotal = view.findViewById(R.id.bookSubTotal);
        bookVatVal = view.findViewById(R.id.bookVatVal);

        //Addon
        selectedAddOnsPrice = view.findViewById(R.id.selectedAddOnsPrice);

        //note
        notesEditText = view.findViewById(R.id.Notes_Txt);

    }

    //fetching add ons
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
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            // Other RecyclerView setup code
        } else {
            Log.e(TAG, "RecyclerView is null. Unable to set LayoutManager.");
        }
        adapter = new Adapter_AddOn(getContext(), addOnList, new Adapter_AddOn.OnAddOnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(double totalPrice) {
                selectedAddOnsPrice.setText(formatPrice(totalPrice));
                updateSubtotalAndVat();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void updateSubtotalAndVat() {
        double roomPrice = parsePrice(roomPriceTextView.getText().toString());
        double totalChildPrice = parsePrice(childGuestPrice.getText().toString());
        double totalAdultPrice = parsePrice(adultGuestPrice.getText().toString());
        double addOnsPrice = parsePrice(selectedAddOnsPrice.getText().toString());

        double subtotal = roomPrice + totalChildPrice + totalAdultPrice + addOnsPrice;
        double vatVal = subtotal * 0.12;

        bookSubTotal.setText(formatPrice(subtotal));
        bookVatVal.setText(formatPrice(vatVal));

        // Optionally update visibility
        bookSubTotal.setVisibility(View.VISIBLE);
        bookVatVal.setVisibility(View.VISIBLE);
    }

    private double parsePrice(String priceText) {
        // Remove everything except numbers and decimal point
        String price = priceText.replaceAll("[^\\d.]", "");
        return price.isEmpty() ? 0.0 : Double.parseDouble(price);
    }

    //fetching price of room
    private void fetchPriceRule(String priceRuleName) {
        priceRules_DBref.child(priceRuleName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Model_PriceRule priceRule = snapshot.getValue(Model_PriceRule.class);
                    if (priceRule != null) {
                        // Get the appropriate price based on the current day of the week
                        double dailyPrice = getPriceForCurrentDay(priceRule);
                        price = dailyPrice;
                        extraChildPrice = priceRule.getExtraChild_price();
                        extraAdultPrice = priceRule.getExtraAdult_price();
                        updateRoomPrice();
                        // Display the extra child and adult prices
                        updateGuestPrices();
                    } else {
                        // Handle null price rule scenario
                        setDefaultPrices();
                    }
                } else {
                    // Handle non-existent price rule scenario
                    setDefaultPrices();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error scenario
                setDefaultPrices();
            }
        });
    }

    private void setDefaultPrices() {
        price = 0;
        extraAdultPrice = 0;
        extraChildPrice = 0;
        updateRoomPrice();
        updateGuestPrices();
    }

    private void updateGuestPrices() {
        int childCount = Integer.parseInt(childEditText.getText().toString());
        int adultCount = Integer.parseInt(adultEditText.getText().toString());

        double totalChildPrice = childCount * extraChildPrice;
        double totalAdultPrice = adultCount * extraAdultPrice;

        childGuestPrice.setText(formatPrice(totalChildPrice));
        adultGuestPrice.setText(formatPrice(totalAdultPrice));

        updateSubtotalAndVat();
    }


    //other details of chosen room
    private void populateRoomDetails() {
        if (roomNameTextView != null && selectedRoom != null && selectedRoom.getRoomName() != null && !selectedRoom.getRoomName().isEmpty()) {
            roomNameTextView.setText("Room Number: " + selectedRoom.getRoomName() + "\n");
        }

        roomTitleTextView.setText(selectedRoom.getTitle());
        roomTypeTextView.setText("Room Type: " + selectedRoom.getRoomType() + "\n");
        roomDetailsTextView.setText("Description: " + selectedRoom.getDescription() + "\n");
//        childGuestPrice.setText("Total Price of Extra Child Guest/s: "+ selectedRoom.getPriceRule());

        if (selectedRoom.getPriceRule() != null) {
            // Fetch the price rule from the database
            fetchPriceRule(selectedRoom.getPriceRule());
        }
    }

    private void updateRoomPrice() {
        if (checkInDateCalendar != null && checkOutDateCalendar != null) {
            long diffInMillis = checkOutDateCalendar.getTimeInMillis() - checkInDateCalendar.getTimeInMillis();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
            double totalPrice = price * days;
            roomPriceTextView.setText(formatPrice(totalPrice));
        } else {
            roomPriceTextView.setText(formatPrice(price));
        }
        updateSubtotalAndVat();
    }


    private void setupEventListeners() {
        applyVoucherCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CompoundButtonCompat.setButtonTintList(applyVoucherCheckBox, ColorStateList.valueOf(Color.parseColor("#882065")));
                    voucherEditText.setVisibility(View.VISIBLE);
                    isVoucherChecked = true;
                } else {
                    CompoundButtonCompat.setButtonTintList(applyVoucherCheckBox, ColorStateList.valueOf(Color.parseColor("#000000")));
                    voucherEditText.setVisibility(View.GONE);
                    isVoucherChecked = false;
                    voucherPrice.setText("No Voucher Selected"); // Clear voucher value display
                }
            }
        });

        setupExpirationDatePicker();
        setupDatePickers();
        setupTimeSpinner();
        setupGuestControls();
    }

    //VALIDATIONS
    //getting price of the day
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

    private boolean isValidPhilippineMobileNumber(String mobileNumber) {
        // Philippine mobile number format: 09XX-XXX-YYYY
        return mobileNumber.matches("^(09|\\+639)\\d{2}\\d{3}\\d{4}$");
    }

    private boolean isValidPhilippineLandlineNumber(String landlineNumber) {
        // Remove hyphens from the landline number
        String formattedLandlineNumber = landlineNumber.replaceAll("-", "");

        // Check if the landline number is empty or matches the Philippine landline number formats
        return formattedLandlineNumber.isEmpty() || formattedLandlineNumber.matches("^(\\d{1,2}\\d{7}|\\d{8})$");
    }

    private boolean isValidPhilippinePostalCode(String zipCode) {
        // Check if the length of the zip code is exactly 4 characters
        if (zipCode.length() != 4) {
            return false;
        }

        // Check if the zip code consists of four digits
        for (char c : zipCode.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private void setupExpirationDatePicker() {
        expirationCalendarPickerImg.setOnClickListener(v -> showDatePicker_and_setMonthYear(expirationDateEditText));
    }

    private void showDatePicker_and_setMonthYear(EditText field) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                (view, year, month, dayOfMonth) -> {
                    month = month + 1;
                    String date = month + "/" + (year % 100);
                    field.setText(date);
                }, currentYear, currentMonth, 1);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void setupDatePickers() {
        calendarPickerInImg.setOnClickListener(v -> showDatePicker(checkInDateEditText, true));
        calendarPickerOutImg.setOnClickListener(v -> {
            if (checkInDateCalendar != null) {
                showDatePicker(checkOutDateEditText, false);
            } else {
                Toast.makeText(mContext, "Please select a check-in date first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker(EditText field, boolean isCheckIn) {
        Calendar calendar = Calendar.getInstance();
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(calendar.getTimeInMillis());
        builder.setTitleText(isCheckIn ? "Select Check-In Date" : "Select Check-Out Date");

        // Define constraints
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Create a list of validators
        List<CalendarConstraints.DateValidator> validators = new ArrayList<>();
        validators.add(new FutureDateValidator());  // Allow only future dates
        validators.add(new UnavailableDateValidator(unavailableDateRanges));  // Add the custom UnavailableDateValidator

        if (!isCheckIn && checkInDateCalendar != null) {
            validators.add(new CheckOutDateValidator(checkInDateCalendar.getTimeInMillis()));  // Ensure check-out date is after check-in date
        }

        MyCompositeDateValidator compositeValidator = new MyCompositeDateValidator(validators);
        constraintsBuilder.setValidator(compositeValidator);

        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            calendar.setTimeInMillis(selection);
            String selectedDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
            field.setText(selectedDate);

            if (isCheckIn) {
                checkInDateCalendar = calendar;
                checkInDate = selectedDate;
                checkOutDateEditText.setText(""); // Clear check-out date if check-in date changes
                checkOutDate = null;
            } else {
                checkOutDateCalendar = calendar;
                checkOutDate = selectedDate;
            }

            // Only check room availability if both dates are set
            if (checkInDate != null && checkOutDate != null) {
                checkRoomAvailability();
            }

            updateRoomPrice();
        });

        // Fetch unavailable dates before showing the date picker
        fetchUnavailableDates(() -> datePicker.show(getParentFragmentManager(), "DATE_PICKER"), isCheckIn);
    }

    private void fetchUnavailableDates(Runnable onSuccess, boolean isCheckIn) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingsRef = database.getReference("Booking");

        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                unavailableDateRanges.clear();
                String selectedRoomName = selectedRoom.getRoomName();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    Model_Booking booking = bookingSnapshot.getValue(Model_Booking.class);
                    if (booking != null && selectedRoomName.equals(booking.getRoom())) {
                        try {
                            Date existingCheckIn = sdf.parse(booking.getCheckInDate());
                            Date existingCheckOut = sdf.parse(booking.getCheckOutDate());
                            unavailableDateRanges.put(existingCheckIn.getTime(), existingCheckOut.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                onSuccess.run(); // Run the success callback
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void checkRoomAvailability() {
        if (checkInDate == null || checkOutDate == null) {
            return; // Avoid null pointer exceptions
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date newCheckIn = sdf.parse(checkInDate);
            Date newCheckOut = sdf.parse(checkOutDate);

            for (Map.Entry<Long, Long> entry : unavailableDateRanges.entrySet()) {
                Date existingCheckIn = new Date(entry.getKey());
                Date existingCheckOut = new Date(entry.getValue());

                if ((newCheckIn.before(existingCheckOut) && newCheckOut.after(existingCheckIn))) {
                    Toast.makeText(mContext, "Room is not available for the selected dates", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(mContext, "Room is available", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setupTimeSpinner() {
        List<String> timeSlots = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            int hour = i % 12;
            if (hour == 0) hour = 12;
            String amPm = (i < 12) ? "AM" : "PM";
            String timeSlot = String.format("%02d:00 %s", hour, amPm);
            timeSlots.add(timeSlot);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkInTimeSpinner.setAdapter(adapter);

        checkInTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTime = parent.getItemAtPosition(position).toString();
                checkOutTimeTextView.setText(selectedTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                checkOutTimeTextView.setText("Not set");
            }
        });
    }

    private void setupGuestControls() {
        adultPlusImg.setOnClickListener(v -> changeGuestCount(adultEditText, 1, 1, 20));
        adultMinusImg.setOnClickListener(v -> changeGuestCount(adultEditText, -1, 1, 20));

        childPlusImg.setOnClickListener(v -> changeGuestCount(childEditText, 1, 0, 20));
        childMinusImg.setOnClickListener(v -> changeGuestCount(childEditText, -1, 0, 20));
    }

    private void changeGuestCount(EditText editText, int change, int min, int max) {
        try {
            int currentCount = Integer.parseInt(editText.getText().toString());
            int newCount = currentCount + change;

            if (newCount >= min && newCount <= max) {
                editText.setText(String.valueOf(newCount));
                updateGuestPrices(); // Update guest prices after changing the count
            } else if (newCount < min) {
                Toast.makeText(getContext(), "Value cannot be lower than " + min, Toast.LENGTH_SHORT).show();
            } else if (newCount > max) {
                Toast.makeText(getContext(), "Value cannot be higher than " + max, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }


    private void validateVoucherCode(final String code, final OnVoucherValidationListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference vouchersRef = database.getReference("Vouchers");

        // Get the current user UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            listener.onValidationError(); // Handle case where user is not authenticated
            return;
        }

        String userId = currentUser.getUid();

        // Check if the voucher code exists in Vouchers node
        vouchersRef.orderByChild("code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean voucherFound = false;
                double voucherValue = 0;

                for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                    VoucherModel voucher = voucherSnapshot.getValue(VoucherModel.class);
                    if (voucher != null) {
                        voucherFound = true;
                        voucherValue = voucher.getValue();
                        break;
                    }
                }

                if (voucherFound) {
                    // Check if the voucher code is already used by the user in UserVouchers node
                    DatabaseReference userVouchersRef = database.getReference("UserVouchers").child(userId).child(code);
                    double finalVoucherValue = voucherValue;
                    userVouchersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Voucher code is already used by the user
                                listener.onValidationFailure();
                            } else {
                                // Voucher code is valid and not used by the user
                                listener.onValidationSuccess(finalVoucherValue);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            listener.onValidationError();
                        }
                    });
                } else {
                    listener.onValidationFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onValidationError();
            }
        });
    }

    private boolean validateInputs() {

        // we need to make sure subtotal, vat, and status is also included in this
        String AddOnsPrice = selectedAddOnsPrice.getText().toString().trim();
        String voucherValue = voucherPrice.getText().toString().trim();
        Map<String, Model_AddOns> selectedAddOns = adapter.getSelectedAddOns();
        String roomPrice = roomPriceTextView.getText().toString().trim();
        String extraAdultPrice = adultGuestPrice.getText().toString().trim();
        String extraChildPrice = childGuestPrice.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();
        String prefix = prefixSpinner.getSelectedItem().toString();
        String checkInDate = checkInDateEditText.getText().toString().trim();
        String checkOutDate = checkOutDateEditText.getText().toString().trim();
        String checkInTime = checkInTimeSpinner.getSelectedItem().toString();
        String checkOutTime = checkInTimeSpinner.getSelectedItem().toString();
        String room = roomNameTextView.getText().toString();
        String roomTitle = roomTitleTextView.getText().toString();
        String adultCount = adultEditText.getText().toString();
        String childCount = childEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String mobilePhone = mobilePhoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address1 = address1EditText.getText().toString().trim();
        String address2 = address2EditText.getText().toString().trim();
        String selectedRegion = regionSpinner.getSelectedItem().toString();
        String selectedCity = citySpinner.getSelectedItem().toString();
        String zipCode = zipCodeEditText.getText().toString().trim();
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expirationDate = expirationDateEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();
        String nameOnCard = nameOnCardEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        if (prefixSpinner.getSelectedItem() == null || prefixSpinner.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please select a prefix", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (firstName.isEmpty()) {
            firstNameEditText.setError("First name is required");
            firstNameEditText.requestFocus();
            return false;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last name is required");
            lastNameEditText.requestFocus();
            return false;
        }

        // Set landline number to "N/A" if empty
        if (phone.isEmpty()) {
            phone = "N/A";
        } else {
            // Validate Philippine landline phone number
            if (!isValidPhilippineLandlineNumber(phone)) {
                phoneEditText.setError("Invalid Philippine landline phone number");
                phoneEditText.requestFocus();
                return false;
            }
        }

        if (mobilePhone.isEmpty() && phone.equals("N/A")) {
            mobilePhoneEditText.setError("At least one phone number is required");
            mobilePhoneEditText.requestFocus();
            return false;
        } else {
            // Validate Philippine mobile phone number
            if (!mobilePhone.isEmpty() && !isValidPhilippineMobileNumber(mobilePhone)) {
                mobilePhoneEditText.setError("Invalid Philippine mobile phone number");
                mobilePhoneEditText.requestFocus();
                return false;
            }
        }

        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email address");
            emailEditText.requestFocus();
            return false;
        }

        if (address1.isEmpty()) {
            address1EditText.setError("Address 1 is required");
            address1EditText.requestFocus();
            return false;
        }

        if (address2.isEmpty()) {
            address2EditText.setText("N/A");
        }

        if (selectedRegion.isEmpty()) {
            Toast.makeText(getContext(), "Please select a region", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedCity.isEmpty()) {
            Toast.makeText(getContext(), "Please select a city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (zipCode.isEmpty() || !isValidPhilippinePostalCode(zipCode)) {
            zipCodeEditText.setError("Invalid Zip/Postal Code");
            zipCodeEditText.requestFocus();
            return false;
        }

        if (cardNumber.isEmpty() || cardNumber.length() != 16) {
            cardNumberEditText.setError("Card number must be 16 digits");
            cardNumberEditText.requestFocus();
            return false;
        }

        if (expirationDate.isEmpty()) {
            expirationDateEditText.setError("Expiration date is required");
            expirationDateEditText.requestFocus();
            return false;
        }

        if (cvv.isEmpty() || cvv.length() != 3) {
            cvvEditText.setError("CVV must be 3 digits");
            cvvEditText.requestFocus();
            return false;
        }

        if (nameOnCard.isEmpty()) {
            nameOnCardEditText.setError("Name on the card is required");
            nameOnCardEditText.requestFocus();
            return false;
        }

        if (notes.isEmpty()) {
            notes = "No Note";
        } else if (notes.split("\\s+").length > 150) {
            notesEditText.setError("Notes cannot exceed 250 words");
            notesEditText.requestFocus();
            return false;
        }

        if (isVoucherChecked) {
            String voucherCode = voucherEditText.getText().toString().trim();
            if (voucherCode.isEmpty()) {
                voucherEditText.setError("Voucher code is required");
                voucherEditText.requestFocus();
                return false;
            } else {
                validateVoucherCode(voucherCode, new OnVoucherValidationListener() {
                    @Override
                    public void onValidationSuccess(double voucherValue) {
                        voucherPrice.setText(formatPrice(voucherValue));
                        isVoucherValid = true;
                        continueBookingProcess();
                    }

                    @Override
                    public void onValidationFailure() {
                        voucherEditText.setError("Invalid voucher code. Please try again.");
                        voucherEditText.requestFocus();
                        isVoucherValid = false;
                    }

                    @Override
                    public void onValidationError() {
                        Toast.makeText(getContext(), "Error checking voucher code. Please try again.", Toast.LENGTH_SHORT).show();
                        isVoucherValid = false;
                    }
                });

                return false;
            }
        } else {
            double defaultVoucherValue = 0.0;
            voucherPrice.setText(formatPrice(defaultVoucherValue));
            voucherEditText.setText("N/A");
            continueBookingProcess();
        }

        return true;
    }

    private void continueBookingProcess() {
        Intent intent = new Intent(mContext, Activity_BookingSummary.class);

        intent.putExtra("addOnsPrice", selectedAddOnsPrice.getText().toString().trim());
        intent.putExtra("voucherValue", voucherPrice.getText().toString().trim());
        intent.putExtra("roomPrice", roomPriceTextView.getText().toString().trim());
        intent.putExtra("extraAdultPrice", adultGuestPrice.getText().toString().trim());
        intent.putExtra("extraChildPrice", childGuestPrice.getText().toString().trim());
        intent.putExtra("subtotal", bookSubTotal.getText().toString().trim());
        intent.putExtra("vat", bookVatVal.getText().toString().trim());
        intent.putExtra("status", status);
        intent.putExtra("country", countryEditText.getText().toString().trim());
        intent.putExtra("prefix", prefixSpinner.getSelectedItem().toString());
        intent.putExtra("checkInDate", checkInDateEditText.getText().toString().trim());
        intent.putExtra("checkOutDate", checkOutDateEditText.getText().toString().trim());
        intent.putExtra("checkInTime", checkInTimeSpinner.getSelectedItem().toString());
        intent.putExtra("checkOutTime", checkInTimeSpinner.getSelectedItem().toString());
        intent.putExtra("room", selectedRoom.getRoomName());
        intent.putExtra("roomTitle", selectedRoom.getTitle());
        intent.putExtra("adultCount", adultEditText.getText().toString());
        intent.putExtra("childCount", childEditText.getText().toString());
        intent.putExtra("firstName", firstNameEditText.getText().toString().trim());
        intent.putExtra("lastName", lastNameEditText.getText().toString().trim());
        intent.putExtra("phone", phoneEditText.getText().toString().trim());
        intent.putExtra("mobilePhone", mobilePhoneEditText.getText().toString().trim());
        intent.putExtra("email", emailEditText.getText().toString().trim());
        intent.putExtra("address1", address1EditText.getText().toString().trim());
        intent.putExtra("address2", address2EditText.getText().toString().trim());
        intent.putExtra("selectedRegion", regionSpinner.getSelectedItem().toString());
        intent.putExtra("selectedCity", citySpinner.getSelectedItem().toString());
        intent.putExtra("zipCode", zipCodeEditText.getText().toString().trim());
        intent.putExtra("cardNumber", cardNumberEditText.getText().toString().trim());
        intent.putExtra("expirationDate", expirationDateEditText.getText().toString().trim());
        intent.putExtra("cvv", cvvEditText.getText().toString().trim());
        intent.putExtra("nameOnCard", nameOnCardEditText.getText().toString().trim());
        intent.putExtra("notes", notesEditText.getText().toString().trim());

        StringBuilder selectedAddOnsBuilder = new StringBuilder();
        Map<String, Model_AddOns> selectedAddOnsMap = adapter.getSelectedAddOns();
        for (Model_AddOns addOn : selectedAddOnsMap.values()) {
            selectedAddOnsBuilder.append(addOn.getTitle()).append(": ₱").append(addOn.getPrice()).append("\n");
        }
        intent.putExtra("selectedAddOns", selectedAddOnsBuilder.toString());
        String voucherCode = voucherEditText.getText().toString().trim();
        intent.putExtra("voucherCode", voucherCode);
        startActivity(intent);
    }

    private interface DatePickerListener {
        void onDateSet(int date, int month, int year);
    }

    public interface DateValidator {
        boolean isValid(long date);
    }

    public interface OnVoucherValidationListener {
        void onValidationSuccess(double voucherValue);
        void onValidationFailure();
        void onValidationError();
    }

}