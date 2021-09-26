package com.iwan.shopcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomItemAdapter extends RecyclerView.Adapter<CustomItemAdapter.ViewHolder> {
    private final ArrayList<Cart> localDataSet;
    public AdapterListener listener;

    public CustomItemAdapter(ArrayList<Cart> dataSet, AdapterListener listener) {
        this.localDataSet = dataSet;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cart, viewGroup, false);

        return new ViewHolder(view, listener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tvNameCart.setText(localDataSet.get(position).id + " Nama Pembeli : " + localDataSet.get(position).name);
        viewHolder.tvItemCart.setText("Barang : " + localDataSet.get(position).item);
        viewHolder.tvNumberOfItemsCart.setText("Jumlah : " + localDataSet.get(position).numberOfItems);
        viewHolder.tvPriceCart.setText("Harga : " + localDataSet.get(position).price);
        viewHolder.tvTotalCart.setText("Total Harga : " + localDataSet.get(position).numberOfItems * localDataSet.get(position).price);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public Cart getItem(int position) {
        return localDataSet.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvNameCart;
        public final TextView tvItemCart;
        public final TextView tvNumberOfItemsCart;
        public final TextView tvPriceCart;
        public final TextView tvTotalCart;
        public final Button btnDeleteItem;

        public ViewHolder(View view, AdapterListener listener) {
            super(view);

            tvNameCart = view.findViewById(R.id.tvNameCart);
            tvItemCart = view.findViewById(R.id.tvItemCart);
            tvNumberOfItemsCart = view.findViewById(R.id.tvNumberOfItemsCart);
            tvPriceCart = view.findViewById(R.id.tvPriceCart);
            tvTotalCart = view.findViewById(R.id.tvTotalCart);
            btnDeleteItem = view.findViewById(R.id.btnDeleteItem);

            btnDeleteItem.setOnClickListener(v -> {
                listener.onDeleteClicked(view, getAdapterPosition());
            });
        }
    }
}
