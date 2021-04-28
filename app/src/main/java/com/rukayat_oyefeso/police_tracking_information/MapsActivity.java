package com.rukayat_oyefeso.police_tracking_information;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String userID;
    String lat;
    String lon;
    LatLng markerPoint;
    Marker[] mCurrLocationMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        userID = getIntent().getStringExtra("userID");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.postDelayed(this, 5000);

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPoint, 16));


                    //LatLng location2 = new LatLng(Double.parseDouble(BackgroundLocation.latitude),Double.parseDouble(BackgroundLocation.longitude));
                    //MarkerOptions marker2 = new MarkerOptions().position(location2).title("police");
                    //marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
                    //mCurrLocationMarker[0] = mMap.addMarker(marker2);
                }
            }
        };
        handler.postDelayed(runnable, 5000);

    }



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

        // Add a marker in Sydney and move the camera
        DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        final Double[] newLat = new Double[1];
        final Double[] newLong = new Double[1];
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newLat[0] = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                newLong[0] = Double.parseDouble(dataSnapshot.child("long").getValue().toString());
                markerPoint = new LatLng(newLat[0], newLong[0]);
                //LatLng sydney = new LatLng(37.391219,-121.930);
                MarkerOptions marker = new MarkerOptions().position(markerPoint).title("User");
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
                mCurrLocationMarker = new Marker[]{mMap.addMarker(marker)};
                mMap.moveCamera(CameraUpdateFactory.newLatLng(markerPoint));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));


                //zoom into a particular position
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                mMap.moveCamera(zoom);
                mMap.animateCamera(zoom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
