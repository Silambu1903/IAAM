package com.rax.iaam.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.ShiftNewModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class ShiftNewAdpater extends RecyclerView.Adapter<ShiftNewAdpater.ShiftNewAdapterViewHolder> {
    private List<ShiftNewModel> shiftNewModelList = new ArrayList<>();
    private ItemClickListener callback;

    public ShiftNewAdpater(ItemClickListener callback) {
        this.shiftNewModelList = shiftNewModelList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ShiftNewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_shift,parent,false);
        return new ShiftNewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftNewAdapterViewHolder holder, int position) {
        ShiftNewModel model = shiftNewModelList.get(position);
        holder.shiftName.setText(model.getShiftName());
        holder.startTime.setText(model.getStartTime());
        holder.endTime.setText(model.getEndTime());
    }

    @Override
    public int getItemCount() {
        return shiftNewModelList.size();
    }
    public void setData(List<ShiftNewModel>list){
        if (getItemCount()!=0){
            shiftNewModelList.clear();
        }
        shiftNewModelList.addAll(list);
        notifyDataSetChanged();
    }

    public class ShiftNewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView shiftName,startTime,endTime;
        public ShiftNewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftName = itemView.findViewById(R.id.txt_shift_name_value);
            startTime = itemView.findViewById(R.id.txt_shift_login_value);
            endTime = itemView.findViewById(R.id.txt_shift_logout_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnItemClick(shiftNewModelList.get(getAdapterPosition()).getShiftId());
                }
            });
        }

    }
}
