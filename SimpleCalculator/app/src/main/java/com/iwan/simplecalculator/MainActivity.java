package com.iwan.simplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText bil1Text, bil2Text;
    TextView hasilText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bil1Text = (EditText) findViewById(R.id.bil1);
        this.bil2Text = (EditText) findViewById(R.id.bil2);
        this.hasilText = (TextView) findViewById(R.id.hasil);
    }

    public void hitung(View v) {
        float bil1, bil2, hasil;

        try {
            bil1 = Float.parseFloat(this.bil1Text.getText().toString());
            bil2 = Float.parseFloat(this.bil2Text.getText().toString());

            switch (v.getId()) {
                case R.id.tambah: hasil = bil1 + bil2; break;
                case R.id.kurang: hasil = bil1 - bil2; break;
                case R.id.kali: hasil = bil1 * bil2; break;
                case R.id.bagi: hasil = bil1 / bil2; break;
                default: hasil = 0; break;
            }

            Button btn = (Button) findViewById(v.getId());

            hasilText.setText(bil1 + " " + btn.getText().toString() + " " + bil2 + " = " + String.valueOf(hasil));
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Bilangan tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }
}