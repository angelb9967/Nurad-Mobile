package com.example.nurad.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurad.Interfaces.RecyclerViewInterface;
import com.example.nurad.Models.AccountModel;
import com.example.nurad.R;
import com.example.nurad.Utilities.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Adapter_Account extends RecyclerView.Adapter<Adapter_Account.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_USERS = "users";
    private Context context;
    private ArrayList<AccountModel> list;
    private AccountModel temp;
    View view;
    public Adapter_Account(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList<AccountModel> list){
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.box_accounts, parent, false);
        return new MyViewHolder(view, recyclerViewInterface); // Pass the interface reference
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.email.setText(list.get(position).getEmail());
        holder.menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.removeaccount) { // Remove Account Clicked
                    temp = new AccountModel(list.get(position).getEmail());
                    deleteItem(position);
                }
                return true;
            });
        });
    }

    private void deleteItem(int position){
        // Check if position is valid
        if (position < 0 || position >= list.size()) {
            return; // Invalid position, do nothing
        }

        String emailToRemove = list.get(position).getEmail();

        // Remove the item from the list
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());

        // Remove the email and its corresponding password from preferences
        boolean removedSuccessfully = removeUserFromPreferences(context, emailToRemove);
        if (removedSuccessfully) {
            Toast.makeText(context, "Account removed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to remove account", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to remove email and corresponding password from preferences
    private boolean removeUserFromPreferences(Context context, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceUtils.SHARED_PREFS, Context.MODE_PRIVATE);
        Set<String> userSet = sharedPreferences.getStringSet(PreferenceUtils.KEY_USERS, new HashSet<>());
        Set<String> newUserSet = new HashSet<>();

        boolean removed = false;
        for (String userData : userSet) {
            String[] parts = userData.split("\\|");
            if (!parts[0].equals(email)) {
                newUserSet.add(userData);
            } else {
                removed = true; // Indicate that the email is found and removed
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PreferenceUtils.KEY_USERS, newUserSet);
        editor.apply();

        return removed;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView menu;
        TextView email;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);
            email = itemView.findViewById(R.id.Email_Etxt);
            menu = itemView.findViewById(R.id.menu);

            itemView.setOnClickListener(view -> {
                if(Adapter_Account.this.recyclerViewInterface != null){
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        Adapter_Account.this.recyclerViewInterface.OnItemClick(pos);
                    }
                }
            });
        }
    }
}
