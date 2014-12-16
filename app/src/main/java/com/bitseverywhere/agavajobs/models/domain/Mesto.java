package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 14.12.2014..
 */
public class Mesto {
    private int id;
    private String naziv;
    private String postanskiBroj;

    public Mesto(int id, String naziv, String postanskiBroj) {
        this.id = id;
        this.naziv = naziv;
        this.postanskiBroj = postanskiBroj;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    @Override
    public String toString() {
        return naziv + " (" + postanskiBroj + ")";
    }
}
