package com.iwan.listviewandinputdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private static class ViewHolder {
        TextView name;
        TextView phone;
    }
    public ItemAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View ConvertView, ViewGroup parent){
        Item item = getItem(position);
        ViewHolder vhItem;
        if(ConvertView==null){
            vhItem = new ViewHolder();
            ConvertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.listview_item, parent,false);
            vhItem.name = ConvertView.findViewById(R.id.tvName);
            vhItem.phone = ConvertView.findViewById(R.id.tvPhone);

            ConvertView.setTag(vhItem);
        }
        else {
            vhItem = (ViewHolder) ConvertView.getTag();
        }

        vhItem.name.setText(item.getName());
        vhItem.phone.setText(item.getPhone());

        return ConvertView;

    }
}
