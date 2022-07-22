package com.example.svampkartan.DataModel;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {

    private String userName;
    private String userEmail;
    private MushroomCollection mushroomCollection;

    private _LocalDateTime userCreated;
    private int numberOfMarkersFound;
    private MushroomSpecies latestSpeciesFound;
    private _LocalDateTime latestMarkerCreatedOn;

    public User() { }

    public User(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
        mushroomCollection = new MushroomCollection();
        userCreated = new _LocalDateTime(LocalDateTime.now());
        numberOfMarkersFound = 0;
    }

    public void addMushroom(Mushroom mushroom) {
        mushroomCollection.mushrooms.add(mushroom);
        numberOfMarkersFound += 1;
        latestSpeciesFound = mushroom.species;
        latestMarkerCreatedOn = new _LocalDateTime(LocalDateTime.now());
    }

    public String getUserName() {return userName;}
    public String getUserEmail() {return userEmail;}
    public MushroomCollection getMushroomCollection() {
        return mushroomCollection;
    }

    public _LocalDateTime getUserCreated() {return userCreated;}
    public int getNumberOfMarkersFound() {return numberOfMarkersFound;}
    public MushroomSpecies getLatestSpeciesFound() {return latestSpeciesFound;}
    public _LocalDateTime getLatestMarkerCreatedOn() {return latestMarkerCreatedOn;}

}
