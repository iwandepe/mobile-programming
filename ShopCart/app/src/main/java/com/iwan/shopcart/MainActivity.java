package com.iwan.shopcart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName, etItem, etNumberOfItems, etPrice, etMoneyPayed;
    TextView tvName, tvItem, tvNumberOfItems, tvPrice, tvMoneyPayed,
            tvTotal, tvChangeMoney, tvBonus, tvNotes;
    Button btnProcess, btnDelete, btnClose;

    String name, item, numberOfItemsStr, priceStr, moneyPayedStr;
    double numberOfItems, price, moneyPayed, total, changeMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Aplikasi Penjualan");

        //EditText
        etName = findViewById(R.id.etName);
        etItem = findViewById(R.id.etItem);
        etNumberOfItems = findViewById(R.id.etNumberOfItems);
        etPrice = findViewById(R.id.etPrice);
        etMoneyPayed = findViewById(R.id.etNumberOfItems);

        //TextView
        tvName = findViewById(R.id.tvNameCart);
        tvItem = findViewById(R.id.tvItemCart);
        tvNumberOfItems = findViewById(R.id.tvNumberOfItemsCart);
        tvPrice = findViewById(R.id.tvPriceCart);
        tvMoneyPayed = findViewById(R.id.tvMoneyPayedCart);
        tvTotal = findViewById(R.id.tvTotalCart);
        tvChangeMoney = findViewById(R.id.tvChangeMoneyCart);
        tvBonus = findViewById(R.id.tvBonus);
        tvNotes = findViewById(R.id.tvNotes);

        //Button
        btnProcess = findViewById(R.id.btnProcess);
        btnDelete = findViewById(R.id.btnDelete);
        btnClose = findViewById(R.id.btnClose);

        //Proses
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                name = etName.getText().toString().trim();
                item = etItem.getText().toString().trim();
                numberOfItemsStr = etNumberOfItems.getText().toString().trim();
                priceStr = etPrice.getText().toString().trim();
                moneyPayedStr = etMoneyPayed.getText().toString().trim();

                numberOfItems = Double.parseDouble(numberOfItemsStr);
                price = Double.parseDouble(priceStr);
                moneyPayed = Double.parseDouble(moneyPayedStr);
                total = (numberOfItems * price);

                tvName.setText("Nama Pembeli : " + name);
                tvItem.setText("Nama Barang : " + item);
                tvNumberOfItems.setText("Jumlah Barang : " + numberOfItemsStr);
                tvPrice.setText("Harga Barang : " + priceStr);
                tvMoneyPayed.setText("Uang bayar : " + moneyPayedStr);

                tvTotal.setText("Total Belanja " + total);
                if (total >= 200000) {
                    tvBonus.setText("Bonus : HardDisk 1TB");
                } else if (total >= 50000) {
                    tvBonus.setText("Bonus : Keyboard Gaming");
                } else if (total >= 40000) {
                    tvBonus.setText("Bonus : Mouse Gaming");
                } else {
                    tvBonus.setText("Bonus : Tidak ada bonus!");
                }

                changeMoney = (moneyPayed - total);
                if (moneyPayed < total) {
                    tvNotes.setText("Keterangan : Uang bayar kurang Rp. " + (-changeMoney));
                    tvChangeMoney.setText("Uang Kembalian : Rp. 0");
                } else {
                    tvNotes.setText("Keterangan : Tunggu kembalian");
                    tvChangeMoney.setText("Uang Kembalian : Rp. " + changeMoney);
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvName.setText("");
                tvItem.setText("");
                tvNumberOfItems.setText("");
                tvPrice.setText("");
                tvMoneyPayed.setText("");
                tvChangeMoney.setText("");
                tvNotes.setText("");
                tvBonus.setText("");
                tvTotal.setText("");

                Toast.makeText(getApplicationContext(), "Data sudah dihapus", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
            }
        });

    }
}