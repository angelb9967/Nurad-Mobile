package com.example.nurad.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Activities.Activity_BottomNav;
import com.example.nurad.Fragments.Fragment_Booking;
import com.example.nurad.Fragments.Fragment_Search;
import com.example.nurad.Models.RoomModel;
import com.example.nurad.R;
import com.example.nurad.Models.Model_PriceRule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Adapter_Room extends RecyclerView.Adapter<Adapter_Room.RoomViewHolder> {

    private Context context;
    private List<RoomModel> roomList;
    private Fragment_Search.OnRoomSelectedListener listener;
    private DatabaseReference priceRules_DBref;

    public Adapter_Room(Context context, List<RoomModel> roomList, Fragment_Search.OnRoomSelectedListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.listener = listener;
        this.priceRules_DBref = FirebaseDatabase.getInstance().getReference("PriceRules");
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
        holder.roomDescription.setText(room.getDescription());
        holder.depositRequired.setVisibility(room.isDepositRequired() ? View.VISIBLE : View.GONE);
        holder.recommendedTag.setVisibility(room.isRecommended() ? View.INVISIBLE : View.GONE);
        holder.roomName.setText(room.getName());
        holder.roomType.setText(room.getType());

        // Ensure imageUrl is not null or empty before loading
        if (room.getImageUrl() != null && !room.getImageUrl().isEmpty()) {
            Picasso.get().load(room.getImageUrl()).into(holder.roomImage);
        } else {
            // Set a placeholder or error image if imageUrl is not valid
            holder.roomImage.setImageResource(R.drawable.logo_purple);
        }

        // Fetch the price rule for the room
        if (room.getPriceRule() != null) {
            fetchPriceRule(room.getPriceRule(), holder);
        } else {
            // Set a default price if no price rule is available
            holder.roomPrice.setText("₱" + formatPrice(room.getPrice()));
        }

        if (holder.roomName != null) {
            holder.roomName.setText(room.getName());
        } else {
            Log.e("Adapter_Room", "roomNameTextView is null!");
        }

        if (holder.roomType != null) {
            holder.roomType.setText(room.getType());
        } else {
            Log.e("Adapter_Room", "roomTypeTextView is null!");
        }

        if (holder.roomPrice != null) {
            holder.roomPrice.setText("Price: ₱" + room.getPrice());
        } else {
            Log.e("Adapter_Room", "roomPriceTextView is null!");
        }

        holder.bookNowButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoomSelected(room);
            }

            // Pass the selected room details to Fragment_Booking
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectedRoom", room);
            bundle.putString("title", room.getTitle());
            bundle.putString("description", room.getDescription());
            bundle.putString("roomName", room.getName());
            bundle.putString("roomType", room.getType());
            bundle.putString("priceRule", room.getPriceRule());
            Fragment_Booking fragmentBooking = Fragment_Booking.newInstance(room);

            // Ensure the context is an instance of FragmentActivity
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                fragmentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragmentBooking)
                        .addToBackStack(null)
                        .commit();

                // Update the Selected Fragment in the Bottom Navigation
                Activity_BottomNav activity = (Activity_BottomNav) fragmentActivity;
                activity.updateSelectedNavItem(fragmentBooking);
            } else {
                // Handle the case where context is not a FragmentActivity
                Toast.makeText(context, "Unable to open booking. Context is not a FragmentActivity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    private void fetchPriceRule(String priceRuleName, RoomViewHolder holder) {
        priceRules_DBref.child(priceRuleName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Model_PriceRule priceRule = snapshot.getValue(Model_PriceRule.class);
                    if (priceRule != null) {
                        // Get the appropriate price based on the current day of the week
                        double price = getPriceForCurrentDay(priceRule);
                        String formattedPrice = formatPrice(price);
                        holder.roomPrice.setText("₱" + formattedPrice);
                    } else {
                        // Handle null price rule scenario
                        holder.roomPrice.setText("₱" + formatPrice(0));
                    }
                } else {
                    // Handle non-existent price rule scenario
                    holder.roomPrice.setText("₱" + formatPrice(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error scenario
                holder.roomPrice.setText("₱" + formatPrice(0));
            }
        });
    }

    private double getPriceForCurrentDay(Model_PriceRule priceRule) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.FRIDAY:
                return priceRule.getFriday_price();
            case Calendar.SATURDAY:
                return priceRule.getSaturday_price();
            case Calendar.SUNDAY:
                return priceRule.getSunday_price();
            default:
                return priceRule.getPrice();
        }
    }

    private String formatPrice(double price) {
        return String.format(Locale.US, "%.2f", price);
    }

    public void updateData(List<RoomModel> newRoomList) {
        roomList.clear();
        roomList.addAll(newRoomList);
        notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView roomImage;
        TextView roomTitle;
        TextView roomDescription;
        TextView depositRequired;
        TextView roomName; // Add this line
        TextView roomType; // Add this line
        TextView roomPrice;
        TextView priceDetails;
        TextView recommendedTag;
        Button bookNowButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomTitle = itemView.findViewById(R.id.roomTitle);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            depositRequired = itemView.findViewById(R.id.depositRequired);
            roomName = itemView.findViewById(R.id.roomName); // Add this line
            roomType = itemView.findViewById(R.id.roomType); // Add this line
            roomPrice = itemView.findViewById(R.id.roomPrice);
            priceDetails = itemView.findViewById(R.id.priceDetails);
            recommendedTag = itemView.findViewById(R.id.recommendedTag);
            bookNowButton = itemView.findViewById(R.id.bookNowButton);
        }
    }
}