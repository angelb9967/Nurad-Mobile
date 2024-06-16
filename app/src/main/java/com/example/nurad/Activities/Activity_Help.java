package com.example.nurad.Activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nurad.Adapters.HelpCenterAdapter;
import com.example.nurad.Models.HelpCenterModel;
import com.example.nurad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Activity_Help extends AppCompatActivity {

    private RecyclerView recyclerViewHelpCenter;
    private HelpCenterAdapter helpCenterAdapter;
    private List<HelpCenterModel> helpCenterList;

    private DatabaseReference helpCenterRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        recyclerViewHelpCenter = findViewById(R.id.recyclerViewHelpCenter);
        recyclerViewHelpCenter.setLayoutManager(new LinearLayoutManager(this));
        helpCenterList = new ArrayList<>();
        helpCenterAdapter = new HelpCenterAdapter(this, helpCenterList);
        recyclerViewHelpCenter.setAdapter(helpCenterAdapter);

        helpCenterRef = FirebaseDatabase.getInstance().getReference().child("Help Center");
        helpCenterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                helpCenterList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HelpCenterModel helpCenter = dataSnapshot.getValue(HelpCenterModel.class);
                    if (helpCenter != null) {
                        helpCenterList.add(helpCenter);
                    }
                }
                helpCenterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}