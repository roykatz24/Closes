package com.example.closes.PagesPackage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.closes.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

public class CheckDonationsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCheckDonates;
    private static final String TAG = "closes";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_donations);

        initUI();
        initListeners();
        initLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            Log.i(TAG, "Inside onStart function; requesting permission when permission is not available");
            requestPermissions();
        } else {
            Log.i(TAG, "Inside onStart function; getting location when permission is already available");
            getLastLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();
    }

    private void initUI() {
        btnCheckDonates = findViewById(R.id.btnCheckDonates);
    }

    private void initListeners() {
        btnCheckDonates.setOnClickListener(this);
    }

    // Init the elements of location
    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback();

        createLocationRequest();

        new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
    }

    // Refresh the location
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // Set interval of 5 seconds that refresh the location
        // Set interval of 4 seconds that refresh the location in places that give us better location
        mLocationRequest.setFastestInterval(4000);
        // Set high priority of accuracy of location
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Start the updates of the location
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(CheckDonationsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(CheckDonationsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    // Stop the updates of the location
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    // Check if the permission of the location is granted
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    // Request the permission of location
    private void requestPermissions() {
        Log.i(TAG, "Inside requestPermissions function");
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "**Inside requestPermissions function when shouldProvideRationale = true");
            startLocationPermissionRequest();
        } else {
            Log.i(TAG, "**Inside requestPermissions function when shouldProvideRationale = false");
            startLocationPermissionRequest();
        }
    }

    // Show the dialog of the request permission
    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(CheckDonationsActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    // Get the last location of us
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        location = task.getResult();
                        Log.i(TAG, "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
                    } else {
                        Log.i(TAG, "Error: " + task.getException());
                    }
                });
    }

    private void alertDialogChoosePurpose() {
        String[] purposes = {
                "I want to pick a donation", "I want to add clothes"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose your purpose");
        builder.setItems(purposes, (dialog, which) -> {
            if ("I want to pick a donation".equals(purposes[which])) {
                startActivity(new Intent(this, PickDonationsActivity.class));
            } else if ("I want to add clothes".equals(purposes[which])) {
                startActivity(new Intent(this, AddDonationsActivity.class));
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheckDonates:
                alertDialogChoosePurpose();
                break;
        }
    }

}
