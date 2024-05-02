package com.example.nurad.Adapters;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class Adapter_CustomAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> originalList;
    private List<String> filteredList;

    public Adapter_CustomAutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List<String> originalList) {
        super(context, resource, originalList);
        this.originalList = new ArrayList<>(originalList);
        this.filteredList = new ArrayList<>(originalList);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public String getItem(int position) {
        return filteredList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    // No constraint, return the original list
                    results.values = originalList;
                    results.count = originalList.size();
                } else {
                    // Filter the original list based on the constraint
                    List<String> filtered = new ArrayList<>();
                    for (String item : originalList) {
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filtered.add(item);
                        }
                    }
                    // Add "Manage Saved Passwords" option at the bottom
                    filtered.add("Manage Saved Logins");
                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
