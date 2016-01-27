package com.enlaps.m.and.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by vsatish on 1/26/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item>{

    public ItemAdapter(Context context, ArrayList<Item> items) {
        super( context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);

        tvTitle.setText( item.title);
        tvPriority.setText(item.priority);

        return convertView;
    }
}
