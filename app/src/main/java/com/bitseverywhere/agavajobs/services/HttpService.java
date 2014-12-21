package com.bitseverywhere.agavajobs.services;

import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.models.domain.Delatnost;
import com.bitseverywhere.agavajobs.models.domain.DetaljiPosla;
import com.bitseverywhere.agavajobs.models.domain.Drzava;
import com.bitseverywhere.agavajobs.models.domain.Jezik;
import com.bitseverywhere.agavajobs.models.domain.Korisnik;
import com.bitseverywhere.agavajobs.models.domain.Mesto;
import com.bitseverywhere.agavajobs.models.domain.Posao;
import com.bitseverywhere.agavajobs.models.domain.RadnoIskustvo;
import com.bitseverywhere.agavajobs.models.domain.Stats;
import com.bitseverywhere.agavajobs.models.domain.StepenStrucneSpreme;
import com.bitseverywhere.agavajobs.models.domain.Zanimanje;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpService {

    private static final String BASE_URL = "http://agava.rs/";

    private static final String API_URL = "http://agava.brizb.rs/api/";

    //private static final String API_URL = "http://mobile.agava.rs/api/";

    //private static final String API_URL = "http://192.168.1.2/Agava.Mobile.Api/api/";

    private static HttpService instance = new HttpService();

    private HttpService() {

    }

    public static HttpService getInstance() {
        return instance;
    }

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
        biografija.setDrzava(json.optInt("Drzava"));
        biografija.setMesto(json.optInt("Mesto"));
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
        biografija.setPusac(json.getBoolean("Pusac"));
        biografija.setuBraku(json.getBoolean("UBraku"));
        biografija.setImaDece(json.getBoolean("ImaDece"));
        biografija.setRadNaRacunaru(json.optInt("RadNaRacunaru", 0));
        if (!json.isNull("StrucnaSprema")) {
            biografija.setStepenStrucneSpremen(json.getInt("StrucnaSprema"));
        }
        if (!json.isNull("VrstaZanimanja")) {
            biografija.setDelatnost(json.getInt("VrstaZanimanja"));
        }
        if (!json.isNull("Zanimanje")) {
            biografija.setZanimanje(json.getInt("Zanimanje"));
        }
        biografija.setOstalaZnanja(json.getString("OstalaZnanja"));
        JSONArray zmrJson = json.getJSONArray("ZeljenaMestaRada");
        for (int i = 0; i < zmrJson.length(); i++) {
            JSONObject d = zmrJson.getJSONObject(i);
            biografija.getZeljeneDrzaveRada().add(new Drzava(d.getInt("Id"),
                    d.getString("Naziv")));
        }
        JSONArray radnoIskustvo = json.getJSONArray("RadnoIskustvo");
        for (int i = 0; i < radnoIskustvo.length(); i++) {
            JSONObject ri = radnoIskustvo.getJSONObject(i);
            biografija.getRadnoIskustvo().add(new RadnoIskustvo(ri.getInt("Id"),
                    ri.getString("Poslodavac"),
                    ri.getString("Period"),
                    ri.getString("OpisPosla")));
        }
        JSONArray prihvatljivaZanimanja = json.getJSONArray("PrihvatljivaZanimanja");
        for (int i = 0; i < prihvatljivaZanimanja.length(); i++) {
            JSONObject z = prihvatljivaZanimanja.getJSONObject(i);
            biografija.getPrihvatljivaZanimanja().add(new Zanimanje(z.getInt("Id"), z.getString("Naziv")));
        }
        JSONArray pasosi = json.getJSONArray("Pasosi");
        for (int i = 0; i < pasosi.length(); i++) {
            JSONObject pasos = pasosi.getJSONObject(i);
            biografija.getPasosi().add(new Drzava(pasos.getInt("Id"), pasos.getString("Naziv")));
        }
        JSONArray jezici = json.getJSONArray("Jezici");
        for (int i = 0; i < jezici.length(); i++) {
            JSONObject jezik = jezici.getJSONObject(i);
            biografija.getJezici().add(new Jezik(jezik.getInt("Id"), jezik.getString("Naziv")));
        }
        biografija.setA(json.getBoolean("A"));
        biografija.setB(json.getBoolean("B"));
        biografija.setC(json.getBoolean("C"));
        biografija.setD(json.getBoolean("D"));
        biografija.setE(json.getBoolean("E"));
        biografija.setF(json.getBoolean("F"));
        biografija.setM(json.getBoolean("M"));
        biografija.setOsudjivan(json.getString("Osudjivan"));
        biografija.setZdravstveniProblemi(json.getString("ZdravstveniProblemi"));
        biografija.setOstaleNapomene(json.getString("OstaleNapomene"));
        biografija.setStatus(json.getInt("Status"));
        return biografija;
    }

    public int sacuvajBiografiju(Biografija biografija) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(API_URL + "korisnici/SacuvajBiografiju");
        JSONObject json = new JSONObject();
        json.put("Id", biografija.getId());
        json.put("ProfilSlika", biografija.getProfil());
        json.put("FiguraSlika", biografija.getFigura());
        json.put("Ime", biografija.getIme());
        json.put("Prezime", biografija.getPrezime());
        json.put("Jmbg", biografija.getJmbg());
        json.put("DatumRodjenja", biografija.getDatumRodjenja());
        json.put("Pol", biografija.getPol());
        json.put("Visina", biografija.getVisina());
        json.put("Tezina", biografija.getTezina());
        json.put("VelicinaOdece", biografija.getVelicinaOdece());
        json.put("BrojCipela", biografija.getBrojCipela());
        json.put("Pusac", biografija.isPusac());
        json.put("UBraku", biografija.isuBraku());
        json.put("ImaDece", biografija.isImaDece());
        json.put("Drzava", biografija.getDrzava());
        json.put("Mesto", biografija.getMesto());
        json.put("Adresa", biografija.getAdresa());
        json.put("FiksniTelefon", biografija.getFiksniTelefon());
        json.put("MobilniTelefon", biografija.getMobilniTelefon());
        json.put("Email", biografija.getEmail());
        json.put("StrucnaSprema", biografija.getStepenStrucneSpremen());
        json.put("VrstaZanimanja", biografija.getDelatnost());
        json.put("Zanimanje", biografija.getZanimanje());
        json.put("RadNaRacunaru", biografija.getRadNaRacunaru());
        json.put("OstalaZnanja", biografija.getOstalaZnanja());

        JSONArray radnoIskustvo = new JSONArray();
        for (RadnoIskustvo ri : biografija.getRadnoIskustvo()) {
            JSONObject item = new JSONObject();
            item.put("Id", ri.getId());
            item.put("Poslodavac", ri.getPoslodavac());
            item.put("Period", ri.getPeriod());
            item.put("OpisPosla", ri.getOpis());
            radnoIskustvo.put(item);
        }
        json.put("RadnoIskustvo", radnoIskustvo);

        JSONArray zeljenaMestaRada = new JSONArray();
        for (Drzava drzava : biografija.getZeljeneDrzaveRada()) {
            JSONObject item = new JSONObject();
            item.put("Id", drzava.getId());
            item.put("Naziv", drzava.getNaziv());
            zeljenaMestaRada.put(item);
        }
        json.put("ZeljenaMestaRada", zeljenaMestaRada);

        JSONArray prihvatljivPoslovi = new JSONArray();
        for (Zanimanje zanimanje : biografija.getPrihvatljivaZanimanja()) {
            JSONObject item = new JSONObject();
            item.put("Id", zanimanje.getId());
            item.put("Naziv", zanimanje.getNaziv());
            prihvatljivPoslovi.put(item);
        }
        json.put("PrihvatljivaZanimanja", prihvatljivPoslovi);
        JSONArray pasosi = new JSONArray();
        for (Drzava drzava : biografija.getPasosi()) {
            JSONObject item = new JSONObject();
            item.put("Id", drzava.getId());
            item.put("Naziv", drzava.getNaziv());
            pasosi.put(item);
        }
        json.put("Pasosi", pasosi);

        JSONArray jezici = new JSONArray();
        for (Jezik jezik : biografija.getJezici()) {
            JSONObject item = new JSONObject();
            item.put("Id", jezik.getId());
            item.put("Naziv", jezik.getNaziv());
            jezici.put(item);
        }
        json.put("Jezici", jezici);

        json.put("A", biografija.isA());
        json.put("B", biografija.isB());
        json.put("C", biografija.isC());
        json.put("D", biografija.isD());
        json.put("E", biografija.isE());
        json.put("F", biografija.isF());
        json.put("M", biografija.isM());
        json.put("Osudjivan", biografija.getOsudjivan());
        json.put("ZdravstveniProblemi", biografija.getZdravstveniProblemi());
        json.put("OstaleNapomene", biografija.getOstaleNapomene());
        json.put("Status", biografija.getStatus());

        String jsonString = json.toString();
        StringEntity entity = new StringEntity(jsonString, "utf-8");
        request.setEntity(entity);
        request.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();
        if (status == 201) {
            HttpEntity result = response.getEntity();
            InputStream content = result.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            line = builder.toString();
            return Integer.parseInt(line);
        }
        throw new Exception("Resource is not created");

    }

    public List<Delatnost> vratiDelatnosti() throws IOException, JSONException {
        String response = httpGet(API_URL + "sifarnici/VratiVrsteZanimanja");
        JSONArray json = new JSONArray(response);
        List<Delatnost> delatnosti = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = json.getJSONObject(i);
            Delatnost d = new Delatnost(jsonObject.getInt("Id"), jsonObject.getString("Naziv"));
            JSONArray zanimanjaJson = jsonObject.getJSONArray("Zanimanja");
            for (int j = 0; j < zanimanjaJson.length(); j++) {
                JSONObject zanimanjeJson = zanimanjaJson.getJSONObject(j);
                d.getZanimanja().add(new Zanimanje(zanimanjeJson.getInt("Id"),
                        zanimanjeJson.getString("Naziv")));
            }
            delatnosti.add(d);
        }

        return delatnosti;
    }

    public List<Jezik> vratiJezike() throws IOException, JSONException {
        String response = httpGet(API_URL + "sifarnici/VratiJezike");
        JSONArray json = new JSONArray(response);
        List<Jezik> jezici = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jezik = json.getJSONObject(i);
            jezici.add(new Jezik(jezik.getInt("Id"), jezik.getString("Naziv")));
        }
        return jezici;
    }

    public List<StepenStrucneSpreme> vratiStrucneSpreme() throws IOException, JSONException {
        String response = httpGet(API_URL + "sifarnici/VratiStrucneSpreme");
        JSONArray json = new JSONArray(response);
        List<StepenStrucneSpreme> sss = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObj = json.getJSONObject(i);
            sss.add(new StepenStrucneSpreme(jsonObj.getInt("Id"), jsonObj.getString("Naziv")));
        }
        return sss;
    }

    public Stats vratiStatistiku() throws IOException, JSONException {
        String response = httpGet(API_URL + "poslovi/stats");
        JSONObject json = new JSONObject(response);
        return new Stats(json.getInt("UkupanBrojPremijumPoslova"), json.getInt("UkupanBrojHotPoslova"),
                json.getInt("UkupanBrojStandardnihPoslova"));
    }

    public int login(String email, String password) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPut request = new HttpPut(API_URL + "korisnici/login");
        JSONObject auth = new JSONObject();
        auth.put("KorisnickoIme", email);
        auth.put("Lozinka", password);
        StringEntity entity = new StringEntity(auth.toString(), "utf-8");
        request.setEntity(entity);
        request.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");

        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity result = response.getEntity();
            InputStream content = result.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            line = builder.toString();
            auth = new JSONObject(line);
            return auth.getInt("Id");
        }
        throw new Exception("User not logged in");
    }

    public int registrujNalog(String ime, String prezime, String email, String lozinka) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(API_URL + "korisnici/RegistrujNalog");
        JSONObject auth = new JSONObject();
        auth.put("Ime", ime);
        auth.put("Prezime", prezime);
        auth.put("EMail", email);
        auth.put("Lozinka", lozinka);
        StringEntity entity = new StringEntity(auth.toString(), "utf-8");
        request.setEntity(entity);
        request.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");

        HttpResponse response = client.execute(request);
        int status = response.getStatusLine().getStatusCode();
        if (status == 201) {
            HttpEntity result = response.getEntity();
            InputStream content = result.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
            line = builder.toString();
            return Integer.parseInt(line);
        } else if (status == 409) {
            throw new Exception("Uneti e-mail je već registrovan");
        }
        throw new Exception("Registracija naloga nije uspela");
    }

    public String konkurisi(int idKorisnika, int idPosla) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(API_URL + "poslovi/Konkurisi");
        JSONObject json = new JSONObject();
        json.put("IdKorisnika", idKorisnika);
        json.put("IdPosla", idPosla);
        StringEntity entity = new StringEntity(json.toString(), "utf-8");
        request.setEntity(entity);
        request.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");

        HttpResponse response = client.execute(request);
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() == 201) return null;
        return status.getReasonPhrase();
    }

    public List<Posao> vratiMojeKonkurse(int userId) throws Exception {
        String response = httpGet(API_URL + "poslovi/MojiKonkursi/" + userId);
        JSONArray json = new JSONArray(response);
        List<Posao> poslovi = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject item = json.getJSONObject(i);
            poslovi.add(new Posao(item.getInt("Id"),
                    item.getString("Naziv"), item.getString("Poslodavac"), item.getString("Lokacija"),
                    item.getString("Rok"), item.getString("Zanimanje"), null,
                    item.getInt("BrojPrijavljenih")));
        }
        return poslovi;
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
                    json.getString("Image"),
                    json.getInt("BrojPrijavljenih"));
            poslovi.add(posao);
        }
        return poslovi;
    }


    private String httpGet(String url) throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpParams p = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(p, 30000);
        HttpClient client = new DefaultHttpClient(p);
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
