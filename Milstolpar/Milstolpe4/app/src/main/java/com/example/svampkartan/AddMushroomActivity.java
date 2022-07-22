package com.example.svampkartan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AddMushroomActivity extends AppCompatActivity {

    DocumentReference ref = FirebaseFirestore.getInstance().collection("milstolpe4").document("Mushrooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mushroom);

        Button addMushroomButton = findViewById(R.id.GotoMapButton);
        addMushroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMapActivity();
            }
        });

        Button addMarkerButton = findViewById(R.id.AddMarkerButton);
        addMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMarker();
            }
        });
    }

    private void startMapActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addMarker() {
        int lat = ThreadLocalRandom.current().nextInt(-90, 90);
        int lon = ThreadLocalRandom.current().nextInt(-180, 180);

        Map<String, Object> markerUpdate = new HashMap<>();
        markerUpdate.put(lat + "", lon + "");
        ref.update(markerUpdate);

    }
}