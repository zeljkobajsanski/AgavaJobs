package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 14.12.2014..
 */
public class Korisnik {
    private int id;
    private String email;
    private String ime;
    private String slika;

    public Korisnik(int id, String email, String ime, String slika) {
        this.id = id;
        this.email = email;
        this.ime = ime;
        this.slika = slika;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getIme() {
        return ime;
    }

    public String getSlika() {
        return slika;
    }
}
