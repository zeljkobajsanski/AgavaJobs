package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitseverywhere.agavajobs.ImageUtils;
import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.Utils;
import com.bitseverywhere.agavajobs.activities.IMainActivity;
import com.bitseverywhere.agavajobs.adapters.RadnoIskustvoAdapter;
import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.models.domain.Delatnost;
import com.bitseverywhere.agavajobs.models.domain.Drzava;
import com.bitseverywhere.agavajobs.models.domain.Jezik;
import com.bitseverywhere.agavajobs.models.domain.Mesto;
import com.bitseverywhere.agavajobs.models.domain.RadnoIskustvo;
import com.bitseverywhere.agavajobs.models.domain.StepenStrucneSpreme;
import com.bitseverywhere.agavajobs.models.domain.Zanimanje;
import com.bitseverywhere.agavajobs.services.HttpService;

import org.json.JSONException;

import java.io.IOException;
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
    private Biografija biografija;
    private ArrayAdapter<Drzava> drzaveAdapter;
    private ArrayAdapter<Mesto> mestaAdapter;
    private EditText ime, prezime, brojPasosa, adresa, fiksniTelefon,
                     mobilniTelefon, email, ostalaZnanja;
    private EditText visina, tezina, brojCipela;
    private TextView datumRodjenja, lblDatumRodjenja;
    private RadioButton musko, zensko;
    private Spinner velicinaOdece, drzava, mesto, radNaRacunaru;
    private ProgressBar progressBar;
    private ImageView profil, figura;
    private CheckBox pusac, uBraku, imaDece;
    private Spinner strucnaSprema, delatnost, zanimanje;
    private ArrayAdapter<Delatnost> delatnostiAdapter;
    private ArrayAdapter<StepenStrucneSpreme> stepenStrucneSpremeAdapter;
    private ArrayAdapter<Zanimanje> zanimanjaAdapter;
    private ImageButton btnDodajMestoRada, btnObrisiMestoRada, btnDodajRadnoIskustvo, btnObrisiRadnoIskustvo;
    private String[] naziviDrzava;
    private ListView zeljenaMestaRada, radnoIskustvo, prihvatljivaZanimanja, pasosi, jezici;
    private ArrayAdapter<Drzava> zeljenaMestaRadaAdapter;
    private RadnoIskustvoAdapter radnoIskustvoAdapter;
    private ArrayAdapter<Zanimanje> prihvatljivaZanimanjaAdapter;
    private ArrayAdapter<Drzava> pasosiAdapter;
    private ArrayAdapter<Jezik> jeziciAdapter;
    private List<Jezik> sviJezici;
    private CheckBox a, b, c, d, e, f, m;
    private EditText osudjivan, zdravstveniProblemi, ostaleNapomene;
    private RadioButton nezaposlen, zaposlen, student;
    private IMainActivity mainActivity;
    private MenuItem refreshBtn;
    private View noConnection;

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
        setHasOptionsMenu(true);
        if (drzaveAdapter == null) {
            drzaveAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner);
            drzaveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        if (zeljenaMestaRadaAdapter == null) {
            zeljenaMestaRadaAdapter = new ArrayAdapter(getActivity(), R.layout.checked_list_item);
            zeljenaMestaRadaAdapter.setNotifyOnChange(true);
        }
        if (stepenStrucneSpremeAdapter == null) {
            stepenStrucneSpremeAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner);
            stepenStrucneSpremeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        if (mestaAdapter == null) {
            mestaAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner);
            mestaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        if (delatnostiAdapter == null) {
            delatnostiAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner);
            delatnostiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        if (zanimanjaAdapter == null) {
            zanimanjaAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner);
            zanimanjaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        if (radnoIskustvoAdapter == null) {
            radnoIskustvoAdapter = new RadnoIskustvoAdapter(getActivity());
        }
        if (prihvatljivaZanimanjaAdapter == null) {
            prihvatljivaZanimanjaAdapter = new ArrayAdapter<Zanimanje>(getActivity(), R.layout.checked_list_item);
        }
        if (pasosiAdapter == null) {
            pasosiAdapter = new ArrayAdapter<Drzava>(getActivity(), R.layout.checked_list_item);
        }
        if (jeziciAdapter == null) {
            jeziciAdapter = new ArrayAdapter<Jezik>(getActivity(), R.layout.checked_list_item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biografija, container, false);
        ime = (EditText)view.findViewById(R.id.ime);
        prezime = (EditText)view.findViewById(R.id.prezime);
        brojPasosa = (EditText)view.findViewById(R.id.jmbg);
        this.visina = (EditText)view.findViewById(R.id.visina);
        this.tezina = (EditText)view.findViewById(R.id.tezina);
        this.brojCipela = (EditText)view.findViewById(R.id.brojCipela);
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
        ostalaZnanja = (EditText)view.findViewById(R.id.ostalaZnanja);

        drzava = (Spinner)view.findViewById(R.id.drzava);
        drzava.setOnItemSelectedListener(this);
        drzava.setAdapter(drzaveAdapter);


        mesto = (Spinner)view.findViewById(R.id.mesto);
        mesto.setOnItemSelectedListener(this);
        mesto.setAdapter(mestaAdapter);

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
        strucnaSprema.setAdapter(stepenStrucneSpremeAdapter);

        delatnost = (Spinner)view.findViewById(R.id.delatnost);
        delatnost.setOnItemSelectedListener(this);
        delatnost.setAdapter(delatnostiAdapter);

        zanimanje = (Spinner)view.findViewById(R.id.zanimanje);
        zanimanje.setAdapter(zanimanjaAdapter);

        btnDodajMestoRada = (ImageButton)view.findViewById(R.id.btnDodajMestoRada);
        btnDodajMestoRada.setOnClickListener(this);
        btnObrisiMestoRada = (ImageButton)view.findViewById(R.id.btnObrisiMestaRada);
        btnObrisiMestoRada.setOnClickListener(this);
        zeljenaMestaRada = (ListView)view.findViewById(R.id.zeljenaMestaRada);
        zeljenaMestaRada.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        zeljenaMestaRada.setAdapter(zeljenaMestaRadaAdapter);
        Utils.setListViewHeightBasedOnChildren(zeljenaMestaRada);
        radnoIskustvo = (ListView)view.findViewById(R.id.radnoIskustvo);
        radnoIskustvo.setAdapter(radnoIskustvoAdapter);
        radnoIskustvo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadnoIskustvo ri = radnoIskustvoAdapter.getItem(position);
                RadnoIskustvoFragment dialog = RadnoIskustvoFragment.newInstance(ri.getId(), ri.getPoslodavac(),
                        ri.getPeriod(), ri.getOpis());
                dialog.setEdit(ri, true);
                dialog.show(getFragmentManager(), null);
            }
        });

        btnDodajRadnoIskustvo = (ImageButton)view.findViewById(R.id.btnDodajIskustvo);
        btnDodajRadnoIskustvo.setOnClickListener(this);
        btnObrisiRadnoIskustvo = (ImageButton)view.findViewById(R.id.btnObrisiIskustvo);
        btnObrisiRadnoIskustvo.setOnClickListener(this);

        prihvatljivaZanimanja = (ListView)view.findViewById(R.id.prihvatljivaZanimanja);
        prihvatljivaZanimanja.setAdapter(prihvatljivaZanimanjaAdapter);
        prihvatljivaZanimanja.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        ImageButton btnDodajPrihvatljivoZanimanje = (ImageButton)view.findViewById(R.id.btnDodajPrihvatljivaZanimanja);
        btnDodajPrihvatljivoZanimanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrihvatljivaZanimanjaFragment dialog = PrihvatljivaZanimanjaFragment.newInstance(delatnostiAdapter);
                dialog.show(getFragmentManager(), null);
            }
        });
        ImageButton btnObrisiPrihvatljivoZanimanje = (ImageButton)view.findViewById(R.id.btnObrisiPrihvatljivaZanimanja);
        btnObrisiPrihvatljivoZanimanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray pos = prihvatljivaZanimanja.getCheckedItemPositions();
                for (int i = 0; i < prihvatljivaZanimanja.getCount(); i++) {
                    if (pos.get(i)) {
                        Zanimanje zanimanje = prihvatljivaZanimanjaAdapter.getItem(i);
                        prihvatljivaZanimanjaAdapter.remove(zanimanje);
                        biografija.getPrihvatljivaZanimanja().remove(zanimanje);
                    }
                }
                pos.clear();
                prihvatljivaZanimanjaAdapter.notifyDataSetChanged();
                Utils.setListViewHeightBasedOnChildren(prihvatljivaZanimanja);
            }
        });
        pasosi = (ListView)view.findViewById(R.id.pasosi);
        pasosi.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        pasosi.setAdapter(pasosiAdapter);
        ImageButton btnDodajPasos = (ImageButton)view.findViewById(R.id.btnDodajPasos);
        btnDodajPasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Izaberite državu");
                final boolean[] choosen = new boolean[naziviDrzava.length];
                for (int i = 0; i < drzaveAdapter.getCount(); i++) {
                    for (int j = 0; j < biografija.getPasosi().size(); j++) {
                        if (drzaveAdapter.getItem(i).getId() == biografija.getPasosi().get(j).getId()) {
                            choosen[i] = true;
                        }
                    }
                }
                builder.setMultiChoiceItems(naziviDrzava, choosen, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        choosen[which] = isChecked;
                    }
                });
                builder.setPositiveButton("Prihvati", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < choosen.length; i++) {
                            Drzava drzava = drzaveAdapter.getItem(i);
                            if (choosen[i]) {
                                if (!biografija.getPasosi().contains(drzava)) {
                                    biografija.getPasosi().add(drzava);
                                    pasosiAdapter.add(drzava);
                                }
                            } else {
                                biografija.getPasosi().remove(drzava);
                                pasosiAdapter.remove(drzava);
                            }
                        }
                        Utils.setListViewHeightBasedOnChildren(pasosi);
                    }
                });
                builder.setNegativeButton("Otkaži", null);
                builder.show();
            }
        });
        ImageButton btnObrisiPasos = (ImageButton)view.findViewById(R.id.btnObrisiPasos);
        btnObrisiPasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkBoxes = pasosi.getCheckedItemPositions();
                List<Drzava> brisi = new ArrayList<Drzava>();
                for (int i = 0; i < pasosiAdapter.getCount(); i++) {
                    if (checkBoxes.get(i)) {
                        Drzava pasos = pasosiAdapter.getItem(i);
                        brisi.add(pasos);
                    }
                }
                for (Drzava pasos : brisi) {
                    pasosiAdapter.remove(pasos);
                    biografija.getPasosi().remove(pasos);
                }
                checkBoxes.clear();
                Utils.setListViewHeightBasedOnChildren(pasosi);
            }
        });
        jezici = (ListView)view.findViewById(R.id.jezici);
        jezici.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        jezici.setAdapter(jeziciAdapter);

        ImageButton btnDodajJezik = (ImageButton)view.findViewById(R.id.btnDodajJezik);
        btnDodajJezik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Izaberite jezike");
                final int count = sviJezici.size();
                String[] naziviJezika = new String[count];
                final boolean[] izabraniJezici = new boolean[count];

                for (int i = 0; i < count; i++) {
                    Jezik jezik = sviJezici.get(i);
                    naziviJezika[i] = jezik.getNaziv();
                    izabraniJezici[i] = false;
                    for (int j = 0; j < biografija.getJezici().size(); j++) {
                        if (biografija.getJezici().get(j).equals(jezik)) {
                            izabraniJezici[i] = true;
                            break;
                        }
                    }
                }
                builder.setMultiChoiceItems(naziviJezika, izabraniJezici, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                          izabraniJezici[which] = isChecked;
                    }
                });
                builder.setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < count; i++) {
                            Jezik jezik = sviJezici.get(i);
                            boolean izabran = izabraniJezici[i];
                            if (izabran) {
                                if (!biografija.getJezici().contains(jezik)) {
                                    biografija.getJezici().add(jezik);
                                    jeziciAdapter.add(jezik);
                                }
                            } else {
                                jeziciAdapter.remove(jezik);
                                biografija.getJezici().remove(jezik);
                            }
                        }
                        Utils.setListViewHeightBasedOnChildren(jezici);
                    }
                });
                builder.setNegativeButton("Otkaži", null);
                builder.show();
            }
        });
        ImageButton btnObrisiJezik = (ImageButton)view.findViewById(R.id.btnObrisiJezik);
        btnObrisiJezik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkboxes = jezici.getCheckedItemPositions();
                int count = jeziciAdapter.getCount();
                List<Jezik> brisi = new ArrayList<Jezik>();
                for (int i = 0; i < count; i++) {
                    if (checkboxes.get(i)) {
                        Jezik jezik = jeziciAdapter.getItem(i);
                        brisi.add(jezik);
                    }
                }
                for (Jezik jezik : brisi) {
                    biografija.getJezici().remove(jezik);
                    jeziciAdapter.remove(jezik);
                }
                checkboxes.clear();
                Utils.setListViewHeightBasedOnChildren(jezici);
            }
        });
        a = (CheckBox)view.findViewById(R.id.A);
        b = (CheckBox)view.findViewById(R.id.B);
        c = (CheckBox)view.findViewById(R.id.C);
        d = (CheckBox)view.findViewById(R.id.D);
        e = (CheckBox)view.findViewById(R.id.E);
        f = (CheckBox)view.findViewById(R.id.F);
        m = (CheckBox)view.findViewById(R.id.M);
        osudjivan = (EditText)view.findViewById(R.id.osudjivan);
        zdravstveniProblemi = (EditText)view.findViewById(R.id.zdravstveniProblemi);
        ostaleNapomene = (EditText)view.findViewById(R.id.ostaleNapomene);
        nezaposlen = (RadioButton)view.findViewById(R.id.nezaposlen);
        zaposlen = (RadioButton)view.findViewById(R.id.zaposlen);
        student = (RadioButton)view.findViewById(R.id.student);

        noConnection = view.findViewById(R.id.noConnection);
        noConnection.setVisibility(View.GONE);
        noConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainActivity.setActionBarTitle(R.string.title_biografija);
        if (biografija == null) {
            refresh();
        } else {
            setBiografija(biografija);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (IMainActivity)activity;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.biografija, menu);
        refreshBtn = menu.getItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                sacuvaj();
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sacuvaj() {
        String bIme = ime.getText().toString();
        if (TextUtils.isEmpty(bIme)) {
            ime.setError("Unesite ime");
            ime.requestFocus();
            return;
        }
        String bPrezime = prezime.getText().toString();
        if (TextUtils.isEmpty(bPrezime)) {
            prezime.setError("Unesite prezime");
            prezime.requestFocus();
            return;
        }
        String bMobilni = mobilniTelefon.getText().toString();
        String bFiksni = fiksniTelefon.getText().toString();
        if (TextUtils.isEmpty(bMobilni) && TextUtils.isEmpty(bFiksni)) {
            mobilniTelefon.setError("Unesite mobilni ili fiksni telefon");
            fiksniTelefon.setError("Unesite mobilni ili fiksni telefon");
            fiksniTelefon.requestFocus();
            return;
        }
        mobilniTelefon.setError(null);
        fiksniTelefon.setError(null);

        String bEmail = email.getText().toString();
        if (TextUtils.isEmpty(bEmail)) {
            email.setError("Unesite e-mail");
            email.requestFocus();
            return;
        }

        biografija.setIme(bIme);
        biografija.setPrezime(bPrezime);
        biografija.setJmbg(brojPasosa.getText().toString());
        biografija.setDatumRodjenja(datumRodjenja.getText().toString());
        if (musko.isChecked()) {
            biografija.setMusko();
        }
        if (zensko.isChecked()) {
            biografija.setZensko();
        }


        biografija.setVisina(Integer.parseInt(visina.getText().toString()));
        biografija.setTezina(Integer.parseInt(tezina.getText().toString()));
        biografija.setBrojCipela(Integer.parseInt(brojCipela.getText().toString()));
        biografija.setVelicinaOdece(velicinaOdece.getSelectedItemPosition());
        biografija.setPusac(pusac.isChecked());
        biografija.setuBraku(uBraku.isChecked());
        biografija.setImaDece(imaDece.isChecked());
        biografija.setDrzava(((Drzava) drzava.getSelectedItem()).getId());
        biografija.setMesto(((Mesto) mesto.getSelectedItem()).getId());
        biografija.setAdresa(adresa.getText().toString());
        biografija.setFiksniTelefon(bFiksni);
        biografija.setMobilniTelefon(bMobilni);
        biografija.setEmail(bEmail);
        biografija.setStepenStrucneSpremen(((StepenStrucneSpreme) strucnaSprema.getSelectedItem()).getId());
        biografija.setDelatnost(((Delatnost) delatnost.getSelectedItem()).getId());
        biografija.setZanimanje(((Zanimanje) zanimanje.getSelectedItem()).getId());
        biografija.setRadNaRacunaru(radNaRacunaru.getSelectedItemPosition());
        biografija.setOstalaZnanja(ostalaZnanja.getText().toString());
        biografija.setA(a.isChecked());
        biografija.setB(b.isChecked());
        biografija.setC(c.isChecked());
        biografija.setD(d.isChecked());
        biografija.setE(e.isChecked());
        biografija.setF(f.isChecked());
        biografija.setM(m.isChecked());
        biografija.setOsudjivan(osudjivan.getText().toString());
        biografija.setZdravstveniProblemi(zdravstveniProblemi.getText().toString());
        biografija.setOstaleNapomene(ostaleNapomene.getText().toString());
        if (nezaposlen.isChecked()) {
            biografija.setNezaposlen();
        }
        if (zaposlen.isChecked()) {
            biografija.setZaposlen();
        }
        if (student.isChecked()) {
            biografija.setStudent();
        }

        new SacuvajBiografijuTask().execute(biografija);
    }

    @Override
    public void refresh() {
        new UcitajPodatkeTask().execute(id);
    }

    private void setDrzave(List<Drzava> drzave) {
        if (drzaveAdapter != null) {
            drzaveAdapter.clear();
            drzaveAdapter.addAll(drzave);
            naziviDrzava = new String[drzave.size()];
            for (int i = 0; i < naziviDrzava.length; i++) {
                naziviDrzava[i] = drzave.get(i).getNaziv();
            }
            postaviDrzavu();
        }
    }

    private void setStrucneSpreme(List<StepenStrucneSpreme> strucneSpreme) {
        stepenStrucneSpremeAdapter.clear();
        stepenStrucneSpremeAdapter.addAll(strucneSpreme);
        postaviStrucnuSpremu();
    }

    private void setDelatnosti(List<Delatnost> delatnosti) {
        delatnostiAdapter.clear();
        delatnostiAdapter.addAll(delatnosti);
        postaviDelatnost();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (biografija == null) return;
        if (parent == drzava && drzaveAdapter.getCount() > 0) {
            Drzava izabranaDrzava = drzaveAdapter.getItem(position);
            List<Mesto> mesta = izabranaDrzava.getMesta();
            biografija.setDrzava(izabranaDrzava.getId());
            mestaAdapter.clear();
            mestaAdapter.addAll(mesta);
            postaviMesto();
        } else if (parent == mesto && mestaAdapter.getCount() > 0) {
            Mesto izabranoMesto = mestaAdapter.getItem(position);
            biografija.setMesto(izabranoMesto.getId());
        } else if (parent == velicinaOdece) {
            biografija.setVelicinaOdece(position);
        } else if (parent == delatnost && delatnostiAdapter.getCount() > 0) {
            List<Zanimanje> zanimanja = delatnostiAdapter.getItem(position).getZanimanja();
            zanimanjaAdapter.clear();
            zanimanjaAdapter.addAll(zanimanja);
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
                Bitmap img = ImageUtils.getBitmapFromStringBase64(biografija.getProfil());
                if (img != null) {
                    profil.setImageBitmap(img);
                } else {
                    profil.setImageDrawable(getResources().getDrawable(R.drawable.head));
                }
            } else {
                profil.setImageDrawable(getResources().getDrawable(R.drawable.head));
            }
            if (biografija.getFigura() != null) {
                Bitmap img = ImageUtils.getBitmapFromStringBase64(biografija.getFigura());
                if (img != null) {
                    figura.setImageBitmap(img);
                } else {
                    figura.setImageDrawable(getResources().getDrawable(R.drawable.siluete));
                }
            } else {
                figura.setImageDrawable(getResources().getDrawable(R.drawable.siluete));
            }
            this.visina.setText(Integer.toString(biografija.getVisina()));
            this.tezina.setText(Integer.toString(biografija.getTezina()));
            this.brojCipela.setText(Integer.toString(biografija.getBrojCipela()));
            velicinaOdece.setSelection(biografija.getVelicinaOdece());
            adresa.setText(biografija.getAdresa());
            fiksniTelefon.setText(biografija.getFiksniTelefon());
            mobilniTelefon.setText(biografija.getMobilniTelefon());
            email.setText(biografija.getEmail());
            pusac.setChecked(biografija.isPusac());
            uBraku.setChecked(biografija.isuBraku());
            imaDece.setChecked(biografija.isImaDece());
            radNaRacunaru.setSelection(biografija.getRadNaRacunaru());
            ostalaZnanja.setText(biografija.getOstalaZnanja());
            postaviDrzavu();
            postaviMesto();
            postaviStrucnuSpremu();
            postaviDelatnost();
            postaviZanimanje();
            zeljenaMestaRadaAdapter.clear();
            for (Drzava drzava : biografija.getZeljeneDrzaveRada()) {
                zeljenaMestaRadaAdapter.add(drzava);
            }
            zeljenaMestaRadaAdapter.notifyDataSetChanged();
            Utils.setListViewHeightBasedOnChildren(zeljenaMestaRada);
            radnoIskustvoAdapter.clear();
            radnoIskustvoAdapter.addAll(biografija.getRadnoIskustvo());
            Utils.setListViewHeightBasedOnChildren(radnoIskustvo);
            prihvatljivaZanimanjaAdapter.clear();
            prihvatljivaZanimanjaAdapter.addAll(biografija.getPrihvatljivaZanimanja());
            Utils.setListViewHeightBasedOnChildren(prihvatljivaZanimanja);
            pasosiAdapter.clear();
            pasosiAdapter.addAll(biografija.getPasosi());
            Utils.setListViewHeightBasedOnChildren(pasosi);
            jeziciAdapter.clear();
            jeziciAdapter.addAll(biografija.getJezici());
            Utils.setListViewHeightBasedOnChildren(jezici);
            a.setChecked(biografija.isA());
            b.setChecked(biografija.isB());
            c.setChecked(biografija.isC());
            d.setChecked(biografija.isD());
            e.setChecked(biografija.isE());
            f.setChecked(biografija.isF());
            m.setChecked(biografija.isM());
            osudjivan.setText(biografija.getOsudjivan());
            zdravstveniProblemi.setText(biografija.getZdravstveniProblemi());
            ostaleNapomene.setText(biografija.getOstaleNapomene());
            nezaposlen.setChecked(biografija.isNezaposlen());
            zaposlen.setChecked(biografija.isZaposlen());
            student.setChecked(biografija.isStudent());
        }

    }

    private void postaviDrzavu() {
        if (this.biografija != null && drzaveAdapter != null) {
            for (int i = 0; i < drzaveAdapter.getCount(); i++) {
                if (drzaveAdapter.getItem(i).getId() == this.biografija.getDrzava()) {
                    drzava.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviMesto() {
        if (this.biografija != null && mestaAdapter != null) {
            for (int i = 0; i < mestaAdapter.getCount(); i++) {
                if (mestaAdapter.getItem(i).getId() == this.biografija.getMesto()) {
                    this.mesto.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviStrucnuSpremu() {
        if (this.biografija != null && stepenStrucneSpremeAdapter != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < stepenStrucneSpremeAdapter.getCount(); i++) {
                if (stepenStrucneSpremeAdapter.getItem(i).getId() == biografija.getStepenStrucneSpremen()) {
                    strucnaSprema.setSelection(i);
                    break;
                }
            }
        }
    }

    private void postaviDelatnost() {
        if (this.biografija != null && delatnostiAdapter != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < delatnostiAdapter.getCount(); i++) {
                if (biografija.getDelatnost() == delatnostiAdapter.getItem(i).getId()) {
                    delatnost.setSelection(i);
                }
            }
        }
    }

    private void postaviZanimanje() {
        if (this.biografija != null && zanimanjaAdapter != null) {
            Biografija biografija = this.biografija;
            for (int i = 0; i < zanimanjaAdapter.getCount(); i++) {
                if (zanimanjaAdapter.getItem(i).getId() == biografija.getZanimanje()) {
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
            final Calendar calendar = Calendar.getInstance();
            if (biografija != null && biografija.getDatumRodjenja() != null) {
                Date date = null;
                try {
                    date = new SimpleDateFormat(dateFormat).parse(biografija.getDatumRodjenja());
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = new Date();
                }
                calendar.setTime(date);
            }

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
        } else if (v == btnDodajMestoRada && this.naziviDrzava != null) {
            boolean[] checked = new boolean[naziviDrzava.length];
            for (int i = 0; i < drzaveAdapter.getCount(); i++) {
                checked[i] = biografija.sadrziZeljenuDrzavu(drzaveAdapter.getItem(i));
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMultiChoiceItems(naziviDrzava, checked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    Drzava drzava = drzaveAdapter.getItem(which);
                    if (isChecked) {
                        biografija.getZeljeneDrzaveRada().add(drzava);
                        zeljenaMestaRadaAdapter.add(drzava);
                    } else {
                        biografija.getZeljeneDrzaveRada().remove(drzava);
                        zeljenaMestaRadaAdapter.remove(drzava);
                    }
                    zeljenaMestaRadaAdapter.notifyDataSetChanged();
                    Utils.setListViewHeightBasedOnChildren(zeljenaMestaRada);
                }
            });
            builder.setTitle("Željene države");
            builder.show();
        } else if (v == btnObrisiMestoRada) {
            int count = zeljenaMestaRada.getCount();
            SparseBooleanArray viewItems = zeljenaMestaRada.getCheckedItemPositions();
            for (int i = 0; i < count; i++) {
                if (viewItems.get(i)) {
                    TextView item = (TextView)zeljenaMestaRada.getChildAt(i);
                    for (int j = 0; j < drzaveAdapter.getCount(); j++) {
                        Drzava drzava = drzaveAdapter.getItem(j);
                        if (drzava.getNaziv().equals(item.getText())) {
                            zeljenaMestaRadaAdapter.remove(drzava);
                            biografija.getZeljeneDrzaveRada().remove(drzava);
                        }
                    }
                }
            }
            viewItems.clear();
            zeljenaMestaRadaAdapter.notifyDataSetChanged();
            Utils.setListViewHeightBasedOnChildren(zeljenaMestaRada);
        } else if (v == btnDodajRadnoIskustvo) {
            RadnoIskustvoFragment dialog = RadnoIskustvoFragment.newInstance(0, null, null, null);
            dialog.setEdit(new RadnoIskustvo(), false);
            dialog.show(getFragmentManager(), null);
        } else if (v == btnObrisiRadnoIskustvo) {
            List<RadnoIskustvo> selections = radnoIskustvoAdapter.getSelections();
            for (int i = 0; i < selections.size(); i++) {
                RadnoIskustvo ri = selections.get(i);
                radnoIskustvoAdapter.remove(ri);
                biografija.getRadnoIskustvo().remove(ri);
                selections.remove(ri);
            }
            radnoIskustvoAdapter.notifyDataSetChanged();
            Utils.setListViewHeightBasedOnChildren(radnoIskustvo);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PROFILE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (biografija != null) {
                String image = ImageUtils.getStringBase64FromBitmap(imageBitmap);
                if (image != null) {
                    biografija.setProfil(image);
                    profil.setImageBitmap(imageBitmap);
                }
            }
        }
        if (requestCode == REQUEST_FIGURE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (biografija != null) {
                String image = ImageUtils.getStringBase64FromBitmap(imageBitmap);
                if (image != null) {
                    biografija.setFigura(image);
                    figura.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    public void setDatumRodjenja(String noviDatumRodjenja) {
        biografija.setDatumRodjenja(noviDatumRodjenja);
        datumRodjenja.setText(noviDatumRodjenja);
    }

    public void azurirajRadnoIskustvo(RadnoIskustvo radnoIskustvo) {
        radnoIskustvoAdapter.notifyDataSetChanged();
    }

    public void dodajRadnoIskustvo(RadnoIskustvo edit) {
        radnoIskustvoAdapter.add(edit);
        biografija.getRadnoIskustvo().add(edit);
        Utils.setListViewHeightBasedOnChildren(this.radnoIskustvo);
        radnoIskustvoAdapter.notifyDataSetChanged();
    }

    public void dodajPrihvatljivoZanimanje(Zanimanje zanimanje) {
        prihvatljivaZanimanjaAdapter.add(zanimanje);
        biografija.getPrihvatljivaZanimanja().add(zanimanje);
        Utils.setListViewHeightBasedOnChildren(prihvatljivaZanimanja);
    }

    private class UcitajPodatkeTask extends AsyncTask<Integer, Void, Sifarnici> {

        private boolean error;

        @Override
        protected Sifarnici doInBackground(Integer... params) {
            Sifarnici sifarnici = new Sifarnici();
            try {
                sifarnici.setDrzave(HttpService.getInstance().vratiDrzave());
                sifarnici.setDelatnosti(HttpService.getInstance().vratiDelatnosti());
                sifarnici.setStrucneSpreme(HttpService.getInstance().vratiStrucneSpreme());
                sifarnici.setJezici(HttpService.getInstance().vratiJezike());
                sifarnici.biografija = HttpService.getInstance().vratiBiografiju(params[0]);
            } catch (IOException e) {
                error = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sifarnici;
        }

        @Override
        protected void onPreExecute() {
            if (BiografijaFragment.this.progressBar != null && BiografijaFragment.this.refreshBtn == null) {
                BiografijaFragment.this.progressBar.setVisibility(View.VISIBLE);
            }
            if (BiografijaFragment.this.refreshBtn != null) {
                BiografijaFragment.this.refreshBtn.setActionView(R.layout.progress_bar);
            }
            if (BiografijaFragment.this.noConnection != null) {
                BiografijaFragment.this.noConnection.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPostExecute(Sifarnici sifarnici) {
            BiografijaFragment.this.setDrzave(sifarnici.getDrzave());
            BiografijaFragment.this.setStrucneSpreme(sifarnici.getStrucneSpreme());
            BiografijaFragment.this.setDelatnosti(sifarnici.getDelatnosti());
            BiografijaFragment.this.setJezici(sifarnici.getJezici());
            BiografijaFragment.this.setBiografija(sifarnici.biografija);
            if (BiografijaFragment.this.progressBar != null) {
                BiografijaFragment.this.progressBar.setVisibility(View.GONE);
            }
            if (BiografijaFragment.this.refreshBtn != null) {
                BiografijaFragment.this.refreshBtn.setActionView(null);
            }
            if (error) {
                BiografijaFragment.this.noConnection.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setJezici(List<Jezik> jezici) {
        sviJezici = jezici;
    }

    private class SacuvajBiografijuTask extends AsyncTask<Biografija, Void, Biografija> {

        @Override
        protected void onPreExecute() {
            BiografijaFragment.this.progressBar.setVisibility(View.VISIBLE);
            //BiografijaFragment.this.refreshBtn.setActionView(R.layout.progress_bar);
        }

        private Exception exc;
        @Override
        protected Biografija doInBackground(Biografija... params) {
            try {
                HttpService httpService = HttpService.getInstance();
                int id = httpService.sacuvajBiografiju(params[0]);
                return httpService.vratiBiografiju(id);
            } catch (Exception e1) {
                exc = e1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Biografija biografija) {
            if (exc != null) {
                BiografijaFragment.this.showError(exc);
            } else {
                Toast.makeText(getActivity(), "Podaci su uspešno sačuvani",
                        Toast.LENGTH_LONG).show();
                BiografijaFragment.this.setBiografija(biografija);
            }
            BiografijaFragment.this.progressBar.setVisibility(View.GONE);

        }
    }

    private void showError(Exception exc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Greška");
        builder.setMessage("Greška prilikom snimanja podataka");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private class Sifarnici {
        public List<Drzava> drzave = new ArrayList<>();
        public List<StepenStrucneSpreme> strucneSpreme = new ArrayList<>();
        public List<Delatnost> delatnosti = new ArrayList<>();
        public List<Jezik> jezici = new ArrayList<>();
        public Biografija biografija;

        public List<Drzava> getDrzave() {
            return drzave;
        }

        public void setDrzave(List<Drzava> drzave) {
            this.drzave = drzave;
        }

        public List<StepenStrucneSpreme> getStrucneSpreme() {
            return strucneSpreme;
        }

        public void setStrucneSpreme(List<StepenStrucneSpreme> strucneSpreme) {
            this.strucneSpreme = strucneSpreme;
        }

        public List<Delatnost> getDelatnosti() {
            return delatnosti;
        }

        public void setDelatnosti(List<Delatnost> delatnosti) {
            this.delatnosti = delatnosti;
        }

        public List<Jezik> getJezici() {
            return jezici;
        }

        public void setJezici(List<Jezik> jezici) {
            this.jezici = jezici;
        }
    }

}
