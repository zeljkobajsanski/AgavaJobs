package com.bitseverywhere.agavajobs.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Željko on 13.12.2014..
 */
public class GalleryAdapter extends BaseAdapter {

    private String[] images;
    private Context context;

    public GalleryAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = new ImageView(context);
        Picasso.with(context).load(images[position]).into(view);
        view.setLayoutParams(new Gallery.LayoutParams(400, 400));
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }
}