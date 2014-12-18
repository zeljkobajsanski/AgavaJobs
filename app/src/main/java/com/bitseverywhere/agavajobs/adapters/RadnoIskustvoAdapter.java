package com.bitseverywhere.agavajobs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.models.domain.RadnoIskustvo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Željko on 17.12.2014..
 */
public class RadnoIskustvoAdapter extends ArrayAdapter<RadnoIskustvo> {
    private LayoutInflater layoutInflater;
    private List<RadnoIskustvo> selections = new ArrayList<>();

    public RadnoIskustvoAdapter(Context context) {
        super(context, R.layout.radno_iskustvo);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.radno_iskustvo, parent, false);
        RadnoIskustvo entity = getItem(position);
        ((TextView)view.findViewById(R.id.poslodavac)).setText(entity.getPoslodavac());
        ((TextView)view.findViewById(R.id.period)).setText(entity.getPeriod());
        ((TextView)view.findViewById(R.id.opis)).setText(entity.getOpis());
        ((CheckBox)view.findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selections.add(getItem(position));
                } else {
                    selections.remove(getItem(position));
                }
            }
        });
        return view;
    }

    public List<RadnoIskustvo> getSelections() {
        return selections;
    }
}
