package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.activities.IMainActivity;
import com.bitseverywhere.agavajobs.models.domain.Stats;
import com.bitseverywhere.agavajobs.services.HttpService;

import org.json.JSONException;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends android.support.v4.app.Fragment {


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private IMainActivity main;
    private TextView premijum, hot, standardni;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        View premijum = (View)view.findViewById(R.id.premijum1);
        premijum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriPrmijumPoslove();
            }
        });
        View premijum2 = (View)view.findViewById(R.id.premijum2);
        premijum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriPrmijumPoslove();
            }
        });
        View hot = (View)view.findViewById(R.id.hot1);
        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriHotPoslove();
            }
        });
        View hot2 = (View)view.findViewById(R.id.hot2);
        hot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriHotPoslove();
            }
        });
        View standradni = (View)view.findViewById(R.id.standard1);
        standradni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriStandardnePoslove();
            }
        });
        View standradni2 = (View)view.findViewById(R.id.standard2);
        standradni2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriStandardnePoslove();
            }
        });
        View biografija = (View)view.findViewById(R.id.biografija);
        biografija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.otvoriBiografiju();
            }
        });

        this.premijum = (TextView)view.findViewById(R.id.premijum);
        this.hot = (TextView)view.findViewById(R.id.hot);
        this.standardni = (TextView)view.findViewById(R.id.standard);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        main = (IMainActivity)activity;
    }

    private void prikaziPoslove(Stats stats) {
        if (stats != null) {
            premijum.setText(Integer.toString(stats.getPremijum()));
            hot.setText(Integer.toString(stats.getHot()));
            standardni.setText(Integer.toString(stats.getStandarni()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new UcitajTask().execute();
    }

    private class UcitajTask extends AsyncTask<Void, Void, Stats> {

        @Override
        protected Stats doInBackground(Void... params) {
            try {
                return HttpService.getInstance().vratiStatistiku();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Stats stats) {
            HomeFragment.this.prikaziPoslove(stats);
        }
    }


}
