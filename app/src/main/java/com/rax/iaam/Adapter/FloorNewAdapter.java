package com.rax.iaam.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.BlockNewModel;
import com.rax.iaam.Model.NewFloorModel;
import com.rax.iaam.Model.UserAssignment.FloorModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;
//edited on 11/12/2020 by silambu
public class FloorNewAdapter extends RecyclerView.Adapter<FloorNewAdapter.FloorNewAdapterViewHolder> {
    private List<NewFloorModel> floorModelList = new ArrayList<>();
    private ItemClickListener callback;

    public FloorNewAdapter(ItemClickListener callBack) {
        this.floorModelList = floorModelList;
        this.callback = callBack;
    }

    @NonNull
    @Override
    public FloorNewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_floor, parent, false);
        return new FloorNewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FloorNewAdapterViewHolder holder, int position) {
        NewFloorModel model = floorModelList.get(position);
        holder.SiteName.setText(model.getFloorName());
    }

    @Override
    public int getItemCount() {
        return floorModelList.size();
    }

    public void setData(List<NewFloorModel> list) {
        if (getItemCount() != 0) {
            floorModelList.clear();
        }
        floorModelList.addAll(list);
        notifyDataSetChanged();
    }

    public class FloorNewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView SiteName;

        public FloorNewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            SiteName = itemView.findViewById(R.id.txt_floor_name_new);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnItemClick(floorModelList.get(getAdapterPosition()).getFloorId());
                }
            });
        }
    }

}
