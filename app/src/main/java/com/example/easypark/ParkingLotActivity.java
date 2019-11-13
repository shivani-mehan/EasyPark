package com.example.easypark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ParkingLotActivity extends AppCompatActivity implements OnMapReadyCallback, OnMarkerDragListener {

    private GoogleMap mMap;
    LatLng parkingPosition;
    String markerTitle;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot);

        // Build The Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set Action Bar
        markerTitle = getIntent().getStringExtra("markerTitle");
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle(markerTitle);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reserved!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent("INTENT_NAME").putExtra("markerTitle", markerTitle).putExtra("latlng", marker.getPosition());
                LocalBroadcastManager.getInstance(ParkingLotActivity.this).sendBroadcast(intent);
            }
        });

        // Get Data
        parkingPosition = getIntent().getParcelableExtra("latlng");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set draggable markers
        marker = mMap.addMarker(new MarkerOptions().position(parkingPosition).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot)));
        mMap.setOnMarkerDragListener(this);


        // Move to parking lot
        if (markerTitle.equals("Lazaridis Hall Parking")) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingPosition, (float)19));
        }
        else if (!markerTitle.equals("Bricker Academic Parking Lot")){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingPosition, (float)20.25));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkingPosition, (float)19.25));
        }

        mMap.setMapType(mMap.MAP_TYPE_SATELLITE); // Here is where you set the map type

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
