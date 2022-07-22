

package com.example.svampkartan;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.svampkartan.DataModel.User;
import com.example.svampkartan.DataModel.MushroomCollection;
import com.example.svampkartan.DataModel.Mushroom;
import com.example.svampkartan.DataModel.MushroomSpecies;
import com.example.svampkartan.Fragments.AddMushroom.AddMushroomFragment;
import com.example.svampkartan.Fragments.AddMushroom.AddMushroomListener;
import com.example.svampkartan.Fragments.BlankFragment;
import com.example.svampkartan.Fragments.FragmentListener;
import com.example.svampkartan.Fragments.IdentifyFragment;
import com.example.svampkartan.Fragments.SettingsFragment;
import com.example.svampkartan.Fragments.SignIn.SignInFragment;
import com.example.svampkartan.Fragments.SignIn.UserAccountFragment;
import com.example.svampkartan.Fragments.SignIn.SignInListener;
import com.example.svampkartan.Fragments.SignIn.UserAccountListener;
import com.example.svampkartan.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        FragmentListener, AddMushroomListener, SignInListener, UserAccountListener {


    /*******************************************************
                           Class Members
     ******************************************************/

    // map
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng currentUserLocation;
    private float markerZIndex;

    // fragments
    private int fragmentId;
    private BlankFragment blankFragment;
    private AddMushroomFragment addMushroomFragment;
    private IdentifyFragment identifyFragment;
    private SignInFragment signInFragment;
    private UserAccountFragment userAccountFragment;
    private SettingsFragment settingsFragment;
    public boolean defaultTheme;

    // user data
    private User signedInUser;
    private FirebaseDatabase firebaseDatabase;
    private final String firebaseURL = "https://svampkartan-92ffe-default-rtdb.europe-west1.firebasedatabase.app/";

    // user authentication
    private FirebaseAuth mAuth;

    // buttons
    private ImageButton addMushroomButton;
    private ImageButton identifyButton;
    private ImageButton loginButton;
    private ImageButton settingsButton;

    /******************************************************
                    OnCreate / OnMapReady
     ******************************************************/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentId", fragmentId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // init fragments
        fragmentId = 0;
        blankFragment = new BlankFragment();
        addMushroomFragment = new AddMushroomFragment();
        identifyFragment = new IdentifyFragment();
        signInFragment = new SignInFragment();
        settingsFragment = new SettingsFragment();

        // set fragment style options
        defaultTheme = true;

        // Add button listeners
        addMushroomButton = findViewById(R.id.ShowAddMushroomFragmentButton);
        addMushroomButton.setOnClickListener(view -> changeFragment(addMushroomFragment, 1, addMushroomButton));

        identifyButton = findViewById(R.id.ShowIdentifyMushroomFragmentButton);
        identifyButton.setOnClickListener(view -> changeFragment(identifyFragment, 2, identifyButton));

        loginButton = findViewById(R.id.ShowLoginFragmentButton);
        loginButton.setOnClickListener(view -> {
            if (mAuth != null && mAuth.getCurrentUser() != null) {
                changeFragment(userAccountFragment, 3, loginButton);
            } else {
                changeFragment(signInFragment, 3, loginButton);
            }
        });

        settingsButton = findViewById(R.id.ShowOptionsFragmentButton);
        settingsButton.setOnClickListener(v -> changeFragment(settingsFragment, 4, settingsButton));

        // restore state after rotate
        if (savedInstanceState != null) {
            fragmentId = savedInstanceState.getInt("fragmentId");
            if (fragmentId == 1) {
                toggleButtonBackground(addMushroomButton);
            } else if  (fragmentId == 2) {
                toggleButtonBackground(identifyButton);
            } else if(fragmentId == 3) {
                toggleButtonBackground(loginButton);
            } else if(fragmentId == 4) {
                toggleButtonBackground(settingsButton);
            }
        }

        firebaseDatabase = FirebaseDatabase.getInstance(firebaseURL);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Remove default location markers
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        // load user data
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            // load from firebase
            loadUserFromDB(false);
        } else {
            // create temp user
            signedInUser = new User("user1", "user1@gmail.com");
        }

        // create markers
        markerZIndex = 0;
        createMarkers();

        // set marker ClickListener
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getZIndex() == 10000) {
                marker.setZIndex(markerZIndex);
                return true;
            } else {
                marker.setZIndex(10000);
                return false;
            }
        });

        // setup marker info window
        Context mContext = this;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        // setup location updates
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationUpdates();

    }

    /******************************************************
                        Fragment Functions
     ******************************************************/

    private void changeFragment(Fragment fragment, int id, ImageButton button) {
        if (fragmentId == id) {
            fragment = blankFragment;
            fragmentId = 0;
            toggleButtonBackground(null);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
        } else {
            fragmentId = id;
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            toggleButtonBackground(button);
            if (id == 2) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, 102);
                }
            }
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, fragment)
                .commitNow();
    }

    @Override
    public void changeToBlankFragment() {
        changeFragment(blankFragment, 0, null);
    }

    @Override
    public void onBackPressed() {
        if (fragmentId != 0) {
            changeFragment(blankFragment, 0, null);
        } else {
            super.onBackPressed();
        }
    }

    private void toggleButtonBackground(ImageButton button) {
        ArrayList<ImageButton> buttons = new ArrayList<>(Arrays.asList(addMushroomButton,
                identifyButton, loginButton, settingsButton));
        for (ImageButton b : buttons) {
            if (b == button) {
                b.setBackgroundResource(R.drawable.roundbuttonselected);
            } else {
                b.setBackgroundResource(R.drawable.roundbutton);
            }
        }
    }

    /******************************************************
                          Map Functions
     ******************************************************/

    @SuppressLint("MissingPermission") // suppress false positive from android studio
    private void getLocationUpdates() {
        // check for GPS permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // enable user tracking on map
            mMap.setMyLocationEnabled(true);

            // move map camera to last known user location
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 14.0f));
            });

            // enable automatic user location updates
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(500)
                    .setFastestInterval(0)
                    .setMaxWaitTime(0)
                    .setSmallestDisplacement(0)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }, null);
        } else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            getLocationUpdates();
        }
    }

    /******************************************************
                        User Functions
     ******************************************************/

    public void addMushroom(MushroomSpecies species, String comment) {
        Mushroom mushroom = new Mushroom(LocalDateTime.now(), currentUserLocation, species, comment);
        signedInUser.addMushroom(mushroom);
        if (mAuth != null) {
           uploadData();
        }
        addMarker(mushroom);
    }

    private void addMarker(Mushroom mushroom) {
        Bitmap a = BitmapFactory.decodeResource(this.getResources(), mushroom.species.getMarkerImageId());
        Bitmap b = Bitmap.createScaledBitmap(a, 120, 120, false);
        mMap.addMarker(new MarkerOptions().position(mushroom.position.toLatLng())
                .anchor(0.5f, 0.5f)
                .zIndex(markerZIndex)
                .title(mushroom.species.toString())
                .snippet(mushroom.date.toLocalDateTime().toString().substring(0, 10) + "\n" + mushroom.comment))
                .setIcon(BitmapDescriptorFactory.fromBitmap(b));
        markerZIndex += 1;
    }

    private void createMarkers() {
        if (signedInUser != null) {
            MushroomCollection mushroomCollection = signedInUser.getMushroomCollection();
            for (Mushroom mushroom : mushroomCollection) {
                addMarker(mushroom);
            }
        }
    }

    private void uploadData() {
        firebaseDatabase.getReference(signedInUser.getUserName()).setValue(signedInUser);
    }

    private void loadUserFromDB(Boolean changeToUserAccountFragment) {
        String userName = mAuth.getCurrentUser().getEmail().split("@")[0];
        firebaseDatabase.getReference(userName)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.getValue() == null) {
                        signedInUser = new User(userName, mAuth.getCurrentUser().getEmail());
                        uploadData();
                    } else {
                        signedInUser = dataSnapshot.getValue(User.class);
                        createMarkers();
                    }
                    userAccountFragment = new UserAccountFragment(signedInUser);
                    if (changeToUserAccountFragment) {
                        fragmentId = -1;
                        changeFragment(userAccountFragment, 2, findViewById(R.id.ShowLoginFragmentButton));
                    }
                });
    }

    @Override
    public void onUserSignIn(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        loadUserFromDB(true);
    }

    @Override
    public void onUserSignOut() {
        mAuth.signOut();
        signedInUser = null;
        fragmentId = -1;
        changeFragment(signInFragment, 2, findViewById(R.id.ShowLoginFragmentButton));
    }
}