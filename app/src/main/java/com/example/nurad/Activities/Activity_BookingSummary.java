package com.example.nurad.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nurad.R;

public class Activity_BookingSummary extends AppCompatActivity {

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
        TextView totalValTextView = findViewById(R.id.totalVal);
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
        voucherValueTextView.setText("₱ - "+voucherValue);
        notesTextView.setText(notes);
        checkinduration.setText(checkInDate+" ("+checkInTime+")");
        checkoutduration.setText(checkOutDate+" ("+checkOutTime+")");

        // Display selected add-ons
        displaySelectedAddOns(selectedAddOns);

        // Calculate total price
        double subtotalValue = parsePrice(subtotal);
        double vatValue = parsePrice(vat);
        double voucherValueValue = parsePrice(voucherValue);

// Calculate the subtotal without the voucher value
        double semitotal = subtotalValue + vatValue;

// Deduct the voucher value from the subtotal, VAT, and add-ons total
        double totalValue = semitotal - voucherValueValue;

// Set total price
        totalValTextView.setText("₱ " + String.format("%.2f", totalValue));
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
            addOnsTextView.setText("");
            addOnsValTextView.setText("₱ 0.00");
        }
    }

    private String formatPrice(double price) {
        // Format the price with two decimal places and the currency symbol
        return String.format("%.2f", price);
    }
}