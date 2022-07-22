package com.example.svampkartan.DataModel;

import com.example.svampkartan.R;

import java.io.Serializable;

public enum MushroomSpecies implements Serializable {
    Boletus_Edulis(R.drawable.boletus_edulis_2, R.drawable.boletus_edulis_big_marker),
    Cantharellus_Cibarius(R.drawable.cantharellus_cibarius_2, R.drawable.cantharellus_cibarius_big_marker),
    Psilocybe_Semilanceata(R.drawable.psilocybe_semilanceata_3, R.drawable.psilocybe_semilanceata_big_marker),
    Coprinus_Comatus(R.drawable.coprinus_comatus_2, R.drawable.coprinus_comatus_big_marker),
    Agaricus_Campestris(R.drawable.agaricus_campestris_2, R.drawable.agaricus_campestris_big_marker),
    Sparassis_Crispa(R.drawable.sparassis_crispa, R.drawable.sparassis_crispa_big_marker);

    private final int imageId;
    private final int markerImageId;
    MushroomSpecies(int imageId, int markerImageId) {
        this.imageId = imageId;
        this.markerImageId = markerImageId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getMarkerImageId() {
        return markerImageId;
    }

    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }

}
