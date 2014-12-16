package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 14.12.2014..
 */
public class Biografija {
    private int id;
    private String profil;
    private String figura;
    private String ime;
    private String prezime;
    private String jmbg;
    private String datumRodjenja;
    private int pol;
    private int visina;
    private int tezina;
    private int velicinaOdece;
    private int brojCipela;
    private int drzava;
    private int mesto;
    private String adresa;
    private String fiksniTelefon;
    private String mobilniTelefon;
    private String email;
    private boolean pusac, uBraku, imaDece;
    private int radNaRacunaru;
    private int stepenStrucneSpremen, delatnost, zanimanje;
    private String ostalaZnanja;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getFigura() {
        return figura;
    }

    public void setFigura(String figura) {
        this.figura = figura;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public int getPol() {
        return pol;
    }

    public void setPol(int pol) {
        this.pol = pol;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
    }

    public int getTezina() {
        return tezina;
    }

    public void setTezina(int tezina) {
        this.tezina = tezina;
    }

    public int getVelicinaOdece() {
        return velicinaOdece;
    }

    public void setVelicinaOdece(int velicinaOdece) {
        this.velicinaOdece = velicinaOdece;
    }

    public int getBrojCipela() {
        return brojCipela;
    }

    public void setBrojCipela(int brojCipela) {
        this.brojCipela = brojCipela;
    }

    public int getDrzava() {
        return drzava;
    }

    public void setDrzava(int drzava) {
        this.drzava = drzava;
    }

    public int getMesto() {
        return mesto;
    }

    public void setMesto(int mesto) {
        this.mesto = mesto;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getFiksniTelefon() {
        return fiksniTelefon;
    }

    public void setFiksniTelefon(String fiksniTelefon) {
        this.fiksniTelefon = fiksniTelefon;
    }

    public String getMobilniTelefon() {
        return mobilniTelefon;
    }

    public void setMobilniTelefon(String mobilniTelefon) {
        this.mobilniTelefon = mobilniTelefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMusko() {
        return getPol() == 1;
    }

    public boolean isZensko() {
        return getPol() == 2;
    }

    public boolean isPusac() {
        return pusac;
    }

    public void setPusac(boolean pusac) {
        this.pusac = pusac;
    }

    public boolean isuBraku() {
        return uBraku;
    }

    public void setuBraku(boolean uBraku) {
        this.uBraku = uBraku;
    }

    public boolean isImaDece() {
        return imaDece;
    }

    public void setImaDece(boolean imaDece) {
        this.imaDece = imaDece;
    }

    public int getRadNaRacunaru() {
        return radNaRacunaru;
    }

    public void setRadNaRacunaru(int radNaRacunaru) {
        this.radNaRacunaru = radNaRacunaru;
    }

    public int getStepenStrucneSpremen() {
        return stepenStrucneSpremen;
    }

    public void setStepenStrucneSpremen(int stepenStrucneSpremen) {
        this.stepenStrucneSpremen = stepenStrucneSpremen;
    }

    public int getDelatnost() {
        return delatnost;
    }

    public void setDelatnost(int delatnost) {
        this.delatnost = delatnost;
    }

    public int getZanimanje() {
        return zanimanje;
    }

    public void setZanimanje(int zanimanje) {
        this.zanimanje = zanimanje;
    }

    public String getOstalaZnanja() {
        return ostalaZnanja;
    }

    public void setOstalaZnanja(String ostalaZnanja) {
        this.ostalaZnanja = ostalaZnanja;
    }
}
