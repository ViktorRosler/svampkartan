package com.example.svampkartan.DataModel;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class _LatLng implements Serializable {

    public double latitude;
    public double longitude;

    public _LatLng() {}

    public _LatLng(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

}
