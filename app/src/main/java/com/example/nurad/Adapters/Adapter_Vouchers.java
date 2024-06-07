package com.example.nurad.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Models.VoucherModel;
import com.example.nurad.R;

import java.util.List;
import java.util.Locale;

public class Adapter_Vouchers extends RecyclerView.Adapter<Adapter_Vouchers.VoucherViewHolder> {

    private List<VoucherModel> voucherList;

    public Adapter_Vouchers(List<VoucherModel> voucherList) {
        this.voucherList = voucherList;
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
        holder.voucherTitle.setText(voucher.getTitle());
        holder.voucherDescription.setText(voucher.getDescription());
        holder.voucherValidity.setText("Valid Until: " +voucher.getValidity());
        double value = voucher.getValue();
        String formattedValue = formatPrice(value);
        holder.voucherValue.setText("Get a Discount Value of: ₱" + formattedValue);

        holder.voucherCode.setText("Redeem Code: " +voucher.getCode());
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView voucherTitle, voucherDescription, voucherValidity, voucherValue, voucherCode;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherTitle = itemView.findViewById(R.id.voucher_title);
            voucherDescription = itemView.findViewById(R.id.voucher_description);
            voucherValidity = itemView.findViewById(R.id.voucher_validity);
            voucherValue = itemView.findViewById(R.id.voucher_value);
            voucherCode = itemView.findViewById(R.id.voucher_code);
        }
    }

    private String formatPrice(double price) {
        return String.format(Locale.US, "%.2f", price);
    }
}