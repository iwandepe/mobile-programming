package com.iwan.listviewandinputdialog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    ItemAdapter itemAdapter;
    ArrayList<Item> itemList;
    private ItemDbHelper openDB;
    ItemListFragment itemListFragment;
    private Item activeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB = new ItemDbHelper(this);

        itemList = new ArrayList<>();
        itemList = openDB.getAllItems();

        ImageView btnAdd = findViewById(R.id.btnAdd);
        ImageView btnDelete = findViewById(R.id.btnDelete);
        ImageView btnEdit = findViewById(R.id.btnEdit);
        ImageView btnFind = findViewById(R.id.btnFind);
        btnAdd.setOnClickListener(listener);
        btnDelete.setOnClickListener(listener);
        btnEdit.setOnClickListener(listener);
        btnFind.setOnClickListener(listener);

        itemAdapter = new ItemAdapter(this, R.layout.listview_item, itemList);

        itemListFragment = (ItemListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentItemList);

        itemListFragment.lvData.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            activeItem = (Item) parent.getItemAtPosition(position);
            Toast.makeText(this, "You choose " + activeItem.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAdd:
                    showAddDialog();
                    break;
                case R.id.btnEdit:
                    if (activeItem != null)
                        showEditDialog();
                    else
                        Toast.makeText(MainActivity.this, "Please choose contact", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnDelete:
                    if (activeItem != null)
                        deleteItem();
                    else
                        Toast.makeText(MainActivity.this, "Please choose contact", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnFind:
                    if (activeItem != null)
                        callNumber();
                    else
                        Toast.makeText(MainActivity.this, "Please choose contact", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void showAddDialog(){
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

    private void showEditDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View inputDialog = li.inflate(R.layout.input_dialog,null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(inputDialog);
        final EditText etName = inputDialog.findViewById(R.id.etName);
        final EditText etPhone = inputDialog.findViewById(R.id.etPhone);

        etName.setText(activeItem.getName());
        etPhone.setText(activeItem.getPhone());
        alertDialog
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString();
                        String phone = etPhone.getText().toString();

                        openDB.updateItem(activeItem, name, phone);

                        itemList = openDB.getAllItems();

                        for (int i = 0; i < itemList.size(); i++) {
                            Log.i(TAG, itemList.get(i).getPhone());
                        }

                        itemAdapter.clear();
                        itemAdapter.addAll(itemList);
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

    private void deleteItem(){
        openDB.deleteItem(activeItem.getId(), activeItem.getName());
        itemListFragment.removeList(activeItem);
        Toast.makeText(this, activeItem.getName() + " deleted", Toast.LENGTH_SHORT).show();
    }

    private void callNumber() {
        Uri number = Uri.parse("tel:" + activeItem.getPhone());
        Intent callNumber = new Intent(Intent.ACTION_DIAL, number);

        startActivity(callNumber);
    }
}