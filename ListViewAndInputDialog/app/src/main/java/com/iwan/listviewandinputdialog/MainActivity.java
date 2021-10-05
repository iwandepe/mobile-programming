package com.iwan.listviewandinputdialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(listener);

        ListView lvData = findViewById(R.id.lvData);


        ArrayList<Item> itemList = new ArrayList<>();

        itemAdapter = new ItemAdapter(this, R.layout.listview_item, itemList);

        lvData.setAdapter(itemAdapter);

        Item item1 = new Item("iwan", "092896");
        Item item2 = new Item("dwi", "092832");
        Item item3 = new Item("prakoso", "092567");

        itemAdapter.add(item1);
        itemAdapter.add(item2);
        itemAdapter.add(item3);
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
                        // do something
                        Item item = new Item(etName.getText().toString(), etPhone.getText().toString());
                        itemAdapter.add(item);
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