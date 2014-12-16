package com.bitseverywhere.agavajobs.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TIP_POSLA = "tip";

    private int mTipPosla;

    private IMainActivity mListener;

    private List<Posao> mPoslovi = new ArrayList<>();

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

        if (getArguments() != null) {
            mTipPosla = getArguments().getInt(TIP_POSLA);
        }
        refresh();
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            mListener.izabranPosao(mPoslovi.get(position).getID());
        }
    }

    @Override
    public void refresh() {
        new UcitajPosloveTask().execute(mTipPosla);
    }

    private void rebind(List<Posao> poslovi) {
        Activity activity = getActivity();
        if (activity != null) {
            mPoslovi = poslovi;
            setListAdapter(new PosloviAdapter(activity, poslovi));
        }
    }

    private class UcitajPosloveTask extends AsyncTask<Integer, Void, List<Posao>> {

        @Override
        protected List<Posao> doInBackground(Integer... params) {
            try {
                HttpService httpService = new HttpService();
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
        }
    }
}
