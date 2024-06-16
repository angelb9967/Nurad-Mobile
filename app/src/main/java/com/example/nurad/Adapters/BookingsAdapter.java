package com.example.nurad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Activities.Activity_BookingsInvoice;
import com.example.nurad.Models.Model_Booking;
import com.example.nurad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {
    private List<Model_Booking> bookingList;
    private Context context;

    public BookingsAdapter(List<Model_Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_invoice, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Model_Booking booking = bookingList.get(position);

        holder.bookingIdTextView.setText(booking.getBookingId());
        holder.roomTextView.setText(booking.getRoomTitle() + " (" + booking.getRoom() + ")");
        holder.checkInText.setText(booking.getCheckInDate() + " (" + booking.getCheckInTime() + ")");
        holder.checkOutText.setText(booking.getCheckOutDate() + " (" + booking.getCheckOutTime() + ")");
        holder.totalTextView.setText("₱ " + formatPrice(booking.getTotalValue()));
        holder.bookStat.setText(booking.getStatus());
        holder.bAndP_date.setText(booking.getBookingDate());
        holder.roomPrice.setText("₱ " + formatPrice(booking.getRoomPrice()));
        holder.adultQty.setText(booking.getAdultCount() + " x Adult");
        holder.childQty.setText(booking.getChildCount() + " x Adult");
        holder.adultPrice.setText("₱ " + formatPrice(booking.getExtraAdultPrice()));
        holder.childPrice.setText("₱ " + formatPrice(booking.getExtraChildPrice()));
        holder.noteMess.setText(booking.getNotes());
        holder.subPrice.setText("₱ " + formatPrice(booking.getSubtotalValue()));
        holder.taxVat.setText("₱ " + formatPrice(booking.getVatValue()));
        holder.discCode.setText("Discount" + " (" + booking.getVoucherCode() + ")");
        holder.discVal.setText("₱ " + formatPrice(booking.getVoucherValueValue()));

        // Fetch and display the last 4 digits of the card number
        fetchCardNumber(booking.getPaymentId(), holder.paymentMethod);

        // Fetch and display the contact information
        fetchContactInfo(booking.getContactId(), holder.custName, holder.custEmail, holder.custContact);

        // Fetch and display the address information
        fetchAddressInfo(booking.getAddressId(), holder.custAddress);

        displaySelectedAddOns(booking.getSelectedAddOns(), holder.addOnsTextView, holder.addOnsValTextView);

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            // Navigate to Activity_BookingsInvoice with booking details
            Intent intent = new Intent(context, Activity_BookingsInvoice.class);
            intent.putExtra("booking", booking);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingIdTextView;
        TextView roomTextView;
        TextView checkInText;
        TextView checkOutText;
        TextView totalTextView, subPrice, taxVat, discCode, discVal;
        TextView bookStat, bAndP_date, roomPrice, adultQty, childQty, adultPrice, childPrice, noteMess;
        TextView paymentMethod;
        TextView custName, custEmail, custContact, custAddress;
        TextView addOnsTextView, addOnsValTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingIdTextView = itemView.findViewById(R.id.bookingId_textview);
            roomTextView = itemView.findViewById(R.id.roomNameNumber);
            checkInText = itemView.findViewById(R.id.staydurationval);
            checkOutText = itemView.findViewById(R.id.staydurationoutval);
            totalTextView = itemView.findViewById(R.id.totalValText);
            bookStat = itemView.findViewById(R.id.bookingStatus);
            bAndP_date = itemView.findViewById(R.id.paymentDate_textview);
            roomPrice = itemView.findViewById(R.id.roomNameNumberVals);
            adultQty = itemView.findViewById(R.id.quantityAdult);
            childQty = itemView.findViewById(R.id.quantityChild);
            adultPrice = itemView.findViewById(R.id.quantityAdultVal);
            childPrice = itemView.findViewById(R.id.quantityChildVal);
            noteMess = itemView.findViewById(R.id.notess);
            subPrice = itemView.findViewById(R.id.subtotalVal);
            taxVat = itemView.findViewById(R.id.taxVATVal);
            discCode = itemView.findViewById(R.id.discountVoucher);
            discVal = itemView.findViewById(R.id.discountVoucherVal);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            custName = itemView.findViewById(R.id.cust_name);
            custEmail = itemView.findViewById(R.id.cust_email);
            custContact = itemView.findViewById(R.id.cust_contact);
            custAddress = itemView.findViewById(R.id.cust_address);
            addOnsTextView = itemView.findViewById(R.id.addOns);
            addOnsValTextView = itemView.findViewById(R.id.addOnsVal);
        }
    }

    private String formatPrice(double price) {
        return String.format(Locale.US, "%.2f", price);
    }

    private void fetchCardNumber(String paymentId, TextView paymentMethod) {
        DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference("Payment Information").child(paymentId);

        paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String cardNumber = snapshot.child("cardNumber").getValue(String.class);
                    if (cardNumber != null && cardNumber.length() >= 4) {
                        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
                        paymentMethod.setText("xxxx xxxx xxxx " + lastFourDigits);
                    } else {
                        paymentMethod.setText("Invalid card number");
                    }
                } else {
                    paymentMethod.setText("Payment information not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                paymentMethod.setText("Error fetching payment information");
            }
        });
    }

    private void displaySelectedAddOns(Map<String, String> selectedAddOns, TextView addOnsTextView, TextView addOnsValTextView) {
        if (selectedAddOns != null && !selectedAddOns.isEmpty()) {
            StringBuilder addOnsBuilder = new StringBuilder(); // To build add-on titles string
            double totalAddOnPrice = 0.0; // To calculate total add-on price

            for (Map.Entry<String, String> addOnEntry : selectedAddOns.entrySet()) {
                String addOnTitle = addOnEntry.getKey();
                String addOnPrice = addOnEntry.getValue();

                // Append add-on title to the StringBuilder
                addOnsBuilder.append(addOnTitle).append(", ");

                // Add add-on price to the total price
                totalAddOnPrice += parsePrice(addOnPrice);
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

    private void fetchContactInfo(String contactId, TextView custName, TextView custEmail, TextView custContact) {
        DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference("Contact Information").child(contactId);

        contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String prefix = snapshot.child("prefix").getValue(String.class);
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String mobilePhone = snapshot.child("mobilePhone").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                    String fullName = prefix + " " + firstName + " " + lastName;
                    String contactInfo = mobilePhone + " or " + phone;

                    custName.setText("Name: "+fullName);
                    custEmail.setText("Email: "+email);
                    custContact.setText("Contact No: "+contactInfo);
                } else {
                    custName.setText("Name not found");
                    custEmail.setText("Email not found");
                    custContact.setText("Contact not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                custName.setText("Error fetching contact information");
                custEmail.setText("Error fetching contact information");
                custContact.setText("Error fetching contact information");
            }
        });
    }

    private void fetchAddressInfo(String addressId, TextView custAddress) {
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference("Address Information").child(addressId);

        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String address1 = snapshot.child("address1").getValue(String.class);
                    String address2 = snapshot.child("address2").getValue(String.class);
                    String city = snapshot.child("city").getValue(String.class);
                    String region = snapshot.child("region").getValue(String.class);
                    String country = snapshot.child("country").getValue(String.class);
                    String zip = snapshot.child("zipCode").getValue(String.class);

                    String fullAddress = address1 + "/" + address2 + ";" + city + ", " + region + ", " + country + ", " + zip;

                    custAddress.setText("Address: "+fullAddress);
                } else {
                    custAddress.setText("Address not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                custAddress.setText("Error fetching address information");
            }
        });
    }

    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}