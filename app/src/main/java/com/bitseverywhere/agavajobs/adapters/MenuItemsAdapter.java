package com.bitseverywhere.agavajobs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.models.IMenuItem;
import com.bitseverywhere.agavajobs.models.MenuGroup;
import com.bitseverywhere.agavajobs.models.MenuItem;

import java.util.ArrayList;

/**
 * Created by Željko on 13.12.2014..
 */
public class MenuItemsAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<IMenuItem> items;
    private LayoutInflater vi;

    public MenuItemsAdapter(Context context, ArrayList<IMenuItem> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final IMenuItem i = items.get(position);
        if (i != null) {
            if(i.isMenuGroup()){
                MenuGroup menuGroup = (MenuGroup)i;
                v = vi.inflate(R.layout.menu_group, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                final TextView sectionView =
                        (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(menuGroup.getCaption());
            } else {
                MenuItem menuItem = (MenuItem)i;
                v = vi.inflate(R.layout.menu_item, null);
                final ImageView img = (ImageView)v.findViewById(R.id.list_item_entry_drawable);
                if (img != null) {
                    img.setImageDrawable(menuItem.getImage());
                }
                final TextView title =
                        (TextView)v.findViewById(R.id.list_item_entry_title);
                if (title != null) title.setText(menuItem.getCaption());
            }
        }
        return v;
    }
}
