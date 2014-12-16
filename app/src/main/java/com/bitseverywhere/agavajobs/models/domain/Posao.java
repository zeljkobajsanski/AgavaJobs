package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 13.12.2014..
 */
public class Posao {
    private int ID;
    private String naziv;
    private String poslodavac;
    private String lokacija;
    private String rok;
    private String zanimanje;
    private String slika;

    public Posao(int ID, String naziv, String poslodavac, String lokacija, String rok, String zanimanje, String slika) {
        this.ID = ID;
        this.naziv = naziv;
        this.poslodavac = poslodavac;
        this.lokacija = lokacija;
        this.rok = rok;
        this.zanimanje = zanimanje;
        this.slika = slika;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPoslodavac() {
        return poslodavac;
    }

    public void setPoslodavac(String poslodavac) {
        this.poslodavac = poslodavac;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public String getZanimanje() {
        return zanimanje;
    }

    public void setZanimanje(String zanimanje) {
        this.zanimanje = zanimanje;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }
}
