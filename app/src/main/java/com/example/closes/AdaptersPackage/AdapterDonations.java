package com.example.closes.AdaptersPackage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closes.ModelsPackage.DonatesModel;
import com.example.closes.PagesPackage.MapActivity;
import com.example.closes.R;

import java.util.ArrayList;

public class AdapterDonations extends RecyclerView.Adapter<ViewHolderAdapterDonations> {

    private final ArrayList<DonatesModel> donatesModelArrayList;
    private final LayoutInflater mInflater;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;

    public AdapterDonations(ArrayList<DonatesModel> donatesModelArrayList, Context context) {
        this.donatesModelArrayList = donatesModelArrayList;
        mInflater = LayoutInflater.from(context);

        initLocation();
    }

    private void initLocation() {
        locationManager = (LocationManager) mInflater.getContext().getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
    }

    @NonNull
    @Override
    public ViewHolderAdapterDonations onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_donations, parent, false);
        return new ViewHolderAdapterDonations(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderAdapterDonations holder, int position) {
        DonatesModel listData = donatesModelArrayList.get(position);
        holder.tvName.setText(listData.getName());
        holder.tvSize.setText(listData.getSize());
        holder.tvDescription.setText(listData.getDescription());

        double lat1 = listData.getLat();
        double lng1 = listData.getLng();
        if (ActivityCompat.checkSelfPermission(mInflater.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(mInflater.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
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
                String distanceMeShow = "";
                Location locationA = new Location("Point A");
                locationA.setLatitude(lat1);
                locationA.setLongitude(lng1);
                Location locationB = new Location("Point B");
                locationB.setLatitude(location.getLatitude());
                locationB.setLongitude(location.getLongitude());
                distanceMe = locationA.distanceTo(locationB);

                if (distanceMe < 1000) {
                    int dis = (int) (distanceMe);
                    distanceMeShow = "Meters: " + dis;
                } else if (distanceMe >= 1000) {
                    String disM = String.format("%.2f", distanceMe / 1000);
                    distanceMeShow = "Km: " + disM;
                }

                holder.tvDistance.setText(distanceMeShow);
            }
        }

        holder.btnCall.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(mInflater.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                String phone = "tel:" + listData.getPhone();
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
                mInflater.getContext().startActivity(i);
            } else {
                ActivityCompat.requestPermissions((Activity) mInflater.getContext(), new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
        });

        holder.btnPlace.setOnClickListener(v -> {
            Intent intent = new Intent(mInflater.getContext(), MapActivity.class);
            intent.putExtra("dataMap", listData);
            mInflater.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return donatesModelArrayList.size();
    }

}
