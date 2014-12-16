package com.bitseverywhere.agavajobs.models.domain;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Željko on 16.12.2014..
 */
public class Delatnost {
    private int id;
    private String naziv;
    private List<Zanimanje> zanimanja = new ArrayList<>();

    public Delatnost(int id, String naziv) {
        this.naziv = naziv;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public List<Zanimanje> getZanimanja() {
        return zanimanja;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
