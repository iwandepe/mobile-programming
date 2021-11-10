package com.iwan.ets

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToContact = findViewById<Button>(R.id.btnToContact)
        val btnToMaps = findViewById<Button>(R.id.btnToMaps)
        val btnToGoogle = findViewById<Button>(R.id.btnToGoogle)
        val btnToYoutube = findViewById<Button>(R.id.btnToYoutube)

        btnToContact.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

        btnToMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        btnToGoogle.setOnClickListener{
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }

        btnToYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
            intent.setPackage("com.google.android.youtube");
            startActivity(intent)
        }
    }
}