package com.bitseverywhere.agavajobs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.bitseverywhere.agavajobs.models.domain.Zanimanje;

/**
 * Created by Željko on 17.12.2014..
 */
public class PrihvatljivaZanimanjaAdapter extends ArrayAdapter<Zanimanje> {

    private LayoutInflater layoutInflater;

    public PrihvatljivaZanimanjaAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_multiple_choice);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        CheckedTextView t = (CheckedTextView)view.findViewById(android.R.id.text1);
        return view;
    }
}
