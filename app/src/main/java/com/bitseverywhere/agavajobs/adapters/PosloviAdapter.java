package com.bitseverywhere.agavajobs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.models.domain.Posao;

import java.util.List;


/**
 * Created by Željko on 13.12.2014..
 */
public class PosloviAdapter extends ArrayAdapter<Posao> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Posao> source;

    public PosloviAdapter(Context context) {
        super(context, R.layout.row_posao);
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.row_posao, parent, false);
        Posao posao = getItem(position);
        TextView kompanija = (TextView)view.findViewById(R.id.kompanija);
        kompanija.setText(posao.getPoslodavac());
        TextView lokacija = (TextView)view.findViewById(R.id.lokacija);
        lokacija.setText(posao.getLokacija());
        TextView naziv = (TextView)view.findViewById(R.id.nazivOglasa);
        naziv.setText(posao.getNaziv());
        TextView rok = (TextView)view.findViewById(R.id.rok);
        rok.setText(posao.getRok());
        return view;
    }
}
