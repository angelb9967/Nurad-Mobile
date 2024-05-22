package com.example.nurad.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Models.AboutUsModel;
import com.example.nurad.R;
import java.util.List;

public class Adapter_AboutUs extends RecyclerView.Adapter<Adapter_AboutUs.ViewHolder> {
    private List<AboutUsModel> aboutUsList;

    public Adapter_AboutUs(List<AboutUsModel> aboutUsList) {
        this.aboutUsList = aboutUsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_us_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AboutUsModel aboutUsModel = aboutUsList.get(position);
        holder.titleTextView.setText(aboutUsModel.getTitle());
        holder.descriptionTextView.setText(aboutUsModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return aboutUsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
