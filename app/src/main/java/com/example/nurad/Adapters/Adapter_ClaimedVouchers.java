package com.example.nurad.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Models.ClaimedVouchersModel;
import com.example.nurad.R;

import java.util.List;

public class Adapter_ClaimedVouchers extends RecyclerView.Adapter<Adapter_ClaimedVouchers.ClaimedVouchersViewHolder> {

    private List<ClaimedVouchersModel> claimedVouchersList;
    private TextView placeholderTextView; // Reference to the placeholder TextView

    public Adapter_ClaimedVouchers(List<ClaimedVouchersModel> claimedVouchersList, TextView placeholderTextView) {
        this.claimedVouchersList = claimedVouchersList;
        this.placeholderTextView = placeholderTextView;
    }

    @NonNull
    @Override
    public ClaimedVouchersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.claimed_vouchers, parent, false);
        return new ClaimedVouchersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimedVouchersViewHolder holder, int position) {
        ClaimedVouchersModel claimedVoucher = claimedVouchersList.get(position);
        holder.claimedCode.setText(claimedVoucher.getCode());
        holder.claimedStatus.setText("Status: " + claimedVoucher.getStatus());
        holder.claimedDate.setText("Date Redeemed: " + claimedVoucher.getClaimedDate());
    }

    @Override
    public int getItemCount() {
        return claimedVouchersList.size();
    }

    public class ClaimedVouchersViewHolder extends RecyclerView.ViewHolder {
        TextView claimedCode, claimedStatus, claimedDate;

        public ClaimedVouchersViewHolder(@NonNull View itemView) {
            super(itemView);
            claimedCode = itemView.findViewById(R.id.claimed_code);
            claimedStatus = itemView.findViewById(R.id.claimed_status);
            claimedDate = itemView.findViewById(R.id.claimed_date);
        }
    }

    // Method to update claimed vouchers list
    public void updateList(List<ClaimedVouchersModel> newList) {
        claimedVouchersList = newList;
        notifyDataSetChanged();
        updatePlaceholderVisibility();
    }

    // Method to update visibility of placeholder TextView
    private void updatePlaceholderVisibility() {
        if (claimedVouchersList.isEmpty()) {
            placeholderTextView.setVisibility(View.VISIBLE);
        } else {
            placeholderTextView.setVisibility(View.GONE);
        }
    }
}