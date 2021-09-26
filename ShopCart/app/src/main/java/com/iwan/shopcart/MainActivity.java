package com.iwan.shopcart;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etName, etItem, etNumberOfItems, etPrice, etMoneyPayed;
    TextView tvMoneyPayed, tvTotal, tvChangeMoney, tvBonus, tvNotes;
    Button btnProcess, btnDelete, btnClose, btnPay;

    ArrayList<Cart> cartList;
    CustomItemAdapter adapter;

    int total = 0;

    private CartDbHelper openDB;

    private void setDefaultText() {
        tvMoneyPayed.setText(R.string.money_payed_default_text);
        tvChangeMoney.setText(R.string.default_text);
        tvNotes.setText(R.string.default_text);
        tvBonus.setText(R.string.default_text);
        tvTotal.setText(R.string.total_default_text);
    }

    private void clearEditText() {
        etName.setText("");
        etItem.setText("");
        etPrice.setText("");
        etNumberOfItems.setText("");
        etMoneyPayed.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB = new CartDbHelper(this);

        cartList = new ArrayList<>();
        cartList = openDB.getAllCarts();

        RecyclerView recyclerView = findViewById(R.id.rvCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomItemAdapter(cartList, new AdapterListener() {
            @Override
            public void onDeleteClicked(View view, int position) {
                Cart cart = adapter.getItem(position);

                openDB.deleteCart(cart.id);

                cartList.remove(position);
                adapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Data terhapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClicked(View view, int position) {
            }
        });

        recyclerView.setAdapter(adapter);

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

        this.setDefaultText();

        //Proses
        btnProcess.setOnClickListener(v -> {
            try {
                String name = etName.getText().toString().trim();
                String item = etItem.getText().toString().trim();
                int numberOfItems = Integer.parseInt(etNumberOfItems.getText().toString().trim());
                int price = Integer.parseInt(etPrice.getText().toString().trim());

                double totalPrice = (numberOfItems * price);

                total += totalPrice;

                Cart cart = new Cart(name, item, numberOfItems, price);

                long id = openDB.addCart(cart);

                cart.id = id;

                int index = adapter.getItemCount();

                cartList.add(cart);

                adapter.notifyItemInserted(index);

                tvTotal.setText("Total Belanja = " + total);

                this.clearEditText();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Isi data dengan lengkap !!!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPay.setOnClickListener(v -> {
            try {
                if (adapter.getItemCount() > 0) {
                    String moneyPayedStr = etMoneyPayed.getText().toString().trim();

                    int moneyPayed = Integer.parseInt(moneyPayedStr);

                    tvMoneyPayed.setText("Uang bayar : " + moneyPayedStr);

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

                    int changeMoney = (moneyPayed - total);
                    if (moneyPayed < total) {
                        tvNotes.setText("Keterangan : Uang bayar kurang Rp. " + (-changeMoney));
                        tvChangeMoney.setText("Uang Kembalian : Rp. 0");
                    } else {
                        tvNotes.setText(R.string.change_money_notes);
                        tvChangeMoney.setText("Uang Kembalian : Rp. " + changeMoney);
                    }
                    etMoneyPayed.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Belum ada barang yang bisa dibayar !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Masukkan nominal bayar !!!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            this.setDefaultText();

            openDB.getAllCarts();

            total = 0;

            cartList.clear();
            adapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Data sudah dihapus", Toast.LENGTH_SHORT).show();
        });

        btnClose.setOnClickListener(v -> moveTaskToBack(true));

    }

    @Override
    protected void onDestroy() {
        openDB.close();
        super.onDestroy();
    }
}