package com.example.svampkartan.DataModel;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MushroomCollection implements Iterable<Mushroom>, Serializable {

    public boolean shared;
    public ArrayList<Mushroom> mushrooms;

    public MushroomCollection() {
        shared = true;
        mushrooms = new ArrayList<>();
    }

    @NonNull
    @Override
    public Iterator<Mushroom> iterator() {
        return mushrooms.iterator();
    }
}
