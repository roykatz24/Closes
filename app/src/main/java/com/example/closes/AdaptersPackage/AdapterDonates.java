package com.example.closes.AdaptersPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closes.ModelsPackage.DonatesModel;
import com.example.closes.R;

import java.util.ArrayList;

public class AdapterDonates extends RecyclerView.Adapter<ViewHolderAdapterDonates> {

    private final ArrayList<DonatesModel> donatesModelArrayList;
    private final LayoutInflater mInflater;

    public AdapterDonates(ArrayList<DonatesModel> donatesModelArrayList, Context context) {
        this.donatesModelArrayList = donatesModelArrayList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderAdapterDonates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_donates, parent, false);
        return new ViewHolderAdapterDonates(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderAdapterDonates holder, int position) {
        DonatesModel listData = donatesModelArrayList.get(position);
        holder.tvName.setText(listData.getName());
    }

    @Override
    public int getItemCount() {
        return donatesModelArrayList.size();
    }

}
