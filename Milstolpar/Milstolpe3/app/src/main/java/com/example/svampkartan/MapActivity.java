package com.example.svampkartan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button mapActivityButton = findViewById(R.id.activitybutton2);
        mapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map_activity_button_click();
            }
        });

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle_button_click();
            }
        });

    }

    private void map_activity_button_click() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void toggle_button_click()
    {
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        ImageView view = findViewById(R.id.imageView);

        if (toggleButton.isChecked()) {
            view.setColorFilter(getResources().getColor(R.color.blue));
        } else {
            view.setColorFilter(getResources().getColor(R.color.red));
        }
    }
}