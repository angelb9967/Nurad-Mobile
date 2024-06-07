package com.example.nurad.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Models.VoucherModel;
import com.example.nurad.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter_Vouchers extends RecyclerView.Adapter<Adapter_Vouchers.VoucherViewHolder> {

    private List<VoucherModel> voucherList;
    private SimpleDateFormat dateFormat;
    private DatabaseReference expiredVouchersRef;

    public Adapter_Vouchers(List<VoucherModel> voucherList) {
        this.voucherList = voucherList;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        expiredVouchersRef = database.getReference().child("Expired Vouchers");
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        VoucherModel voucher = voucherList.get(position);

        // Check if the voucher status is "Active" before binding
        if (voucher.getStatus().equals("Active")) {
            holder.bindVoucher(voucher);
        } else {
            // Hide the card view if the voucher is not active
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public void setVoucherList(List<VoucherModel> voucherList) {
        this.voucherList = voucherList;
        notifyDataSetChanged();
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView voucherTitle, voucherDescription, voucherValidity, voucherValue, voucherCode;
        CardView cardView;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            voucherTitle = itemView.findViewById(R.id.voucher_title);
            voucherDescription = itemView.findViewById(R.id.voucher_description);
            voucherValidity = itemView.findViewById(R.id.voucher_validity);
            voucherValue = itemView.findViewById(R.id.voucher_value);
            voucherCode = itemView.findViewById(R.id.voucher_code);
        }

        public void bindVoucher(VoucherModel voucher) {
            voucherTitle.setText(voucher.getTitle());
            voucherDescription.setText(voucher.getDescription());
            voucherValidity.setText("Valid Until: " + voucher.getValidity());
            double value = voucher.getValue();
            String formattedValue = formatPrice(value);
            voucherValue.setText("Get a Discount Value of: ₱" + formattedValue);
            voucherCode.setText("Redeem Code: " + voucher.getCode());

            if (isVoucherExpired(voucher.getValidity())) {
                // Move voucher to "Expired Vouchers" node in Firebase database
                moveVoucherToExpired(voucher);
            }
        }

        private String formatPrice(double price) {
            return String.format(Locale.US, "%.2f", price);
        }

        private void moveVoucherToExpired(VoucherModel voucher) {
            // Set the status of the voucher to "Expired"
            voucher.setStatus("Expired");

            // Get the voucher key
            String voucherKey = voucher.getKey();

            if (voucherKey != null) {
                // Update the status of the voucher to "Expired" in the "Vouchers" node
                DatabaseReference originalVoucherRef = FirebaseDatabase.getInstance().getReference().child("Vouchers").child(voucherKey).child("status");
                originalVoucherRef.setValue("Expired");

                // Push the voucher to the "Expired Vouchers" node with the original key
                expiredVouchersRef.child(voucherKey).setValue(voucher);

                // Remove the voucher from the original node
                FirebaseDatabase.getInstance().getReference().child("Vouchers").child(voucherKey).removeValue();
            } else {
                // Handle the case where voucher key is null
                Log.e("Adapter_Vouchers", "Voucher key is null");
            }
        }

        private boolean isVoucherExpired(String validity) {
            if (validity != null && !validity.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                Date validityDate;
                try {
                    validityDate = sdf.parse(validity);
                    Date currentDate = new Date();
                    return currentDate.after(validityDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false; // Handle parsing exception
                }
            } else {
                // Handle the case where validity is null or empty
                return false;
            }
        }
    }
}