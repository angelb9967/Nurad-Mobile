package com.example.nurad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Activities.Activity_BookingsInvoice;
import com.example.nurad.Models.Model_Booking;
import com.example.nurad.R;

import java.util.List;
import java.util.Locale;

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
        holder. bAndP_date.setText(booking.getBookingDate());
        holder.roomPrice.setText("₱ " + formatPrice(booking.getRoomPrice()));
        holder.adultQty.setText(booking.getAdultCount()+" x Adult");
        holder.childQty.setText(booking.getChildCount()+" x Adult");
        holder.adultPrice.setText("₱ " + formatPrice(booking.getExtraAdultPrice()));
        holder.childPrice.setText("₱ " + formatPrice(booking.getExtraChildPrice()));
        holder.noteMess.setText(booking.getNotes());
        holder.subPrice.setText("₱ " + formatPrice(booking.getSubtotalValue()));
        holder.taxVat.setText("₱ " + formatPrice(booking.getVatValue()));
        holder.discCode.setText("Discount" + " (" + booking.getVoucherCode() + ")");
        holder.discVal.setText("₱ " + formatPrice(booking.getVoucherValueValue()));

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
        }
    }

    private String formatPrice(double price) {
        return String.format(Locale.US, "%.2f", price);
    }
}