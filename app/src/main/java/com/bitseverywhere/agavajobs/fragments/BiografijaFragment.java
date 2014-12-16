package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitseverywhere.agavajobs.ImageUtils;
import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.models.domain.Delatnost;
import com.bitseverywhere.agavajobs.models.domain.Drzava;
import com.bitseverywhere.agavajobs.models.domain.Mesto;
import com.bitseverywhere.agavajobs.models.domain.StepenStrucneSpreme;
import com.bitseverywhere.agavajobs.models.domain.Zanimanje;
import com.bitseverywhere.agavajobs.services.HttpService;
import com.bitseverywhere.agavajobs.widgets.MultiSelectSpinner;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiografijaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiografijaFragment extends android.support.v4.app.Fragment implements IFragment,
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String ID = "id";
    private static final int REQUEST_PROFILE_PICTURE = 1;
    private static final int REQUEST_FIGURE_PICTURE = 2;
    private static final String[] VELICINE_ODECE = new String[]{"XS", "S", "M", "L", "XL", "XXL", "XXXL"};
    private static final String[] POZNAVANJE_RADA_NA_RACUNARU = new String[]{"Ne koristi", "Početni nivo", "Napredni nivo", "Ekspert"};
    private int id;
    private Biografija biografija = new Biografija();
    private List<Drzava> drzave;
    private List<Mesto> mesta;
    private EditText ime, prezime, brojPasosa, visina, tezina, brojCipela, adresa, fiksniTelefon,
                     mobilniTelefon, email;
    private TextView datumRodjenja, lblDatumRodjenja;
    private RadioButton musko, zensko;
    private Spinner velicinaOdece, drzava, mesto, radNaRacunaru;
    private ProgressBar progressBar;
    private ImageView profil, figura;
    private CheckBox pusac, uBraku, imaDece;
    private Spinner strucnaSprema, delatnost, zanimanje;
    private MultiSelectSpinner zeljenaMestaRada;
    private List<Delatnost> delatnostiList;
    private List<StepenStrucneSpreme> stepenStrucneSpremeList;
    private List<Zanimanje> zanimanjaList;


    public static BiografijaFragment newInstance(int id) {
        BiografijaFragment fragment = new BiografijaFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public BiografijaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID);
        }
        refresh();
        new UcitajDrzaveTask().execute();
        new UcitajStrunceSpremeTask().execute();
        new UcitajDelatnostiTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biografija, container, false);
        ime = (EditText)view.findViewById(R.id.ime);
        prezime = (EditText)view.findViewById(R.id.prezime);
        brojPasosa = (EditText)view.findViewById(R.id.jmbg);
        visina = (EditText)view.findViewById(R.id.visina);
        tezina = (EditText)view.findViewById(R.id.tezina);
        brojCipela = (EditText)view.findViewById(R.id.brojCipela);
        datumRodjenja = (TextView)view.findViewById(R.id.datumRodjenja);
        datumRodjenja.setOnClickListener(this);
        musko = (RadioButton)view.findViewById(R.id.musko);
        zensko = (RadioButton)view.findViewById(R.id.zensko);
        velicinaOdece = (Spinner)view.findViewById(R.id.velicinaOdece);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner,
                VELICINE_ODECE);
        velicinaOdece.setOnItemSelectedListener(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        velicinaOdece.setAdapter(adapter);
        drzava = (Spinner)view.findViewById(R.id.drzava);
        drzava.setOnItemSelectedListener(this);
        mesto = (Spinner)view.findViewById(R.id.mesto);
        mesto.setOnItemSelectedListener(this);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        profil = (ImageView)view.findViewById(R.id.profil);
        profil.setOnClickListener(this);
        figura = (ImageView)view.findViewById(R.id.figura);
        figura.setOnClickListener(this);
        adresa = (EditText)view.findViewById(R.id.adresa);
        fiksniTelefon = (EditText)view.findViewById(R.id.fiksniTelefon);
        mobilniTelefon = (EditText)view.findViewById(R.id.mobilniTelefon);
        email = (EditText)view.findViewById(R.id.email);
        lblDatumRodjenja = ((TextView)view.findViewById(R.id.lblDatumRodjenja));
        lblDatumRodjenja.setOnClickListener(this);
        pusac = (CheckBox)view.findViewById(R.id.pusac);
        uBraku = (CheckBox)view.findViewById(R.id.uBraku);
        imaDece = (CheckBox)view.findViewById(R.id.imaDece);
        radNaRacunaru = (Spinner)view.findViewById(R.id.radNaRacunaru);
        adapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner, POZNAVANJE_RADA_NA_RACUNARU);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radNaRacunaru.setAdapter(adapter);
        strucnaSprema = (Spinner)view.findViewById(R.id.stepenSrtucneSpreme);
        delatnost = (Spinner)view.findViewById(R.id.delatnost);
        delatnost.setOnItemSelectedListener(this);
        zanimanje = (Spinner)view.findViewById(R.id.zanimanje);
        zeljenaMestaRada = (MultiSelectSpinner)view.findViewById(R.id.zeljenaMestaRada);
        zeljenaMestaRada.setTemplate(R.layout.simple_spinner);

        return view;
    }


    @Override
    public void refresh() {
        if (id != 0) {
            new UcitajBiografijuTask().execute(id);
        }
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setDrzave(List<Drzava> drzave) {
        this.drzave = drzave;
        if (getActivity() != null) {
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner, drzave);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            drzava.setAdapter(adapter);
            postaviDrzavu();

            List<String> naziviDrzava = new ArrayList<>();
            for (Drzava d : drzave) {
                naziviDrzava.add(d.getNaziv());
            }
            zeljenaMestaRada.setItems(naziviDrzava);
        }
    }

    private void setStrucneSpreme(List<StepenStrucneSpreme> strucneSpreme) {
        stepenStrucneSpremeList = strucneSpreme;
        if (getActivity() != null) {
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner, stepenStrucneSpremeList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            strucnaSprema.setAdapter(adapter);
            postaviStrucnuSpremu();
        }
    }

    private void setDelatnosti(List<Delatnost> delatnosti) {
        delatnostiList = delatnosti;
        if (getActivity() != null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner, delatnosti);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            delatnost.setAdapter(adapter);
            postaviDelatnost();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == drzava) {
            Drzava izabranaDrzava = drzave.get(position);
            mesta = izabranaDrzava.getMesta();
            biografija.setDrzava(izabranaDrzava.getId());
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner,
                    mesta);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mesto.setAdapter(adapter);
            postaviMesto();
        } else if (parent == mesto) {
            Mesto izabranoMesto = mesta.get(position);
            biografija.setMesto(izabranoMesto.getId());
        } else if (parent == velicinaOdece) {
            biografija.setVelicinaOdece(position);
        } else if (parent == delatnost) {
            zanimanjaList = delatnostiList.get(position).getZanimanja();
            ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.simple_spinner, zanimanjaList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zanimanje.setAdapter(adapter);
            postaviZanimanje();
        }
    }

    private void setBiografija(Biografija biografija) {
        if (biografija != null) {
            this.biografija = biografija;
            this.id = biografija.getId();

            ime.setText(biografija.getIme());
            prezime.setText(biografija.getPrezime());
            datumRodjenja.setText(biografija.getDatumRodjenja());
            brojPasosa.setText(biografija.getJmbg());
            if (biografija.isMusko()) {
                musko.setChecked(true);
            }
            if (biografija.isZensko()) {
                zensko.setChecked(true);
            }
            if (biografija.getProfil() != null) {
                profil.setImageBitmap(ImageUtils.getBitmapFromStringBase64(biografija.getProfil()));
            } else {
                profil.setImageDrawable(getResources().getDrawable(R.drawable.head));
            }
            if (biografija.getFigura() != null) {
                figura.setImageBitmap(ImageUtils.getBitmapFromStringBase64(biografija.getFigura()));
            } else {
                figura.setImageDrawable(getResources().getDrawable(R.drawable.siluete));
            }
           /* visina.setText(biografija.getVisina());
            tezina.setText(biografija.getTezina());
            brojCipela.setText(biografija.getBrojCipela());*/
            velicinaOdece.setSelection(biografija.getVelicinaOdece());
            adresa.setText(biografija.getAdresa());
            fiksniTelefon.setText(biografija.getFiksniTelefon());
            mobilniTelefon.setText(biografija.getMobilniTelefon());
            email.setText(biografija.getEmail());
            pusac.setChecked(biografija.isPusac());
            uBraku.setChecked(biografija.isuBraku());
            imaDece.setChecked(biografija.isImaDece());
            radNaRacunaru.setSelection(biografija.getRadNaRacunaru());
            postaviDrzavu();
            postaviMesto();
            postaviStrucnuSpremu();
            postaviDelatnost();
            postaviZanimanje();

        }

    }

    private void postaviDrzavu() {
        if (this.biografija != null && drzave != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < drzave.size(); i++) {
                Drzava d = drzave.get(i);
                if (d.getId() == biografija.getDrzava()) {
                    drzava.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviMesto() {
        if (this.biografija != null && mesta != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < mesta.size(); i++) {
                Mesto mesto = mesta.get(i);
                if (mesto.getId() == biografija.getMesto()) {
                    this.mesto.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviStrucnuSpremu() {
        if (this.biografija != null && stepenStrucneSpremeList != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < stepenStrucneSpremeList.size(); i++) {
                if (stepenStrucneSpremeList.get(i).getId() == biografija.getStepenStrucneSpremen()) {
                    strucnaSprema.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviDelatnost() {
        if (this.biografija != null && delatnostiList != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < delatnostiList.size(); i++) {
                if (delatnostiList.get(i).getId() == biografija.getDelatnost()) {
                    delatnost.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviZanimanje() {
        if (this.biografija != null && zanimanjaList != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < zanimanjaList.size(); i++) {
                if (zanimanjaList.get(i).getId() == biografija.getZanimanje()) {
                    zanimanje.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (v == profil) {
            startActivityForResult(intent, REQUEST_PROFILE_PICTURE);
        } else if (v == figura) {
            startActivityForResult(intent, REQUEST_FIGURE_PICTURE);
        } else if (v == datumRodjenja || v == lblDatumRodjenja) {
            final String dateFormat = "dd.MM.yyyy";
            Date date;
            try {
                date = new SimpleDateFormat(dateFormat).parse(biografija.getDatumRodjenja());
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(year, monthOfYear, dayOfMonth);
                            String datum = new SimpleDateFormat(dateFormat).format(calendar.getTime());
                            setDatumRodjenja(datum);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PROFILE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profil.setImageBitmap(imageBitmap);
            biografija.setProfil(ImageUtils.getStringBase64FromBitmap(imageBitmap));
        }
        if (requestCode == REQUEST_FIGURE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            biografija.setFigura(ImageUtils.getStringBase64FromBitmap(imageBitmap));
            figura.setImageBitmap(imageBitmap);
        }
    }

    public void setDatumRodjenja(String noviDatumRodjenja) {
        biografija.setDatumRodjenja(noviDatumRodjenja);
        datumRodjenja.setText(noviDatumRodjenja);
    }

    private class UcitajDrzaveTask extends AsyncTask<Void, Void, List<Drzava>> {

        @Override
        protected List<Drzava> doInBackground(Void... params) {
            try {
                return HttpService.getInstance().vratiDrzave();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Drzava> drzave) {
            BiografijaFragment.this.setDrzave(drzave);
        }
    }
    private class UcitajBiografijuTask extends AsyncTask<Integer, Void, Biografija> {

        @Override
        protected void onPreExecute() {
            if (BiografijaFragment.this.progressBar != null) {
                BiografijaFragment.this.progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Biografija doInBackground(Integer... params) {
            try {
                return HttpService.getInstance().vratiBiografiju(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Biografija biografija) {
            BiografijaFragment.this.setBiografija(biografija);
            BiografijaFragment.this.progressBar.setVisibility(View.GONE);
        }
    }
    private class UcitajStrunceSpremeTask extends AsyncTask<Void, Void, List<StepenStrucneSpreme>> {

        @Override
        protected List<StepenStrucneSpreme> doInBackground(Void... params) {
            try {
                return HttpService.getInstance().vratiStrucneSpreme();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
        @Override
        protected void onPostExecute(List<StepenStrucneSpreme> stepenStrucneSpreme) {
            BiografijaFragment.this.setStrucneSpreme(stepenStrucneSpreme);
        }

    }
    private class UcitajDelatnostiTask extends AsyncTask<Void, Void, List<Delatnost>> {

        @Override
        protected List<Delatnost> doInBackground(Void... params) {
            try {
                return HttpService.getInstance().vratiDelatnosti();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
        @Override
        protected void onPostExecute(List<Delatnost> delatnosti) {
            BiografijaFragment.this.setDelatnosti(delatnosti);
        }

    }

}
