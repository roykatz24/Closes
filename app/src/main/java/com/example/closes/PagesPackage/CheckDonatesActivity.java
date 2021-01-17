package com.example.closes.PagesPackage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.closes.R;

public class CheckDonatesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCheckDonates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_donates);

        initUI();
        initListeners();
    }

    private void initUI() {
        btnCheckDonates = findViewById(R.id.btnCheckDonates);
    }

    private void initListeners() {
        btnCheckDonates.setOnClickListener(this);
    }

    private void alertDialogChoosePurpose() {
        String[] purposes = {
                "I want to pick a donation", "I want to add clothes"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please choose your purpose");
        builder.setItems(purposes, (dialog, which) -> {
            if ("I want to pick a donation".equals(purposes[which])) {
                startActivity(new Intent(this, PickDonationActivity.class));
            } else if ("I want to add clothes".equals(purposes[which])) {
                startActivity(new Intent(this, AddDonationActivity.class));
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
