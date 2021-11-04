package com.iwan.listviewandinputdialog;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemListFragment extends Fragment {
    ItemAdapter itemAdapter;

    ArrayList<Item> itemList;

    private ItemDbHelper openDB;

    ListView lvData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        openDB = new ItemDbHelper(getContext());

        itemList = new ArrayList<>();
        itemList = openDB.getAllItems();

        lvData = getView().findViewById(R.id.lvData);

        itemAdapter = new ItemAdapter(getContext(), R.layout.listview_item, itemList);

        lvData.setAdapter(itemAdapter);
    }

    public void addList(Item item) {
        itemAdapter.add(item);
    }

    public void removeList(Item item) { itemAdapter.remove(item);}

    public void setLvData(ListView lvData) {
        this.lvData = lvData;
    }
}