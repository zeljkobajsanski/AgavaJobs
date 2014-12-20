package com.bitseverywhere.agavajobs.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.activities.MainActivity;
import com.bitseverywhere.agavajobs.models.domain.RadnoIskustvo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RadnoIskustvoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadnoIskustvoFragment extends DialogFragment {

    private static final String ID = "id";
    private static final String POSLODAVAC = "poslodavac";
    private static final String PERIOD = "period";
    private static final String OPIS = "opis";

    private int id;
    private String poslodavac, period, opis;
    private MainActivity mainActivity;
    private RadnoIskustvo edit;
    private boolean update;


    public static RadnoIskustvoFragment newInstance(int id, String poslodavac, String period, String opis) {
        RadnoIskustvoFragment fragment = new RadnoIskustvoFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        args.putString(POSLODAVAC, poslodavac);
        args.putString(PERIOD, period);
        args.putString(OPIS, opis);
        fragment.setArguments(args);
        return fragment;
    }

    public RadnoIskustvoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Radno iskustvo");
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_radno_iskustvo, null);
        final EditText poslodavacTxt = ((EditText)view.findViewById(R.id.poslodavac));
        poslodavacTxt.setText(edit.getPoslodavac());
        final EditText periodTxt = ((EditText)view.findViewById(R.id.period));
        periodTxt.setText(edit.getPeriod());
        final EditText opisTxt = ((EditText)view.findViewById(R.id.opis));
        opisTxt.setText(edit.getOpis());
        builder.setView(view);
        builder.setPositiveButton("Prihvati", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BiografijaFragment caller = ((BiografijaFragment)mainActivity.getActiveFragment());
                edit.setPoslodavac(poslodavacTxt.getText().toString());
                edit.setPeriod(periodTxt.getText().toString());
                edit.setOpis(opisTxt.getText().toString());

                if (update) {
                    caller.azurirajRadnoIskustvo(edit);
                } else {
                    caller.dodajRadnoIskustvo(edit);
                }

            }
        });
        builder.setNegativeButton("Otkaži", null);
        return builder.create();
    }

    public void setEdit(RadnoIskustvo edit, boolean update) {
        this.edit = edit;
        this.update = update;
    }
}
