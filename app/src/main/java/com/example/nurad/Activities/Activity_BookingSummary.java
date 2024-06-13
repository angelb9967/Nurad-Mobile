package com.example.nurad.Activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.Models.Model_AddressInfo;
import com.example.nurad.Models.Model_Booking;
import com.example.nurad.Models.Model_ContactInfo;
import com.example.nurad.Models.Model_PaymentInfo;
import com.example.nurad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Activity_BookingSummary extends AppCompatActivity {


    private DatabaseReference booking_DBref, contact_DBref, address_DBref, payment_DBref;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button nextStepButton = findViewById(R.id.nextStepButton);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to save the data to the database
                saveDataToDatabase();
            }
        });


        // Retrieve data from Intent
        Intent intent = getIntent();
        String addOnsPrice = intent.getStringExtra("addOnsPrice");
        String voucherValue = intent.getStringExtra("voucherValue");
        String roomPrice = intent.getStringExtra("roomPrice");
        String extraAdultPrice = intent.getStringExtra("extraAdultPrice");
        String extraChildPrice = intent.getStringExtra("extraChildPrice");
        String subtotal = intent.getStringExtra("subtotal");
        String vat = intent.getStringExtra("vat");
        String status = intent.getStringExtra("status");
        String country = intent.getStringExtra("country");
        String prefix = intent.getStringExtra("prefix");
        String checkInDate = intent.getStringExtra("checkInDate");
        String checkOutDate = intent.getStringExtra("checkOutDate");
        String checkInTime = intent.getStringExtra("checkInTime");
        String checkOutTime = intent.getStringExtra("checkOutTime");
        String room = intent.getStringExtra("room");
        String roomTitle = intent.getStringExtra("roomTitle");
        String adultCount = intent.getStringExtra("adultCount");
        String childCount = intent.getStringExtra("childCount");
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String phone = intent.getStringExtra("phone");
        String mobilePhone = intent.getStringExtra("mobilePhone");
        String email = intent.getStringExtra("email");
        String address1 = intent.getStringExtra("address1");
        String address2 = intent.getStringExtra("address2");
        String selectedRegion = intent.getStringExtra("selectedRegion");
        String selectedCity = intent.getStringExtra("selectedCity");
        String zipCode = intent.getStringExtra("zipCode");
        String cardNumber = intent.getStringExtra("cardNumber");
        String expirationDate = intent.getStringExtra("expirationDate");
        String cvv = intent.getStringExtra("cvv");
        String nameOnCard = intent.getStringExtra("nameOnCard");
        String notes = intent.getStringExtra("notes");
        String selectedAddOns = intent.getStringExtra("selectedAddOns");
        String voucherCode = intent.getStringExtra("voucherCode");

        booking_DBref = FirebaseDatabase.getInstance().getReference("Booking");
        contact_DBref = FirebaseDatabase.getInstance().getReference("Contact Information");
        address_DBref = FirebaseDatabase.getInstance().getReference("Address Information");
        payment_DBref = FirebaseDatabase.getInstance().getReference("Payment Information");

        // Calculate total price
        double subtotalValue = parsePrice(subtotal);
        double vatValue = parsePrice(vat);
        double voucherValueValue = parsePrice(voucherValue);

        double semitotal = subtotalValue + vatValue;

        // Deduct the voucher value from the subtotal, VAT, and add-ons total
        double totalValue = semitotal - voucherValueValue;

        TextView totalValTextView = findViewById(R.id.totalVal);
        totalValTextView.setText(String.format("%.2f", totalValue));

        TextView roomTitleTextView = findViewById(R.id.roomNameNumber);
        TextView roomPriceTextView = findViewById(R.id.roomNameNumberVals);
        TextView adultCountTextView = findViewById(R.id.quantityAdult);
        TextView childCountTextView = findViewById(R.id.quantityChild);
        TextView extraAdultPriceTextView = findViewById(R.id.quantityAdultVal);
        TextView extraChildPriceTextView = findViewById(R.id.quantityChildVal);
        TextView subtotalTextView = findViewById(R.id.subtotalVal);
        TextView vatTextView = findViewById(R.id.taxVATVal);
        TextView voucherCodeTextView = findViewById(R.id.discountVoucher);
        TextView voucherValueTextView = findViewById(R.id.discountVoucherVal);
        TextView notesTextView = findViewById(R.id.notess);
        TextView dateTextView = findViewById(R.id.dateval);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView totalTextView = findViewById(R.id.totalValText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView checkoutduration =  findViewById(R.id.staydurationoutval);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView checkinduration = findViewById(R.id.staydurationval);

        // Set data to TextViews
        roomTitleTextView.setText(roomTitle + " ("+ room +")");
        roomPriceTextView.setText("₱ "+roomPrice);
        adultCountTextView.setText(adultCount + " x Extra Adult");
        childCountTextView.setText(childCount + " x Extra Child");
        extraAdultPriceTextView.setText("₱ "+extraAdultPrice);
        extraChildPriceTextView.setText("₱ "+extraChildPrice);
        subtotalTextView.setText("₱ "+subtotal);
        vatTextView.setText("₱ "+vat);
        voucherCodeTextView.setText("Discount ("+ voucherCode +")");
        voucherValueTextView.setText("₱ "+voucherValue);
        notesTextView.setText(notes);
        checkinduration.setText(checkInDate+" ("+checkInTime+")");
        checkoutduration.setText(checkOutDate+" ("+checkOutTime+")");
        totalTextView.setText("₱ "+totalValue);

        // Display selected add-ons
        displaySelectedAddOns(selectedAddOns);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(calendar.getTime());


        dateTextView.setText(currentDate);


        String lastFourDigits = "";
        if (cardNumber != null && cardNumber.length() >= 4) {
            lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        }

// Display the last four digits in the TextView
        TextView methodPaymentTextView = findViewById(R.id.paymentmethod);
        methodPaymentTextView.setText("Card ending in ****" + lastFourDigits);


    }

    private void saveDataToDatabase() {
        // Retrieve data from Intent
        Intent intent = getIntent();
        String addOnsPrice = intent.getStringExtra("addOnsPrice");
        String voucherValue = intent.getStringExtra("voucherValue");
        String roomPrice = intent.getStringExtra("roomPrice");
        String extraAdultPrice = intent.getStringExtra("extraAdultPrice");
        String extraChildPrice = intent.getStringExtra("extraChildPrice");
        String subtotal = intent.getStringExtra("subtotal");
        String vat = intent.getStringExtra("vat");
        String status = intent.getStringExtra("status");
        String country = intent.getStringExtra("country");
        String prefix = intent.getStringExtra("prefix");
        String checkInDate = intent.getStringExtra("checkInDate");
        String checkOutDate = intent.getStringExtra("checkOutDate");
        String checkInTime = intent.getStringExtra("checkInTime");
        String checkOutTime = intent.getStringExtra("checkOutTime");
        String room = intent.getStringExtra("room");
        String roomTitle = intent.getStringExtra("roomTitle");
        String adultCount = intent.getStringExtra("adultCount");
        String childCount = intent.getStringExtra("childCount");
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String phone = intent.getStringExtra("phone");
        String mobilePhone = intent.getStringExtra("mobilePhone");
        String email = intent.getStringExtra("email");
        String address1 = intent.getStringExtra("address1");
        String address2 = intent.getStringExtra("address2");
        String selectedRegion = intent.getStringExtra("selectedRegion");
        String selectedCity = intent.getStringExtra("selectedCity");
        String zipCode = intent.getStringExtra("zipCode");
        String cardNumber = intent.getStringExtra("cardNumber");
        String expirationDate = intent.getStringExtra("expirationDate");
        String cvv = intent.getStringExtra("cvv");
        String nameOnCard = intent.getStringExtra("nameOnCard");
        String notes = intent.getStringExtra("notes");
        String selectedAddOns = intent.getStringExtra("selectedAddOns");
        String voucherCode = intent.getStringExtra("voucherCode");

        double subtotalValue = parsePrice(subtotal);
        double vatValue = parsePrice(vat);
        double voucherValueValue = parsePrice(voucherValue);

        double semitotal = subtotalValue + vatValue;
        double totalValue = semitotal - voucherValueValue;

        // Extract other data...

        // Get current user ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        booking_DBref = FirebaseDatabase.getInstance().getReference("Booking");
        contact_DBref = FirebaseDatabase.getInstance().getReference("Contact Information");
        address_DBref = FirebaseDatabase.getInstance().getReference("Address Information");
        payment_DBref = FirebaseDatabase.getInstance().getReference("Payment Information");

        String bookingId = booking_DBref.push().getKey();
        String contactId = contact_DBref.push().getKey();
        String addressId = address_DBref.push().getKey();
        String paymentId = payment_DBref.push().getKey();

        if (bookingId == null || contactId == null || addressId == null || paymentId == null) {
            Toast.makeText(this, "Failed to generate IDs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create instances of your models
        Model_Booking booking = new Model_Booking(
                bookingId, contactId, addressId, paymentId, userId, checkInDate, checkOutDate, checkInTime, checkOutTime,
                voucherCode, subtotalValue, Integer.parseInt(adultCount), Integer.parseInt(childCount), notes, room,
                parseSelectedAddOns(selectedAddOns), new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                voucherValueValue, status, parsePrice(roomPrice), parsePrice(extraAdultPrice), parsePrice(extraChildPrice),
                parsePrice(addOnsPrice), totalValue, vatValue, roomTitle);
        Model_AddressInfo addressInfo = new Model_AddressInfo(addressId, userId, country, address1, address2, selectedCity, selectedRegion, zipCode);
        Model_ContactInfo contactInfo = new Model_ContactInfo(contactId, userId, prefix, firstName, lastName, phone, mobilePhone, email);
        Model_PaymentInfo paymentInfo = new Model_PaymentInfo(paymentId, userId, cardNumber, expirationDate, cvv, nameOnCard);

        // Save the data to Firebase
        saveToBookingFirebase(bookingId, booking);
        saveToAddressInfoFirebase(addressId, addressInfo);
        saveToContactInfoFirebase(contactId, contactInfo);
        saveToPaymentInfoFirebase(paymentId, paymentInfo);

        // Show a success message
        Toast.makeText(this, "Room Officially Booked", Toast.LENGTH_SHORT).show();
    }


    private void saveToPaymentInfoFirebase(String paymentId, Model_PaymentInfo paymentInfo) {
        payment_DBref.child(paymentId).setValue(paymentInfo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Payment info saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save payment info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void saveToContactInfoFirebase(String contactId, Model_ContactInfo contactInfo) {
        contact_DBref.child(contactId).setValue(contactInfo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Contact info saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save contact info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void saveToAddressInfoFirebase(String addressId, Model_AddressInfo addressInfo) {
        address_DBref.child(addressId).setValue(addressInfo)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Address info saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save address info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToBookingFirebase(String bookingId, Model_Booking booking) {
        booking_DBref.child(bookingId).setValue(booking)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Booking saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private Map<String, String> parseSelectedAddOns(String selectedAddOns) {
        Map<String, String> addOnsMap = new HashMap<>();
        if (selectedAddOns != null && !selectedAddOns.isEmpty()) {
            String[] addOnsArray = selectedAddOns.split("\n");
            for (String addOn : addOnsArray) {
                String[] addOnDetails = addOn.split(": ₱");
                if (addOnDetails.length == 2) {
                    String addOnTitle = addOnDetails[0];
                    String addOnPrice = addOnDetails[1];
                    addOnsMap.put(addOnTitle, addOnPrice);
                }
            }
        }
        return addOnsMap;
    }


    private double parsePrice(String price) {
        if (price != null && !price.isEmpty()) {
            try {
                return Double.parseDouble(price.replace("₱", "").replace(",", "").trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }


    private void displaySelectedAddOns(String selectedAddOns) {
        LinearLayout addOnsContainer = findViewById(R.id.addOnsContainer);
        TextView addOnsTextView = findViewById(R.id.addOns); // TextView to display add-on titles
        TextView addOnsValTextView = findViewById(R.id.addOnsVal); // TextView to display total add-on price


        if (selectedAddOns != null && !selectedAddOns.isEmpty()) {
            StringBuilder addOnsBuilder = new StringBuilder(); // To build add-on titles string
            double totalAddOnPrice = 0.0; // To calculate total add-on price


            // Split the add-ons string into individual add-ons
            String[] addOnsArray = selectedAddOns.split("\n");


            for (String addOn : addOnsArray) {
                // Split each add-on into title and price
                String[] addOnDetails = addOn.split(": ₱");
                if (addOnDetails.length == 2) {
                    String addOnTitle = addOnDetails[0];
                    String addOnPrice = addOnDetails[1];


                    // Append add-on title to the StringBuilder
                    addOnsBuilder.append(addOnTitle).append(", ");


                    // Add add-on price to the total price
                    totalAddOnPrice += parsePrice(addOnPrice);
                }
            }


            // Remove trailing comma and space from add-on titles
            String addOnsTitles = addOnsBuilder.toString().trim();
            if (addOnsTitles.endsWith(",")) {
                addOnsTitles = addOnsTitles.substring(0, addOnsTitles.length() - 2);
            }


            // Set add-on titles and total price to the TextViews
            addOnsTextView.setText(addOnsTitles);
            addOnsValTextView.setText("₱ " + formatPrice(totalAddOnPrice));
        } else {
            // If no add-ons selected, set empty text to TextViews
            addOnsTextView.setText("No Selected Add-On");
            addOnsValTextView.setText("₱ 0.00");
        }
    }


    private String formatPrice(double price) {
        // Format the price with two decimal places and the currency symbol
        return String.format("%.2f", price);
    }
}