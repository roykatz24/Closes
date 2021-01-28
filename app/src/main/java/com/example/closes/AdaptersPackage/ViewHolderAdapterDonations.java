package com.example.closes.AdaptersPackage;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.closes.R;

public class ViewHolderAdapterDonations extends RecyclerView.ViewHolder {

    public TextView tvName, tvSize, tvDistance, tvDescription;
    public Button btnPlace, btnCall;

    public ViewHolderAdapterDonations(View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
        tvSize = itemView.findViewById(R.id.tvSize);
        tvDistance = itemView.findViewById(R.id.tvDistance);
        tvDescription = itemView.findViewById(R.id.tvDescription);
        btnPlace = itemView.findViewById(R.id.btnPlace);
        btnCall = itemView.findViewById(R.id.btnCall);
    }

}
