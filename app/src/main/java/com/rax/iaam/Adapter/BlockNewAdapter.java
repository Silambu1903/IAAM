package com.rax.iaam.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.BlockNewModel;
import com.rax.iaam.Model.SiteNewModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class BlockNewAdapter extends RecyclerView.Adapter<BlockNewAdapter.BlockNewAdapterViewHolder> {
    private List<BlockNewModel> blockNewModelList = new ArrayList<>();
    private ItemClickListener callback;
    private static final String TAG = "SiteNewAdapter_asdfg";

    public BlockNewAdapter(ItemClickListener callback) {
        this.blockNewModelList = blockNewModelList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BlockNewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_block, parent, false);
        return new BlockNewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockNewAdapterViewHolder holder, int position) {
        BlockNewModel model = blockNewModelList.get(position);
        holder.BlockName.setText(model.getBlockName());
    }

    @Override
    public int getItemCount() {
        return blockNewModelList.size();
    }

    public void setData(List<BlockNewModel> list) {
        if (getItemCount() != 0) {
            blockNewModelList.clear();
        }
        blockNewModelList.addAll(list);
        notifyDataSetChanged();
    }

    public class BlockNewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView BlockName;

        public BlockNewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            BlockName = itemView.findViewById(R.id.txt_block_new_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnItemClick(blockNewModelList.get(getAdapterPosition()).getBlockID());
                }
            });
        }
    }

}
