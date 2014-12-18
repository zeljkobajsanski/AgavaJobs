package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 17.12.2014..
 */
public class RadnoIskustvo {
    private int id;
    private String poslodavac;
    private String period;
    private String opis;

    public RadnoIskustvo() {
    }

    public RadnoIskustvo(int id, String poslodavac, String period, String opis) {
        this.id = id;
        this.poslodavac = poslodavac;
        this.period = period;
        this.opis = opis;
    }

    public int getId() {
        return id;
    }

    public String getPoslodavac() {
        return poslodavac;
    }

    public String getPeriod() {
        return period;
    }

    public String getOpis() {
        return opis;
    }

    public void setPoslodavac(String poslodavac) {
        this.poslodavac = poslodavac;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
