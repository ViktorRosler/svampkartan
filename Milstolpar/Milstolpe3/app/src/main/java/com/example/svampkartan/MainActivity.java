package com.example.svampkartan;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mainbutton = findViewById(R.id.mainbutton);
        mainbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_button_click();
            }
        });

        Button activitybutton = findViewById(R.id.activitybutton);
        activitybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_button_click();
            }
        });

    }

    private void main_button_click() {
        ((TextView)findViewById(R.id.textView)).setText("You clicked the Button!");
    }

    private void activity_button_click() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);

    }
}