package com.rax.iaam.Adapter.FilterAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.rax.iaam.Callbacks.Callcheck;
import com.rax.iaam.Callbacks.OnItemClick;
import com.rax.iaam.Model.Child;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class ItemFloorRecyclerviewAdapter extends RecyclerView.Adapter<ItemFloorRecyclerviewAdapter.ItemViewHolder> {
    private Context mContext;
    private ArrayList<Child> arrayList;
    private OnItemClick callback;
   public static  final String TAG="ItemfloorRecyclerview";
    public ArrayList<Integer> filterfloorId;
    private Callcheck callcheck;
      Chip chip;
    Child child;
    public ItemFloorRecyclerviewAdapter(Context mContext, ArrayList<Child> arrayList, OnItemClick callback,  ArrayList<Integer> filterfloorId,   Callcheck callcheck) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.callback = callback;
        this.filterfloorId= filterfloorId;
        this.callcheck=callcheck;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_layout, parent, false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
      child = arrayList.get(position);
        chip = new Chip(mContext);

        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(mContext,
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Filter);
        chip.setChipDrawable(chipDrawable);
         chip.setText(child.getName());
        chip.setId(child.getId());
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callcheck.OnItemClickcheck(arrayList.get(position).getId());
            }
        });
        holder.chipGroup.addView(chip, holder.chipGroup.getChildCount() - 1);
        if(child.isChildCheck()){
            chip.setChecked(true);
        }

    }
    public void setOnItemClickListener( OnItemClick callback) {
        this.callback = callback;
    }
    public void setData(List<Child> list) {
        if (getItemCount() != 0) {
            arrayList.clear();
        }
        arrayList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ChipGroup chipGroup;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chipGroup = itemView.findViewById(R.id.chipGroup_filter);

            chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup group, int checkedId) {
                    Chip tempChip = group.findViewById(checkedId);
                    if (!tempChip.isChecked()) {
                        group.check(checkedId);

                    } else {
                        tempChip.setChecked(false);
                    }

                }
            });
        }
}}
