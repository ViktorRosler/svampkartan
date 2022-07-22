package com.example.svampkartan.DataModel;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mushroom implements Serializable {

    public _LocalDateTime date;
    public _LatLng position;
    public MushroomSpecies species;
    public String comment;

    public Mushroom() { }

    public Mushroom(LocalDateTime date, LatLng position, MushroomSpecies species, String comment) {
        this.date = new _LocalDateTime(date);
        this.position = new _LatLng(position);
        this.species = species;
        this.comment = comment;
    }


}
