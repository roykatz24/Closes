package com.example.closes.PagesPackage;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.closes.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddDonationsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etDescription, etPhone, etSize;
    private Button btnAddDonate;
    private ImageView ivDonation;
    private Uri filePathUri;
    private FirebaseFirestore firebaseFirestore;
    private String name, description, phone, size;
    private final String storagePath = "my_storage";
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    private StorageReference storageReference;
    private final int image_Request_Code = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donations);

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
        ivDonation = findViewById(R.id.ivDonation);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void initListeners() {
        btnAddDonate.setOnClickListener(this);
        ivDonation.setOnClickListener(this);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
    }

    private void addDataToFirestore() {
        if (filePathUri != null) {
            StorageReference storageReference2nd = storageReference.child(storagePath + System.currentTimeMillis() + "." + getFileExtension(filePathUri));
            storageReference2nd.putFile(filePathUri).addOnSuccessListener(taskSnapshot -> storageReference2nd.getDownloadUrl().addOnSuccessListener(uri -> {

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
                            addDonationHashMap.put("image", uri.toString());

                            firebaseFirestore.collection("donations")
                                    .add(addDonationHashMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "You add donation successfully", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(this, CheckDonationsActivity.class);
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
            }));
        } else {
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                ivDonation.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddDonate:
                addDataToFirestore();
                break;
            case R.id.ivDonation:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), image_Request_Code);
                break;
        }
    }

}
