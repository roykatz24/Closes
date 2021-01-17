package com.example.closes.AdaptersPackage;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.closes.R;

public class ViewHolderAdapaterDonates extends RecyclerView.ViewHolder {

    public TextView tvName;

    public ViewHolderAdapaterDonates(View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
    }

}
