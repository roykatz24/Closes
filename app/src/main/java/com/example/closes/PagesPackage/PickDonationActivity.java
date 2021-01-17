package com.example.closes.PagesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.closes.AdaptersPackage.AdapterDonates;
import com.example.closes.ModelsPackage.DonatesModel;
import com.example.closes.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PickDonationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDonates;
    private final ArrayList<DonatesModel> donatesModelArrayList = new ArrayList<>();
    private AdapterDonates adapterDonates;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_donation);

        initUI();
        readDonatesFirestore();
    }

    private void initUI() {
        recyclerViewDonates = findViewById(R.id.recyclerViewDonates);

        recyclerViewDonates.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void readDonatesFirestore() {
        firebaseFirestore.collection("donations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donatesModelArrayList.clear();

                        for (DocumentSnapshot doc : task.getResult()) {
                            DonatesModel note = doc.toObject(DonatesModel.class);
                            note.setId(doc.getId());
                            donatesModelArrayList.add(note);
                        }

                        adapterDonates = new AdapterDonates(donatesModelArrayList, this);
                        recyclerViewDonates.setAdapter(adapterDonates);
                    } else {
                        Toast.makeText(PickDonationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
