package com.bitseverywhere.agavajobs.models.domain;

public class StepenStrucneSpreme {
    private int id;
    private String naziv;

    public StepenStrucneSpreme(int id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
