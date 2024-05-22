package com.example.nurad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Models.RoomModel;
import com.example.nurad.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Adapter_Room extends RecyclerView.Adapter<Adapter_Room.RoomViewHolder> {

    private Context context;
    private List<RoomModel> roomList;

    public Adapter_Room(Context context, List<RoomModel> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_room_layout, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomModel room = roomList.get(position);
        holder.roomTitle.setText(room.getTitle());

        // Format the price
        double price = room.getPrice();
        String formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(price);
        holder.roomPrice.setText("₱" + formattedPrice);

        holder.roomDescription.setText(room.getDescription());
        holder.depositRequired.setVisibility(room.isDepositRequired() ? View.VISIBLE : View.GONE);

        // Ensure imageUrl is not null or empty before loading
        if (room.getImageUrl() != null && !room.getImageUrl().isEmpty()) {
            Picasso.get().load(room.getImageUrl()).into(holder.roomImage);
        } else {
            // Set a placeholder or error image if imageUrl is not valid
            holder.roomImage.setImageResource(R.drawable.logo_purple);
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void updateData(List<RoomModel> newRoomList) {
        roomList.clear();
        roomList.addAll(newRoomList);
        notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView roomImage;
        TextView roomTitle;
        TextView depositRequired;
        TextView roomDescription;
        TextView roomPrice;
        TextView priceDetails;
        Button bookNowButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomTitle = itemView.findViewById(R.id.roomTitle);
            depositRequired = itemView.findViewById(R.id.depositRequired);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            priceDetails = itemView.findViewById(R.id.priceDetails);
            bookNowButton = itemView.findViewById(R.id.bookNowButton);
        }
    }
}