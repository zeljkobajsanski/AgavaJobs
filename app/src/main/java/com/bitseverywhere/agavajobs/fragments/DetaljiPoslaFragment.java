package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitseverywhere.agavajobs.ImageUtils;
import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.activities.IMainActivity;
import com.bitseverywhere.agavajobs.adapters.GalleryAdapter;
import com.bitseverywhere.agavajobs.models.domain.DetaljiPosla;
import com.bitseverywhere.agavajobs.services.HttpService;

import org.json.JSONException;

import java.io.IOException;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link DetaljiPoslaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetaljiPoslaFragment extends android.support.v4.app.Fragment implements IFragment {

    private static final String ID_POSLA = "id";
    private static final String ID_KORISNIKA = "idKorisnika";

    private int id;
    private int idKorisnika;

    private ImageView logo;
    private ProgressBar progressBar;
    private WebView webView;
    private TextView nazivOglasa;
    private TextView poslodavac, nazivPoslodavca;
    private TextView sifraOglasa, zanimanje, lokacija, brojIzvrsilaca, obrazovanje,
                    rok1;
    private Gallery gallery;
    private IMainActivity mainActivity;

    public static DetaljiPoslaFragment newInstance(int id, int idKorisnika) {
        DetaljiPoslaFragment fragment = new DetaljiPoslaFragment();
        Bundle args = new Bundle();
        args.putInt(ID_POSLA, id);
        args.putInt(ID_KORISNIKA, idKorisnika);
        fragment.setArguments(args);
        return fragment;
    }

    public DetaljiPoslaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID_POSLA);
            idKorisnika = getArguments().getInt(ID_KORISNIKA);
            refresh();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalji_posla, container, false);
        logo = (ImageView)view.findViewById(R.id.logo);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        webView = (WebView)view.findViewById(R.id.webView);
        nazivOglasa = (TextView)view.findViewById(R.id.nazivOglasa);
        poslodavac = (TextView)view.findViewById(R.id.poslodavac);
        sifraOglasa = (TextView)view.findViewById(R.id.sifraOglasa);
        zanimanje = (TextView)view.findViewById(R.id.zanimanje);
        lokacija = (TextView)view.findViewById(R.id.lokacija);
        brojIzvrsilaca = (TextView)view.findViewById(R.id.brojIzvrsilaca);
        obrazovanje = (TextView)view.findViewById(R.id.obrazovanje);
        rok1 = (TextView)view.findViewById(R.id.rok1);
        gallery = (Gallery)view.findViewById(R.id.gallery);
        nazivPoslodavca = (TextView)view.findViewById(R.id.nazivPoslodavca);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detalji_posla, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_ok:
                konkurisi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (IMainActivity)activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainActivity.setActionBarTitle(R.string.title_detalji);
    }

    private void konkurisi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Agava Jobs");
        builder.setMessage("Da li želite da konkurišete za izabrani posao?");
        builder.setPositiveButton("Da, želim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new KonkurisiTask(DetaljiPoslaFragment.this.idKorisnika, DetaljiPoslaFragment.this.id).execute();
            }
        });
        builder.setNegativeButton("Ne želim", null);
        builder.show();

    }

    @Override
    public void refresh() {
        new UcitajDetaljePoslaTask().execute(id);
    }

    private void bind(DetaljiPosla detaljiPosla) {
        if (detaljiPosla != null) {
            if (detaljiPosla.getLogo() != null) {
                logo.setImageBitmap(ImageUtils.getBitmapFromStringBase64(detaljiPosla.getLogo()));
            }
            nazivOglasa.setText(detaljiPosla.getNaziv());
            nazivPoslodavca.setText(detaljiPosla.getPoslodavac());
            poslodavac.setText(detaljiPosla.getPoslodavac());
            webView.loadData(detaljiPosla.getOpis(), "text/html", "UTF-8");
            sifraOglasa.setText(detaljiPosla.getSifra() != "null" ? detaljiPosla.getSifra() : null);
            zanimanje.setText(detaljiPosla.getZanimanje());
            lokacija.setText(detaljiPosla.getLokacija());
            brojIzvrsilaca.setText(detaljiPosla.getBrojIzvrsilaca());
            obrazovanje.setText(detaljiPosla.getObrazovanje());
            rok1.setText(detaljiPosla.getRok());
            if (getActivity() != null) {
                gallery.setAdapter(new GalleryAdapter(getActivity(), detaljiPosla.getSlike()));
            }
        }
    }

    private void uspesnoKonkurisano(String message) {
        if (DetaljiPoslaFragment.this.progressBar != null) {
            DetaljiPoslaFragment.this.progressBar.setVisibility(View.GONE);
        }
        if (message == null) {
            Toast.makeText(getActivity(), "Uspešno ste konkurisali za izabrani posao",
                    Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Agava Jobs");
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);
            builder.show();
        }
    }

    private class UcitajDetaljePoslaTask extends AsyncTask<Integer, Void, DetaljiPosla> {

        @Override
        protected void onPreExecute() {
            if (DetaljiPoslaFragment.this.progressBar != null) {
                DetaljiPoslaFragment.this.progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected DetaljiPosla doInBackground(Integer... params) {
            HttpService http = HttpService.getInstance();
            try {
                return http.vratiDetaljePosla(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(DetaljiPosla detaljiPosla) {
            DetaljiPoslaFragment.this.bind(detaljiPosla);
            if (DetaljiPoslaFragment.this.progressBar != null) {
                DetaljiPoslaFragment.this.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class KonkurisiTask extends AsyncTask<Void, Void, String> {

        private boolean ok = true;
        private int idKorisnika, idPosla;

        private KonkurisiTask(int idKorisnika, int idPosla) {
            this.idKorisnika = idKorisnika;
            this.idPosla = idPosla;
        }

        @Override
        protected void onPreExecute() {
            if (DetaljiPoslaFragment.this.progressBar != null) {
                DetaljiPoslaFragment.this.progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return HttpService.getInstance().konkurisi(idKorisnika, idPosla);
            } catch (Exception e) {
                ok = false;
                e.printStackTrace();
            }
            return "Greška prilikom konkurisanja";
        }

        @Override
        protected void onPostExecute(String message) {
            DetaljiPoslaFragment.this.uspesnoKonkurisano(message);
        }
    }


}
