package com.bitseverywhere.agavajobs.services;

import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.models.domain.DetaljiPosla;
import com.bitseverywhere.agavajobs.models.domain.Drzava;
import com.bitseverywhere.agavajobs.models.domain.Korisnik;
import com.bitseverywhere.agavajobs.models.domain.Mesto;
import com.bitseverywhere.agavajobs.models.domain.Posao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpService {

    private static final String BASE_URL = "http://agava.rs/";

    private static final String API_URL = "http://mobile.agava.rs/api/";

    public List<Posao> vratiPremijumPoslove() throws IOException, JSONException {
        return vratiPoslove(API_URL + "poslovi/premium");
    }

    public List<Posao> vratiHotPoslove() throws IOException, JSONException {
        return vratiPoslove(API_URL + "poslovi/hot");
    }

    public List<Posao> vratiStandardnePoslove() throws IOException, JSONException {
        return vratiPoslove(API_URL + "poslovi/standardni");
    }

    public DetaljiPosla vratiDetaljePosla(int id) throws IOException, JSONException{
        String response = httpGet(API_URL + "poslovi/detalji/" + id);
        JSONObject json = new JSONObject(response);
        JSONArray slikeJson = json.getJSONArray("Slike");
        String[] slike = new String[slikeJson.length()];
        for (int i = 0; i < slikeJson.length(); i++) {
            slike[i] = BASE_URL + slikeJson.getString(i);
        }
        return new DetaljiPosla(json.getInt("Id"),
                json.getBoolean("Arhiviran"),
                json.getString("Logo"),
                json.getString("Naziv"),
                json.getString("Poslodavac"),
                json.getString("Lokacija"),
                json.getString("Rok"),
                json.getString("Obaveze"),
                json.getString("Sifra"),
                json.getString("Zanimanje"),
                json.getString("BrojIzvrsilaca"),
                json.getString("Obrazovanje"),
                slike);
    }

    public Korisnik vratiKorisnika(int id) throws IOException, JSONException {
        String response = httpGet(API_URL + "korisnici/getuser/" + id);
        JSONObject json = new JSONObject(response);
        return new Korisnik(json.getInt("Id"), json.getString("Email"), json.getString("Ime"),
                json.getString("Slika"));
    }

    public List<Drzava> vratiDrzave() throws IOException, JSONException {
        String response = httpGet(API_URL + "sifarnici/VratiDrzave");
        JSONArray drzaveArray = new JSONArray(response);
        List<Drzava> drzave = new ArrayList<>();
        for (int i = 0; i < drzaveArray.length(); i++) {
            JSONObject drzavaJson = drzaveArray.getJSONObject(i);
            Drzava drzava = new Drzava(drzavaJson.getInt("Id"), drzavaJson.getString("Naziv"));
            drzave.add(drzava);
            JSONArray mestaArray = drzavaJson.getJSONArray("Mesta");
            for (int j = 0; j < mestaArray.length(); j++) {
                JSONObject mestoJson = mestaArray.getJSONObject(j);
                Mesto mesto = new Mesto(mestoJson.getInt("Id"), mestoJson.getString("Naziv"),
                        mestoJson.getString("PostanskiBroj"));
                drzava.getMesta().add(mesto);
            }
        }
        return drzave;
    }

    public Biografija vratiBiografiju(int id) throws IOException, JSONException {
        String response = httpGet(API_URL + "korisnici/VratiBiografiju/" + id);
        JSONObject json = new JSONObject(response);
        Biografija biografija = new Biografija();
        biografija.setId(json.getInt("Id"));
        biografija.setIme(json.getString("Ime"));
        biografija.setPrezime(json.getString("Prezime"));
        biografija.setJmbg(json.getString("Jmbg"));
        biografija.setDatumRodjenja(json.getString("DatumRodjenja"));
        biografija.setPol(json.getInt("Pol"));
        biografija.setDrzava(json.getInt("Drzava"));
        biografija.setMesto(json.getInt("Mesto"));
        biografija.setVisina(json.getInt("Visina"));
        biografija.setTezina(json.getInt("Tezina"));
        biografija.setVelicinaOdece(json.getInt("VelicinaOdece"));
        biografija.setBrojCipela(json.getInt("BrojCipela"));
        biografija.setProfil(json.getString("ProfilSlika"));
        biografija.setFigura(json.getString("FiguraSlika"));
        biografija.setAdresa(json.getString("Adresa"));
        biografija.setFiksniTelefon(json.getString("FiksniTelefon"));
        biografija.setMobilniTelefon(json.getString("MobilniTelefon"));
        biografija.setEmail(json.getString("Email"));
        return biografija;
    }

    private List<Posao> vratiPoslove(String url) throws IOException, JSONException {
        String response = httpGet(url);
        JSONArray result = new JSONArray(response);
        return vratiPoslove(result);
    }

    private List<Posao> vratiPoslove(JSONArray jsonArray) throws JSONException {
        ArrayList<Posao> poslovi = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Posao posao = new Posao(json.getInt("Id"),
                    json.getString("Naziv"),
                    json.getString("Poslodavac"),
                    json.getString("Lokacija"),
                    json.getString("Rok"),
                    json.getString("Zanimanje"),
                    json.getString("Image"));
            poslovi.add(posao);
        }
        return poslovi;
    }


    private String httpGet(String url) throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet getMethod = new HttpGet(url);
        HttpResponse response = client.execute(getMethod);
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
        throw new IOException("Nothing returned");
    }
}
