package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 18.12.2014..
 */
public class Jezik {
    private int id;
    private String naziv;

    public Jezik(int id, String naziv) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jezik jezik = (Jezik) o;

        if (id != jezik.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
