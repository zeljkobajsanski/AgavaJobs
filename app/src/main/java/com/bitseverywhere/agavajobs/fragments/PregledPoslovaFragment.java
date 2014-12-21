package com.bitseverywhere.agavajobs.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.activities.IMainActivity;
import com.bitseverywhere.agavajobs.adapters.PosloviAdapter;
import com.bitseverywhere.agavajobs.models.domain.Posao;
import com.bitseverywhere.agavajobs.services.HttpService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class PregledPoslovaFragment extends android.support.v4.app.ListFragment implements IFragment {

    public static final int PREMIJUM_POSLOVI = 0;
    public static final int HOT_POSLOVI = 1;
    public static final int STANDARDNI_POSLOVI = 2;

    private static final String TIP_POSLA = "tip";

    private int mTipPosla;

    private IMainActivity mListener;
    private ProgressBar progressBar;
    private PosloviAdapter posloviAdapter;
    private MenuItem refreshBtn;
    private View noConnection;

    public static PregledPoslovaFragment newInstance(int tipPosla) {
        PregledPoslovaFragment fragment = new PregledPoslovaFragment();
        Bundle args = new Bundle();
        args.putInt(TIP_POSLA, tipPosla);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PregledPoslovaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mTipPosla = getArguments().getInt(TIP_POSLA);
        }
        posloviAdapter = new PosloviAdapter(getActivity());
        setListAdapter(posloviAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (mTipPosla) {
            case PREMIJUM_POSLOVI:
                mListener.setActionBarTitle(R.string.title_premijum);
                break;
            case HOT_POSLOVI:
                mListener.setActionBarTitle(R.string.title_hot);
                break;
            case STANDARDNI_POSLOVI:
                mListener.setActionBarTitle(R.string.title_standardni);
                break;
        }
        refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poslovi, container, false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IMainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IMainActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            mListener.izabranPosao(posloviAdapter.getItem(position).getID());
        }
    }

    @Override
    public void refresh() {
        new UcitajPosloveTask().execute(mTipPosla);
    }

    private void rebind(List<Posao> poslovi) {
        posloviAdapter.clear();
        posloviAdapter.addAll(poslovi);
    }

    private class UcitajPosloveTask extends AsyncTask<Integer, Void, List<Posao>> {

        private boolean error;

        @Override
        protected void onPreExecute() {
            if (PregledPoslovaFragment.this.progressBar != null && PregledPoslovaFragment.this.refreshBtn == null) {
                PregledPoslovaFragment.this.progressBar.setVisibility(View.VISIBLE);
            }
            if (PregledPoslovaFragment.this.refreshBtn != null) {
                PregledPoslovaFragment.this.refreshBtn.setActionView(R.layout.progress_bar);
            }
            if (PregledPoslovaFragment.this.noConnection != null) {
                PregledPoslovaFragment.this.noConnection.setVisibility(View.GONE);
            }
        }

        @Override
        protected List<Posao> doInBackground(Integer... params) {
            try {
                HttpService httpService = HttpService.getInstance();
                switch (params[0]) {
                    case PREMIJUM_POSLOVI:
                        return httpService.vratiPremijumPoslove();
                    case HOT_POSLOVI:
                        return httpService.vratiHotPoslove();
                    case STANDARDNI_POSLOVI:
                        return httpService.vratiStandardnePoslove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Posao> poslovi) {
            PregledPoslovaFragment.this.rebind(poslovi);
            if (PregledPoslovaFragment.this.progressBar != null) {
                PregledPoslovaFragment.this.progressBar.setVisibility(View.GONE);
            }
            if (PregledPoslovaFragment.this.refreshBtn != null) {
                PregledPoslovaFragment.this.refreshBtn.setActionView(null);
            }
            if (PregledPoslovaFragment.this.noConnection != null && error) {
                PregledPoslovaFragment.this.noConnection.setVisibility(View.VISIBLE);
            }
        }
    }
}
