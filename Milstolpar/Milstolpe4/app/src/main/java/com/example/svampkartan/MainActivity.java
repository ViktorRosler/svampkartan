package com.example.svampkartan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton addMushroomButton = findViewById(R.id.AddMushroomButton);
        addMushroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddMushroomActivity();
            }
        });
    }

    private void startAddMushroomActivity() {
        Intent intent = new Intent(this, AddMushroomActivity.class);
        startActivity(intent);
    }
}