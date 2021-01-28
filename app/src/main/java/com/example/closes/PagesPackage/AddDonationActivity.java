package com.example.closes.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.closes.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDonationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etDescription, etPhone, etSize;
    private Button btnAddDonate;
    private FirebaseFirestore firebaseFirestore;
    private String name, description, phone, size;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);

        initUI();
        initListeners();
        initLocation();
    }

    private void initUI() {
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPhone = findViewById(R.id.etPhone);
        etSize = findViewById(R.id.etSize);
        btnAddDonate = findViewById(R.id.btnAddDonate);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void initListeners() {
        btnAddDonate.setOnClickListener(this);
    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
    }

    private void addDataToFirestore() {
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
                name = etName.getText().toString();
                description = etDescription.getText().toString();
                phone = etPhone.getText().toString();
                size = etSize.getText().toString();

                if (!name.equals("") && !description.equals("") && !phone.equals("") && !size.equals("")) {
                    Map<String, Object> addDonationHashMap = new HashMap<>();
                    addDonationHashMap.put("name", name);
                    addDonationHashMap.put("description", description);
                    addDonationHashMap.put("phone", phone);
                    addDonationHashMap.put("size", size);
                    addDonationHashMap.put("lat", location.getLatitude());
                    addDonationHashMap.put("lng", location.getLongitude());

                    firebaseFirestore.collection("donations")
                            .add(addDonationHashMap)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "You add donation successfully", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(this, CheckDonatesActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show());
                } else {
                    Toast.makeText(this, "Please fill all your fields", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "You have a problem with your location", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You have a problem with your location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddDonate:
                addDataToFirestore();
                break;
        }
    }

}
