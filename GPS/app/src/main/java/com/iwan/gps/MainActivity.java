package com.iwan.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationManager locationManager;
    private LocationListener locationListener;

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fMap);
        mapFragment.getMapAsync(this);

        Button btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                goToLocation();
            }
        });

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 200, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal : gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
            case R.id.terrain : gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
            case R.id.sattelite: gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
            case R.id.hibryd: gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); break;
            case R.id.none : gMap.setMapType(GoogleMap.MAP_TYPE_NONE); break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void goToLocation() {
        EditText etLat = findViewById(R.id.etLat);
        EditText etLong = findViewById(R.id.etLong);
        EditText etZoom = findViewById(R.id.etZoom);

        Double lat = Double.parseDouble(etLat.getText().toString());
        Double lng = Double.parseDouble(etLong.getText().toString());
        Double zoom = Double.parseDouble(etZoom.getText().toString());

        Toast.makeText(this, "Move to Lat: " + lat + " Long: " + lng, Toast.LENGTH_SHORT).show();

        LatLng newLocation = new LatLng(lat, lng);
        gMap.addMarker(new MarkerOptions().position(newLocation).title("Marker in " + lat + ":" + lng));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMinZoomPreference(5);
        gMap.setMaxZoomPreference(20);

        LatLng itsLocation = new LatLng(-7.28,112.79);
        gMap.addMarker(new MarkerOptions().position(itsLocation).title("Marker ITS Campus"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(itsLocation));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class LocListener implements LocationListener {
        private TextView tvLat, tvLong;

        @Override
        public void onLocationChanged(@NonNull Location location) {
            tvLat = findViewById(R.id.tvLat);
            tvLong = findViewById(R.id.tvLong);

            tvLat.setText(String.valueOf(location.getLatitude()));
            tvLong.setText(String.valueOf(location.getLongitude()));
            Toast.makeText(getBaseContext(),"GPS capture", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {

        }

        @Override
        public void onFlushComplete(int requestCode) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
}