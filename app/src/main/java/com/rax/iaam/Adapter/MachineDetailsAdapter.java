package com.rax.iaam.Adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.MachineDetailsModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class MachineDetailsAdapter extends RecyclerView.Adapter<MachineDetailsAdapter.MachineAdapterViewHolder> {
    private List<MachineDetailsModel> machineDetailModelList = new ArrayList<>();
    private ItemClickListener callback;

    public MachineDetailsAdapter(ItemClickListener callback) {
        this.machineDetailModelList = machineDetailModelList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MachineAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_machine_details, parent, false);
        return new MachineAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineAdapterViewHolder holder, int position) {
        MachineDetailsModel model = machineDetailModelList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.sNo.setTooltipText(model.getSno());
            holder.sNo.setText(model.getSno());
        }else {
            holder.sNo.setText(model.getSno());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.make.setTooltipText(model.getMake());
            holder.make.setText(model.getMake());
        }else {
            holder.make.setText(model.getMake());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.model.setTooltipText(model.getModel());
            holder.model.setText(model.getModel());
        }else {
            holder.model.setText(model.getModel());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.status.setTooltipText(model.getStatus());
            holder.status.setText(model.getStatus());
        }else {
            holder.status.setText(model.getStatus());
        }

        if (model.getStatus().contains("Commissioned")) {
            holder.status.setTextColor(Color.parseColor("#0F9D58"));
        } else if (model.getStatus().contains("Deactive")) {
            holder.status.setTextColor(Color.parseColor("#DB4437"));
        } else {
            holder.status.setTextColor(Color.parseColor("#DB4437"));
        }

        try {
            String[] sd = model.getOperator();
            StringBuilder stringBuilder
                    = new StringBuilder();
            String comma;
            for (String s:sd){
               stringBuilder.append(s).append(",");
           }
            comma = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.operator.setTooltipText(comma);
                holder.operator.setText(comma);
            }else {
                holder.operator.setText(comma);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return machineDetailModelList.size();
    }

    public void setData(List<MachineDetailsModel> list) {
        if (getItemCount() != 0) {
            machineDetailModelList.clear();
        }
        machineDetailModelList.addAll(list);
        notifyDataSetChanged();
    }


    public class MachineAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, make, model, status, operator;

        public MachineAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.txt_MDs_sn);
            make = itemView.findViewById(R.id.txt_MDs_make);
            model = itemView.findViewById(R.id.txt_MDs_model);
            status = itemView.findViewById(R.id.txt_MDs_status);
            operator = itemView.findViewById(R.id.txt_MDs_operator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnItemClick(machineDetailModelList.get(getAdapterPosition()).getId());
                }
            });
        }

    }
}
