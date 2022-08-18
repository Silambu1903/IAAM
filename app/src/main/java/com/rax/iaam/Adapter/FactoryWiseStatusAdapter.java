package com.rax.iaam.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.FactoryWiseStatusModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class FactoryWiseStatusAdapter extends RecyclerView.Adapter<FactoryWiseStatusAdapter.FactoryWiseStatusViewHolder> {
    private ItemClickListener clickListener;
    private List<FactoryWiseStatusModel> factoryWiseStatusModelList;

    public FactoryWiseStatusAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
        factoryWiseStatusModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FactoryWiseStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fcatorywisestatus_tabel,parent,false);
        return new FactoryWiseStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactoryWiseStatusViewHolder holder, int position) {
        FactoryWiseStatusModel model=factoryWiseStatusModelList.get(position);
        holder.site.setText(model.getSite());
        holder.block.setText(model.getBlock());
        holder.noOfMachines.setText(Integer.toString(model.getNoOfMachines()));
        holder.mom.setText(model.getMom());
        if(holder.mom.getText().toString().contains("-")){
            holder.mom.setTextColor(Color.parseColor("#DB4437"));
        }else if(holder.mom.getText().toString().contains("+")){
            holder.mom.setTextColor(Color.parseColor("#0F9D58"));
        }
    }

    @Override
    public int getItemCount() {
        return factoryWiseStatusModelList.size();
    }
    public void setData(List<FactoryWiseStatusModel> list){
        if(factoryWiseStatusModelList.size()!=0){
            factoryWiseStatusModelList.clear();
        }
        factoryWiseStatusModelList.addAll(list);
        notifyDataSetChanged();
    }

    public class FactoryWiseStatusViewHolder extends RecyclerView.ViewHolder{
        private TextView site,block,noOfMachines,mom;
        public FactoryWiseStatusViewHolder(@NonNull View itemView) {
            super(itemView);

            site=itemView.findViewById(R.id.txt_FWS_site);
            block=itemView.findViewById(R.id.txt_FWS_block);
            noOfMachines=itemView.findViewById(R.id.txt_FWS_machines);
            mom=itemView.findViewById(R.id.txt_FWS_mom);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnItemClick(getAdapterPosition());
                }
            });
        }
    }
}
