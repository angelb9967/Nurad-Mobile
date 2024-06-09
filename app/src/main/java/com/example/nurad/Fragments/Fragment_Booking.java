package com.example.nurad.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private Spinner prefixSpinner, regionSpinner, citySpinner;
    private EditText firstNameEditText, lastNameEditText, phoneEditText, mobilePhoneEditText, emailEditText, countryEditText,
            address1EditText, address2EditText, zipCodeEditText;

    private static final Map<String, List<String>> cities = new HashMap<>();

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
                if (validateInputs()) {
                    Toast.makeText(mContext, "All conditions are met", Toast.LENGTH_SHORT).show();
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
        roomTitleTextView = view.findViewById(R.id.roomTitle);
        roomDetailsTextView = view.findViewById(R.id.roomDetailsTextView);
        roomNameTextView = view.findViewById(R.id.roomNameTextView);
        roomTypeTextView = view.findViewById(R.id.roomTypeTextView);
        roomPriceTextView = view.findViewById(R.id.roomPriceTextView);
        availableCheckInTimes = new ArrayList<>();
        nextStepButton = view.findViewById(R.id.nextStepButton);

        firstNameEditText = view.findViewById(R.id.FirstName_Etxt);
        lastNameEditText = view.findViewById(R.id.LastName_Etxt);
        phoneEditText = view.findViewById(R.id.Phone_Etxt);
        mobilePhoneEditText = view.findViewById(R.id.MobilePhone_Etxt);
        emailEditText = view.findViewById(R.id.Email_Etxt);

        countryEditText = view.findViewById(R.id.Country_Etxt);
        countryEditText.setText("Philippines");
        countryEditText.setEnabled(false);

        //contact
        // Initialize UI components
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

        address1EditText = view.findViewById(R.id.Address1_Etxt);
        address2EditText = view.findViewById(R.id.Address2_Etxt);
        regionSpinner = view.findViewById(R.id.Region_Spinner);
        citySpinner = view.findViewById(R.id.City_Spinner);
        zipCodeEditText = view.findViewById(R.id.ZipCode_Etxt);

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

    private boolean validateInputs() {
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
            // Handle empty selection for region
            // You can show an error message or handle it according to your requirements
            return false;
        }

        if (selectedCity.isEmpty()) {
            // Handle empty selection for city
            // You can show an error message or handle it according to your requirements
            return false;
        }

        if (zipCode.isEmpty() || !isValidPhilippinePostalCode(zipCode)) {
            zipCodeEditText.setError("Invalid Zip/Postal Code");
            zipCodeEditText.requestFocus();
            return false;
        }

        return true;
    }

    private interface DatePickerListener {
        void onDateSet(int date, int month, int year);
    }
}