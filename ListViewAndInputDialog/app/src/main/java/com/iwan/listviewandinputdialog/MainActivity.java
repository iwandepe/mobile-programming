package com.iwan.listviewandinputdialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ItemAdapter itemAdapter;

    ArrayList<Item> itemList;

    private ItemDbHelper openDB;

    ItemListFragment itemListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB = new ItemDbHelper(this);

        itemList = new ArrayList<>();
        itemList = openDB.getAllItems();

        ImageView btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(listener);

        itemAdapter = new ItemAdapter(this, R.layout.listview_item, itemList);

        itemListFragment = (ItemListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentItemList);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAdd:
                    showDialog();
                    break;
            }
        }
    };

    private void showDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View inputDialog = li.inflate(R.layout.input_dialog,null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(inputDialog);
        final EditText etName = inputDialog.findViewById(R.id.etName);
        final EditText etPhone = inputDialog.findViewById(R.id.etPhone);
        alertDialog
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString();
                        String phone = etPhone.getText().toString();

                        Item item = new Item(name, phone);

                        long id = openDB.addItem(item);
                        item.setId(id);
                        itemListFragment.addList(item);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}