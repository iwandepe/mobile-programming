package com.iwan.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity
        extends AppCompatActivity
        implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int PERMISSION_GET_LAST_LOCATION = 10;
    public static final int PERMISSION_REQUEST_LOCATION_UPDATES = 11;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    private MapsFragment fragMap;

    private Button btnStartUpdate, btnStopUpdate;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest = new LocationRequest();

    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime = "";

    private TextView tvLat, tvLong, tvUpdate;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Aplikasi GPS");

        fragMap = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.fragMap);
        setupItemView();
        setupView();

        updateValuesFromBundle(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();

    }

    private void setupItemView(){
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        tvUpdate = findViewById(R.id.tvUpdate);

        btnStartUpdate = findViewById(R.id.btnStartUpdate);
        btnStopUpdate = findViewById(R.id.btnStopUpdate);
        btnStartUpdate.setOnClickListener(op);
        btnStopUpdate.setOnClickListener(op);

    }

    private void setupView(){
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();
        mGoogleApiClient.connect();


    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateUI();
            }
        };
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_GET_LAST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is not granted, this app won't work.",  Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PERMISSION_REQUEST_LOCATION_UPDATES) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is not granted, this app won't work.",  Toast.LENGTH_LONG).show();
            }
            else {
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback, Looper.myLooper());
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("NonConstantResourceId")
    View.OnClickListener op = view -> {
        switch (view.getId()) {
            case R.id.btnStartUpdate:
                mRequestingLocationUpdates = true;
                startLocationUpdates();
                break;
            case R.id.btnStopUpdate:
                mRequestingLocationUpdates = false;
                break;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal: fragMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
            case R.id.terrain: fragMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
            case R.id.sattelite: fragMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
            case R.id.hibryd: fragMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); break;
            case R.id.none: fragMap.setMapType(GoogleMap.MAP_TYPE_NONE); break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected is called", Toast.LENGTH_LONG).show();
        if (mCurrentLocation == null) {
            String[] permission = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions(this, permission, PERMISSION_GET_LAST_LOCATION);
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    private void startLocationUpdates() {
        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_LOCATION_UPDATES);
    }

    private void updateUI() {
        tvLat.setText(String.format("%f", mCurrentLocation.getLatitude()));
        tvLong.setText(String.format("%f", mCurrentLocation.getLongitude()));
        tvUpdate.setText(String.format("%s", mLastUpdateTime));

        fragMap.setMyLoc(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}

class GpsListener implements LocationListener {
    private TextView tvLat, tvLong;
    private Context context;

    public GpsListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        tvLat = ((Activity) context).findViewById(R.id.tvLat);
        tvLong = ((Activity) context).findViewById(R.id.tvLong);

        tvLat.setText(String.valueOf(location.getLatitude()));
        tvLong.setText(String.valueOf(location.getLongitude()));

        Toast.makeText(context, "GPS Capture", Toast.LENGTH_LONG).show();
    }
}

//package com.iwan.gps;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//    private LocationManager locationManager;
//    private LocationListener locationListener;
//
//    private GoogleMap gMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocListener();
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getBaseContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fMap);
//        mapFragment.getMapAsync(this);
//
//        Button btnGo = findViewById(R.id.btnGo);
//        btnGo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                hideKeyboard(view);
//                goToLocation();
//            }
//        });
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 200, locationListener);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.normal : gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
//            case R.id.terrain : gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
//            case R.id.sattelite: gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
//            case R.id.hibryd: gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); break;
//            case R.id.none : gMap.setMapType(GoogleMap.MAP_TYPE_NONE); break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void hideKeyboard(View v) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }
//
//    private void goToLocation() {
//        EditText etLat = findViewById(R.id.etLat);
//        EditText etLong = findViewById(R.id.etLong);
//        EditText etZoom = findViewById(R.id.etZoom);
//
//        Double lat = Double.parseDouble(etLat.getText().toString());
//        Double lng = Double.parseDouble(etLong.getText().toString());
//        Double zoom = Double.parseDouble(etZoom.getText().toString());
//
//        Toast.makeText(this, "Move to Lat: " + lat + " Long: " + lng, Toast.LENGTH_SHORT).show();
//
//        LatLng newLocation = new LatLng(lat, lng);
//        gMap.addMarker(new MarkerOptions().position(newLocation).title("Marker in " + lat + ":" + lng));
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//        gMap.setMinZoomPreference(5);
//        gMap.setMaxZoomPreference(20);
//
//        LatLng itsLocation = new LatLng(-7.28,112.79);
//        gMap.addMarker(new MarkerOptions().position(itsLocation).title("Marker ITS Campus"));
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(itsLocation));
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//
//    private class LocListener implements LocationListener {
//        private TextView tvLat, tvLong;
//
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            tvLat = findViewById(R.id.tvLat);
//            tvLong = findViewById(R.id.tvLong);
//
//            tvLat.setText(String.valueOf(location.getLatitude()));
//            tvLong.setText(String.valueOf(location.getLongitude()));
//            Toast.makeText(getBaseContext(),"GPS capture", Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onLocationChanged(@NonNull List<Location> locations) {
//
//        }
//
//        @Override
//        public void onFlushComplete(int requestCode) {
//
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(@NonNull String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(@NonNull String provider) {
//
//        }
//    }
//}