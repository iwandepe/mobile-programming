package com.iwan.shopcart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etName, etItem, etNumberOfItems, etPrice, etMoneyPayed;
    TextView tvMoneyPayed, tvTotal, tvChangeMoney, tvBonus, tvNotes;
    Button btnProcess, btnDelete, btnClose, btnPay;

    String name, item, numberOfItemsStr, priceStr, moneyPayedStr;
    double numberOfItems, price, moneyPayed, changeMoney, total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Construct the data source
        ArrayList<Item> arrayOfUsers = new ArrayList<>();
        // Create the adapter to convert the array to views
        CustomItemAdapter adapter = new CustomItemAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView =  findViewById(R.id.lvItem);
        listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listView);

        //EditText
        etName = findViewById(R.id.etName);
        etItem = findViewById(R.id.etItem);
        etNumberOfItems = findViewById(R.id.etNumberOfItems);
        etPrice = findViewById(R.id.etPrice);
        etMoneyPayed = findViewById(R.id.etMoneyPayed);

        //TextView
        tvMoneyPayed = findViewById(R.id.tvMoneyPayed);
        tvTotal = findViewById(R.id.tvTotal);
        tvChangeMoney = findViewById(R.id.tvChangeMoney);
        tvBonus = findViewById(R.id.tvBonus);
        tvNotes = findViewById(R.id.tvNotes);

        //Button
        btnProcess = findViewById(R.id.btnProcess);
        btnDelete = findViewById(R.id.btnDelete);
        btnClose = findViewById(R.id.btnClose);
        btnPay = findViewById(R.id.btnPay);

        // Set data to default
        tvMoneyPayed.setText(R.string.money_payed_default_text);
        tvChangeMoney.setText(R.string.default_text);
        tvNotes.setText(R.string.default_text);
        tvBonus.setText(R.string.default_text);
        tvTotal.setText(R.string.total_default_text);

        //Proses
        btnProcess.setOnClickListener(v -> {
            try {
                name = etName.getText().toString().trim();
                item = etItem.getText().toString().trim();
                numberOfItemsStr = etNumberOfItems.getText().toString().trim();
                priceStr = etPrice.getText().toString().trim();

                numberOfItems = Double.parseDouble(numberOfItemsStr);
                price = Double.parseDouble(priceStr);
                double totalPrice = (numberOfItems * price);

                Item newItem = new Item(name, item, numberOfItemsStr, priceStr, String.valueOf(totalPrice));
                adapter.add(newItem);

                setListViewHeightBasedOnChildren(listView);

                total += totalPrice;

                tvTotal.setText("Total Belanja = " + total);

                etName.setText("");
                etItem.setText("");
                etPrice.setText("");
                etNumberOfItems.setText("");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Isi data dengan lengkap !!!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPay.setOnClickListener(v -> {
            try {
                if (adapter.getCount() > 0) {
                    moneyPayedStr = etMoneyPayed.getText().toString().trim();

                    moneyPayed = Double.parseDouble(moneyPayedStr);

                    tvMoneyPayed.setText( "Uang bayar : " + moneyPayedStr);

                    tvTotal.setText("Total Belanja = " + total);
                    if (total >= 200000) {
                        tvBonus.setText(R.string.bonus_1_text);
                    } else if (total >= 50000) {
                        tvBonus.setText(R.string.bonus_2_text);
                    } else if (total >= 40000) {
                        tvBonus.setText(R.string.bonus_3_text);
                    } else {
                        tvBonus.setText(R.string.no_bonus_text);
                    }

                    changeMoney = (moneyPayed - total);
                    if (moneyPayed < total) {
                        tvNotes.setText("Keterangan : Uang bayar kurang Rp. " + (-changeMoney));
                        tvChangeMoney.setText("Uang Kembalian : Rp. 0");
                    } else {
                        tvNotes.setText(R.string.change_money_notes);
                        tvChangeMoney.setText("Uang Kembalian : Rp. " + changeMoney);
                    }
                    etMoneyPayed.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Belum ada barang yang bisa dibayar !!!", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Masukkan nominal bayar !!!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            tvTotal.setText(R.string.total_default_text);
            tvMoneyPayed.setText(R.string.money_payed_default_text);
            tvChangeMoney.setText(R.string.default_text);
            tvNotes.setText(R.string.default_text);
            tvBonus.setText(R.string.default_text);

            total = 0;

            adapter.clear();
            setListViewHeightBasedOnChildren(listView);

            Toast.makeText(getApplicationContext(), "Data sudah dihapus", Toast.LENGTH_SHORT).show();
        });

        btnClose.setOnClickListener(v -> moveTaskToBack(true));

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}