package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.activities.IMainActivity;
import com.bitseverywhere.agavajobs.adapters.MojiKonkursiAdapter;
import com.bitseverywhere.agavajobs.models.domain.DetaljiPosla;
import com.bitseverywhere.agavajobs.models.domain.Posao;
import com.bitseverywhere.agavajobs.services.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MojiKonkursiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MojiKonkursiFragment extends ListFragment implements IFragment {

    private static final String ID_KORISNIKA = "idKorisnika";

    private int idKorisnika;
    private MojiKonkursiAdapter adapter;
    private IMainActivity mainActivity;
    private ProgressBar progressBar;
    private ListView listView;
    private MenuItem refreshBtn;
    private View noConnection;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment MojiKonkursiFragment.
     */
    public static MojiKonkursiFragment newInstance(int idKorisnika) {
        MojiKonkursiFragment fragment = new MojiKonkursiFragment();
        Bundle args = new Bundle();
        args.putInt(ID_KORISNIKA, idKorisnika);
        fragment.setArguments(args);
        return fragment;
    }

    public MojiKonkursiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mainActivity.izabranPosao((int)id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            idKorisnika = getArguments().getInt(ID_KORISNIKA);
        }
        adapter = new MojiKonkursiAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moji_konkursi, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        listView = (ListView)view.findViewById(android.R.id.list);
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

    private void showProgress(boolean isBusy) {
        if (refreshBtn != null) {
            if (isBusy) {
                refreshBtn.setActionView(R.layout.progress_bar);
            } else {
                refreshBtn.setActionView(null);
            }
        }
        if (progressBar != null) {
            progressBar.setVisibility(isBusy && refreshBtn == null ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
        mainActivity.setActionBarTitle(R.string.title_moji_konkursi);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (IMainActivity)activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.poslovi, menu);
        refreshBtn = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                break;
        }
        return true;
    }

    @Override
    public void refresh() {
        new UcitajPodatkeTask().execute(idKorisnika);
    }

    private void source(List<Posao> poslovi) {
        adapter.clear();
        adapter.addAll(poslovi);
    }

    private void showConnectionError(boolean show) {
        if (noConnection == null) return;
        if (show) {
            noConnection.setVisibility(View.VISIBLE);
        } else {
            noConnection.setVisibility(View.GONE);
        }
    }

    private class UcitajPodatkeTask extends AsyncTask<Integer, Void, List<Posao>> {

        private boolean error;

        @Override
        protected void onPreExecute() {
            MojiKonkursiFragment.this.showProgress(true);
            MojiKonkursiFragment.this.showConnectionError(false);
        }

        @Override
        protected List<Posao> doInBackground(Integer... params) {
            try {
                return HttpService.getInstance().vratiMojeKonkurse(params[0]);
            } catch (Exception e) {
                error = true;
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Posao> poslovi) {
            MojiKonkursiFragment.this.source(poslovi);
            MojiKonkursiFragment.this.showProgress(false);
            if (error) {
                MojiKonkursiFragment.this.showConnectionError(true);
            }
        }
    }




}
