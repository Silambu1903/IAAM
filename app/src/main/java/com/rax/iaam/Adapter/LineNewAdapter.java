package com.rax.iaam.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.BlockNewModel;
import com.rax.iaam.Model.NewLineModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;
//edited on 12/12/2020 by silambu
public class LineNewAdapter extends RecyclerView.Adapter<LineNewAdapter.LineNewAdapterViewHolder> {
    private List<NewLineModel>lineModelList=new ArrayList<>();
    private ItemClickListener callback;

    public LineNewAdapter(ItemClickListener callback) {
        this.lineModelList = lineModelList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public LineNewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_new,parent,false);
        return new  LineNewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineNewAdapterViewHolder holder, int position) {
     NewLineModel model = lineModelList.get(position);
     holder.LineName.setText(model.getLineName());

    }
    public void setData(List<NewLineModel> list) {
        if (getItemCount() != 0) {
            lineModelList.clear();
        }
        lineModelList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return lineModelList.size();
    }

    public class LineNewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView LineName;
        public LineNewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            LineName = itemView.findViewById(R.id.txt_line_name_new);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnItemClick(lineModelList.get(getAdapterPosition()).getLineId());
                }
            });
        }
    }
}
