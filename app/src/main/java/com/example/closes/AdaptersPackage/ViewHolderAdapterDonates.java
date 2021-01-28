package com.example.closes.AdaptersPackage;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.closes.R;

public class ViewHolderAdapterDonates extends RecyclerView.ViewHolder {

    public TextView tvName;

    public ViewHolderAdapterDonates(View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tvName);
    }

}
