package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.activities.MainActivity;
import com.bitseverywhere.agavajobs.models.domain.Delatnost;
import com.bitseverywhere.agavajobs.models.domain.Zanimanje;

public class PrihvatljivaZanimanjaFragment extends DialogFragment {

    private ArrayAdapter<Delatnost> delatnostArrayAdapter;
    private ArrayAdapter<Zanimanje> zanimanjaArrayAdapter;
    private BiografijaFragment biografijaFragment;

    public static PrihvatljivaZanimanjaFragment newInstance(ArrayAdapter<Delatnost> prihvatljivaZanimanjaAdapter) {
        PrihvatljivaZanimanjaFragment fragment = new PrihvatljivaZanimanjaFragment();
        fragment.delatnostArrayAdapter = new ArrayAdapter<Delatnost>(prihvatljivaZanimanjaAdapter.getContext(), android.R.layout.simple_list_item_1);
        for (int i = 0; i < prihvatljivaZanimanjaAdapter.getCount(); i++) {
            fragment.delatnostArrayAdapter.add(prihvatljivaZanimanjaAdapter.getItem(i));
        }
        fragment.zanimanjaArrayAdapter = new ArrayAdapter<Zanimanje>(prihvatljivaZanimanjaAdapter.getContext(), android.R.layout.simple_list_item_1);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PrihvatljivaZanimanjaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        biografijaFragment = (BiografijaFragment)((MainActivity)activity).getActiveFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_prihvatljiva_zanimanja, null);
        final Spinner zanimanja = (Spinner)view.findViewById(R.id.zanimanje);
        zanimanja.setAdapter(zanimanjaArrayAdapter);
        Spinner delatnosti = (Spinner)view.findViewById(R.id.delatnost);
        delatnosti.setAdapter(delatnostArrayAdapter);
        delatnosti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Delatnost d = delatnostArrayAdapter.getItem(position);
                zanimanjaArrayAdapter.clear();
                zanimanjaArrayAdapter.addAll(d.getZanimanja());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Prihvatljiva zanimanja");
        builder.setView(view);
        builder.setPositiveButton("Prihvati", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Zanimanje zanimanje = (Zanimanje)zanimanja.getSelectedItem();
                biografijaFragment.dodajPrihvatljivoZanimanje(zanimanje);
            }
        });
        builder.setNegativeButton("Otkaži", null);
        return builder.show();
    }
}
