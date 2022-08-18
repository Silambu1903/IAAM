package com.rax.iaam.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Model.MaintenanceTrackerModel;
import com.rax.iaam.R;

import java.util.List;

public class MaintenanceTrackerAdapter extends RecyclerView.Adapter<MaintenanceTrackerAdapter.ViewHolder> {

    private Context context;
    List<MaintenanceTrackerModel> modelList;

    public MaintenanceTrackerAdapter(Context context, List<MaintenanceTrackerModel> maintenanceTrackerModelList) {
        this.context = context;
        this.modelList = maintenanceTrackerModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_maintenance_tracker,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt1.setText(modelList.get(position).getRequestId());

        holder.txt2.setText(modelList.get(position).getLocation());
        holder.txt3.setText(modelList.get(position).getDesk());
        holder.txt4.setText(modelList.get(position).getRequestTime());
        holder.txt5.setText(modelList.get(position).getStatus());
        if(modelList.get(position).getStatus().contains("Open")){
            holder.txt5.setTextColor(Color.parseColor("#DB4437"));
        }else{
            holder.txt5.setTextColor(Color.parseColor("#0F9D58"));
        }

        holder.txt6.setText(modelList.get(position).getAssaigned());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       private TextView txt,txt1,txt2,txt3,txt4,txt5,txt6;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.textView);
            txt2 = itemView.findViewById(R.id.textView2);
            txt3 = itemView.findViewById(R.id.textView3);
            txt4 = itemView.findViewById(R.id.textView4);
            txt5 = itemView.findViewById(R.id.textView5);
            txt6 = itemView.findViewById(R.id.textView6);
        }


    }
}
