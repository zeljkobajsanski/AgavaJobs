package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 16.12.2014..
 */
public class Zanimanje {
    private int id;
    private String naziv;

    public Zanimanje(int id, String naziv) {
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
        return getNaziv();
    }
}
