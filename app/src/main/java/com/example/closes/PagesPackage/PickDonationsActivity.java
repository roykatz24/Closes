package com.example.closes.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.closes.AdaptersPackage.AdapterDonations;
import com.example.closes.ModelsPackage.DonatesModel;
import com.example.closes.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PickDonationsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDonates;
    private final ArrayList<DonatesModel> donatesModelArrayList = new ArrayList<>();
    private AdapterDonations adapterDonations;
    private FirebaseFirestore firebaseFirestore;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_donations);

        initUI();
        initLocation();
        readDonatesFirestore();
    }

    private void initUI() {
        recyclerViewDonates = findViewById(R.id.recyclerViewDonates);

        recyclerViewDonates.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
    }

    private void readDonatesFirestore() {
        firebaseFirestore.collection("donations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donatesModelArrayList.clear();

                        for (DocumentSnapshot doc : task.getResult()) {
                            DonatesModel donatesModel = doc.toObject(DonatesModel.class);
                            double lat1 = donatesModel.getLat();
                            double lng1 = donatesModel.getLng();
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                            }// TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
                            if (provider != null) {
                                location = locationManager.getLastKnownLocation(provider);
                                if (location != null) {
                                    double distanceMe;
                                    Location locationA = new Location("Point A");
                                    locationA.setLatitude(lat1);
                                    locationA.setLongitude(lng1);
                                    Location locationB = new Location("Point B");
                                    locationB.setLatitude(location.getLatitude());
                                    locationB.setLongitude(location.getLongitude());
                                    distanceMe = locationA.distanceTo(locationB);
                                    if (distanceMe < 5000) {
                                        donatesModel.setId(doc.getId());
                                        donatesModelArrayList.add(donatesModel);
                                    }
                                }
                            }
                        }

                        adapterDonations = new AdapterDonations(donatesModelArrayList, this);
                        recyclerViewDonates.setAdapter(adapterDonations);
                    } else {
                        Toast.makeText(PickDonationsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
