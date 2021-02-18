package com.example.closes.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.closes.ModelsPackage.DonatesModel;
import com.example.closes.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mGoogleMap;
    private MapView mapView;
    private DonatesModel donatesModel;
    private ImageView moovit, gett, waze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initUI();
        initListeners();
    }

    private void initUI() {
        mapView = findViewById(R.id.mapView);
        moovit = findViewById(R.id.imageViewMoovit);
        gett = findViewById(R.id.imageViewGett);
        waze = findViewById(R.id.imageViewWaze);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        donatesModel = (DonatesModel) getIntent().getExtras().getSerializable("dataMap");
    }

    private void initListeners() {
        moovit.setOnClickListener(this);
        gett.setOnClickListener(this);
        waze.setOnClickListener(this);
    }

    private void getMoovit(double des_lat, double des_lng, String name, double orig_lat, double orig_lng) {
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.tranzmate", PackageManager.GET_ACTIVITIES);
            String uri = "moovit://directions?dest_lat=" + des_lat + "&dest_lon=" + des_lng + "&dest_name=" + name + "&orig_lat=" + orig_lat + "&orig_lon=" + orig_lng + "&orig_name=Your current location&auto_run=true&partner_id=ClosesApp";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            String url = "http://app.appsflyer.com/com.tranzmate?pid=DL&c=ClosesApp";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private void getGetTaxi(double des_lat, double des_lng) {
        if (isPackageInstalled(this)) {
            openLinkGetTaxi(this, "gett://order?pickup=my_location&dropoff_latitude=" + des_lat + "&dropoff_longitude=" + des_lng + "&product_id=0c1202f8-6c43-4330-9d8a-3b4fa66505fd");
        } else {
            openLinkGetTaxi(this, "https://play.google.com/store/apps/details?id=" + "com.gettaxi.android");
        }
    }

    private static void openLinkGetTaxi(Activity activity, String link) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
        playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        playStoreIntent.setData(Uri.parse(link));
        activity.startActivity(playStoreIntent);
    }

    private static boolean isPackageInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.gettaxi.android", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    private void getWaze(double des_lat, double des_lng) {
        try {
            String url = "https://www.waze.com/ul?ll=" + des_lat + "%2C" + des_lng + "&navigate=yes&zoom=17";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            MapsInitializer.initialize(this);
            mGoogleMap = googleMap;
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
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
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.771959, 35.217018), 8));
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(donatesModel.getLat(), donatesModel.getLng()))
                    .title(donatesModel.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewMoovit:
                getMoovit(donatesModel.getLat(), donatesModel.getLng(), donatesModel.getName(), 31.771959, 35.217018);
                break;
            case R.id.imageViewGett:
                getGetTaxi(donatesModel.getLat(), donatesModel.getLng());
                break;
            case R.id.imageViewWaze:
                getWaze(donatesModel.getLat(), donatesModel.getLng());
                break;
        }
    }

}
