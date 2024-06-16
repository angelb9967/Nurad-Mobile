package com.example.nurad.Activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.CompoundButtonCompat;
import com.example.nurad.R;

public class Activity_Rate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the RatingBar and set the progress and background tint colors
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        RadioGroup choicesRadioGroup = findViewById(R.id.choicesRadioGroup);
        int filledColor = getResources().getColor(R.color.rating_selected_color);
        int unfilledColor = getResources().getColor(R.color.rating_unfilled_color);

        ratingBar.setProgressTintList(ColorStateList.valueOf(filledColor));
        ratingBar.setSecondaryProgressTintList(ColorStateList.valueOf(filledColor));
        ratingBar.setProgressBackgroundTintList(ColorStateList.valueOf(unfilledColor));

        // Set RadioButton color on selection
        for (int i = 0; i < choicesRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) choicesRadioGroup.getChildAt(i);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(Color.parseColor("#882065")));
                    } else {
                        CompoundButtonCompat.setButtonTintList(radioButton, ColorStateList.valueOf(Color.parseColor("#000000")));
                    }
                }
            });
        }

    }
}