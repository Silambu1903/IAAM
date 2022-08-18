package com.rax.iaam.Adapter;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.MachineDetailsModel;
import com.rax.iaam.Model.SiteNewModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class SiteNewAdapter extends RecyclerView.Adapter<SiteNewAdapter.SiteNewAdapterViewHolder> {
    private List<SiteNewModel> siteNewList = new ArrayList<>();
    private ItemClickListener callback;
    private static final String TAG = "SiteNewAdapter_asdfg";

    public SiteNewAdapter(ItemClickListener callback) {
        this.siteNewList = siteNewList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public SiteNewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_recyler_item, parent, false);
        return new SiteNewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteNewAdapterViewHolder holder, int position) {
        SiteNewModel model = siteNewList.get(position);
        holder.siteId.setText(String.valueOf(model.getSiteId()));
        holder.siteName.setText(model.getSiteName());
    }

    @Override
    public int getItemCount() {
        return siteNewList.size();
    }

    public void setData(List<SiteNewModel> list) {
        if (getItemCount() != 0) {
            siteNewList.clear();
        }
        siteNewList.addAll(list);
        notifyDataSetChanged();
    }

    public class SiteNewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView siteId,siteName;

        public SiteNewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            siteId = itemView.findViewById(R.id.txt_site_new_id);
            siteName = itemView.findViewById(R.id.txt_site_new_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnItemClick(siteNewList.get(getAdapterPosition()).getSiteId());
                }
            });
        }


    }
}
