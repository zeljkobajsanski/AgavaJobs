package com.bitseverywhere.agavajobs.models.domain;

public class DetaljiPosla {
    private int ID;
    private boolean arhiviran;
    private String logo;
    private String naziv;
    private String poslodavac;
    private String lokacija;
    private String rok;
    private String opis;
    private String sifra;
    private String zanimanje;
    private String brojIzvrsilaca;
    private String obrazovanje;
    private String[] slike;

    public DetaljiPosla(int ID, boolean arhiviran, String logo, String naziv, String poslodavac,
                        String lokacija, String rok, String opis, String sifra, String zanimanje,
                        String brojIzvrsilaca, String obrazovanje, String[] slike) {
        this.ID = ID;
        this.arhiviran = arhiviran;
        this.logo = logo;
        this.naziv = naziv;
        this.poslodavac = poslodavac;
        this.lokacija = lokacija;
        this.rok = rok;
        this.opis = opis;
        this.sifra = sifra;
        this.zanimanje = zanimanje;
        this.brojIzvrsilaca = brojIzvrsilaca;
        this.obrazovanje = obrazovanje;
        this.slike = slike;
    }

    public int getID() {
        return ID;
    }

    public boolean isArhiviran() {
        return arhiviran;
    }

    public String getLogo() {
        return logo;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getPoslodavac() {
        return poslodavac;
    }

    public String getLokacija() {
        return lokacija;
    }

    public String getRok() {
        return rok;
    }

    public String getOpis() {
        return opis;
    }

    public String getSifra() {
        return sifra;
    }

    public String getZanimanje() {
        return zanimanje;
    }

    public String getBrojIzvrsilaca() {
        return brojIzvrsilaca;
    }

    public String getObrazovanje() {
        return obrazovanje;
    }

    public String[] getSlike() {
        return slike;
    }
}
