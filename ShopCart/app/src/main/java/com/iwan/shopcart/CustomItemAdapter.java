package com.iwan.shopcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomItemAdapter extends ArrayAdapter<Item> {
    public CustomItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);
        }

        // Get the data item for this position
        Item item = getItem(position);

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tvNameCart);
        TextView tvItem = convertView.findViewById(R.id.tvItemCart);
        TextView tvNumberOfItems = convertView.findViewById(R.id.tvNumberOfItemsCart);
        TextView tvPrice = convertView.findViewById(R.id.tvPriceCart);
        TextView tvTotal = convertView.findViewById(R.id.tvTotalCart);

        // Populate the data into the template view using the data object
        tvName.setText("Nama Pembeli : " + item.name);
        tvItem.setText("Nama Barang : " + item.item);
        tvNumberOfItems.setText("Jumlah Barang : " + item.numberOfItems);
        tvPrice.setText("Harga Barang : " + item.price);
        tvTotal.setText("Total Harga Barang : " + item.total);

        // Return the completed view to render on screen
        return convertView;
    }
}
