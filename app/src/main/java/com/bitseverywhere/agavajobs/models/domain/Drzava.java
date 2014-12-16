package com.bitseverywhere.agavajobs.models.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Željko on 14.12.2014..
 */
public class Drzava {
    private int id;
    private String naziv;
    private List<Mesto> mesta = new ArrayList<>();

    public Drzava(int id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public List<Mesto> getMesta() {
        return mesta;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
