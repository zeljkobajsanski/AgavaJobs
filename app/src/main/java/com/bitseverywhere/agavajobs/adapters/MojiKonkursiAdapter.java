package com.bitseverywhere.agavajobs.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.models.domain.Posao;

/**
 * Created by Željko on 20.12.2014..
 */
public class MojiKonkursiAdapter extends PosloviAdapter {

    public MojiKonkursiAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.moj_konkurs, parent, false);
        Posao posao = getItem(position);
        TextView kompanija = (TextView)view.findViewById(R.id.kompanija);
        kompanija.setText(posao.getPoslodavac());
        TextView lokacija = (TextView)view.findViewById(R.id.lokacija);
        lokacija.setText(posao.getLokacija());
        TextView naziv = (TextView)view.findViewById(R.id.nazivOglasa);
        naziv.setText(posao.getNaziv());
        TextView brojPrijavljenih = (TextView)view.findViewById(R.id.brojPrijavljenih);
        brojPrijavljenih.setText(Integer.toString(posao.getBrojPrijavljenih()));
        return view;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getID();
    }
}
