package com.example.easypark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.easypark.ui.login.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnNavigationItemSelectedListener, OnInfoWindowClickListener {

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle t;
    private NavigationView navigationView;
    private static final String TAG = "MapsActivity";

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // A default location and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(43.474448, -80.528900);

    // Reservation Info
    String parkingLotName;
    LatLng latlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Broadcast Receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));


        // Build The Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Menu Stuff
        drawerLayout = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            parkingLotName = intent.getStringExtra("markerTitle");
            latlng = intent.getParcelableExtra("latlng");
        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get Users Location
        getLocationPermission();
        updateLocationUI();
//        getDeviceLocation();

        // Default Location: WLU
        LatLng wlu = new LatLng(43.473341, -80.529291);
        mMap.addMarker(new MarkerOptions().position(wlu).title("Wilfrid Laurier University").snippet
                ("Wilfrid Laurier University is a public univeristy in" + "\n" + "Waterloo, Ontario."
                + "It was founded in 1911 and is" + "\n" + "named after the 7th Prime Minister of Canada."));

        // Add Hotspots
        hotspotMarkers();

        // Add parking Markers
        parkingMarkers();

        // Info Window
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wlu, (float)16));

    }

    private void hotspotMarkers() {
        // Starbucks
        LatLng starbs = new LatLng(43.476379, -80.525306);
        mMap.addMarker(new MarkerOptions().position(starbs).title("Starbucks").snippet
                ("Address: 247 King St N, Waterloo" + "\n" +
                        "Hours: 6am - 10:30pm" + "\n" + "Type: Coffeehouse/Cafe"));

        // McDonalds
        LatLng mcds = new LatLng(43.481790, -80.525578);
        mMap.addMarker(new MarkerOptions().position(mcds).title("McDonalds").snippet
                ("Address: 362 King Street N, Waterloo" + "\n" + "Hours: Open 24 hours"
                        + "\n" + "Type: Fast food"));

        // Phils
        LatLng phils = new LatLng(43.475247, -80.524392);
        mMap.addMarker(new MarkerOptions().position(phils).title("Phils").snippet
                ("Address: 232 King Street N, Waterloo" + "\n" + "Hours: 9:30PM - 2:30AM WFSS"
                        + "\n" + "Type: Nightclub"));

        // Athletic Complex
        LatLng AC = new LatLng(43.475314, -80.525647);
        mMap.addMarker(new MarkerOptions().position(AC).title("Athletic Complex").snippet
                ("Address: 232 King Street N, Waterloo" + "\n" + "Hours: 6:00AM - 11:00PM"
                        + "\n" + "Type: Gym"));

    }

    private void parkingMarkers() {
        // Parking Behind Bricker
        LatLng parkingBehindBricker = new LatLng(43.473044, -80.526571);
        mMap.addMarker(new MarkerOptions().position(parkingBehindBricker)
                .title("Bricker Academic Parking Lot")
                .snippet(getString(R.string.available_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,15)) +
                        "\n" +
                        getString(R.string.taken_spots) + " " +
                        String.format("%.0f", 50 - getRandomNumbers(1,30)) + "\n"
                        + getString(R.string.parking_hours) + " " + "24/7" + "\n"
                        + getString(R.string.parking_price) + " " + "Gold and Orange Permits Only")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));

        // Parking Lot 4
        LatLng parkingLot4 = new LatLng(43.473904, -80.527118);
        mMap.addMarker(new MarkerOptions().position(parkingLot4)
                .title("Visitor Parking Lot 4")
                .snippet(getString(R.string.available_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,15)) +
                        "\n" +
                        getString(R.string.taken_spots) + " " +
                        String.format("%.0f", 50 - getRandomNumbers(1,30)) + "\n"
                        + getString(R.string.parking_hours) + " " + "7am - 11pm" + "\n"
                        + getString(R.string.parking_price) + " " + "$3/hour or $10/day")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking))) ;

        // Parking Lot 20
        LatLng parkingLot20 = new LatLng(43.474230, -80.527218);
        mMap.addMarker(new MarkerOptions().position(parkingLot20)
                .title("Visitor Parking Lot 20")
                .snippet(getString(R.string.available_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,15)) +
                        "\n" +
                        getString(R.string.taken_spots) + " " +
                        String.format("%.0f", 50 - getRandomNumbers(1,30)) + "\n"
                        + getString(R.string.parking_hours) + " " + "7am - 11pm" + "\n"
                        + getString(R.string.parking_price) + " " + "$3/hour or $10/day")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));

        // Parking Lot 3B
        LatLng parkingLot3B = new LatLng(43.473856, -80.526418);
        mMap.addMarker(new MarkerOptions().position(parkingLot3B)
                .title("Parking Lot 3B")
                .snippet(getString(R.string.available_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,15)) +
                        "\n" +
                        getString(R.string.taken_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,30)) + "\n"
                        + getString(R.string.parking_hours) + " " + "24/7" + "\n"
                        + getString(R.string.parking_price) + " " + "Gold and White Permits Only")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));

        // Laz Hall
        LatLng laz = new LatLng(43.475713, -80.530073);
        mMap.addMarker(new MarkerOptions().position(laz)
                .title("Lazaridis Hall Parking")
                .snippet(getString(R.string.available_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,15)) +
                        "\n" +
                        getString(R.string.taken_spots) + " " +
                        String.format("%.0f", getRandomNumbers(1,30)) + "\n"
                        + getString(R.string.parking_hours) + " " + "24/7" + "\n"
                        + getString(R.string.parking_price) + " " + "Gold Permit + Pay & Display ($3/hr, $10/day)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));

    }

    private double getRandomNumbers(double min, double max) {
        double x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 16));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, 16));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item)) {
            navigationView.setCheckedItem(R.id.nav_none);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationView.setCheckedItem(R.id.nav_none);

        switch (menuItem.getItemId()){

            case R.id.nav_res:{
                if (parkingLotName != null && latlng != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    Bundle bundle = new Bundle();
                    bundle.putString("parkingLotName", parkingLotName);
                    bundle.putParcelable("latlng", latlng);

                    ReservationFragment fragment = new ReservationFragment();
                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.drawer_layout, fragment).addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ReservationFragment fragment = new ReservationFragment();
                    fragmentTransaction.add(R.id.drawer_layout, fragment).addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            }

            case R.id.nav_legend:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LegendFragment fragment = new LegendFragment();
                fragmentTransaction.add(R.id.drawer_layout, fragment).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_about:{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AboutFragment fragment = new AboutFragment();
                fragmentTransaction.add(R.id.drawer_layout, fragment).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_logout:{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }

        }

        menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, ParkingLotActivity.class);
        intent.putExtra("latlng", marker.getPosition());
        intent.putExtra("markerTitle", marker.getTitle());
        startActivity(intent);
    }
}
