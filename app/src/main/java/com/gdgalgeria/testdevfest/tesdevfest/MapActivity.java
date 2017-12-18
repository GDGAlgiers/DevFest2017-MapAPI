package com.gdgalgeria.testdevfest.tesdevfest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("infos_places")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
//                                Log.e("success query", document.getId() + " => " + document.getData());


                                /** Split LatLng into 2 double **/
                                String[] latlong =  document.getString("latlng").toString().split(",");
                                double latitude = Double.parseDouble(latlong[0]);
                                double longitude = Double.parseDouble(latlong[1]);

                                /** get Name & Address **/
                                String name = document.getString("name").toString();
                                String address = document.getString("address").toString();

                                /** Call method addMarker **/
                                addMarker(name,address,latitude,longitude);
                            }
                        } else {
                            Log.e("fail query", "Error getting documents: ", task.getException());
                        }
                    }
                });

        // Add a marker in Algiers, ESI, and move the camera.
        LatLng esi = new LatLng(36.699237, 3.175188);
        mMap.addMarker(new MarkerOptions().position(esi).title("We are Here !"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(esi,5));
    }

    public void addMarker(String name,String address,double lat, double lng){
        LatLng marker = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(marker).title(name).snippet(address));
    }


}