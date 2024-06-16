package com.example.nurad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Models.HelpCenterModel;
import com.example.nurad.R;

import java.util.List;

public class HelpCenterAdapter extends RecyclerView.Adapter<HelpCenterAdapter.ViewHolder> {

    private Context context;
    private List<HelpCenterModel> helpCenterList;

    public HelpCenterAdapter(Context context, List<HelpCenterModel> helpCenterList) {
        this.context = context;
        this.helpCenterList = helpCenterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_help_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelpCenterModel helpCenter = helpCenterList.get(position);
        holder.titleTextView.setText(helpCenter.getTitle());
        holder.descriptionTextView.setText(helpCenter.getDescription());
    }

    @Override
    public int getItemCount() {
        return helpCenterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}