package com.rax.iaam.Adapter.FilterAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.rax.iaam.Callbacks.OnItemClick;
import com.rax.iaam.Model.FilterModel;
import com.rax.iaam.Model.HierarchieModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterAdapterViewHolder> {
    private ArrayList<FilterModel> filterModelList;
    private Context mContext;
    private OnItemClick callback;
    Boolean check;
    int id, ids;
    String abc, a;
    private static final String TAG = "FilterAdapter";
    FilterModel model;
    ArrayList<Integer> checkedIds;
    ArrayList<Integer> filterSiteId;
    ArrayList<Integer> alist;
    public static ArrayList<HierarchieModel> hierarchieListSite;
    ;
    ArrayList<Integer> siteDuplicateRemove;
    public FilterAdapter(OnItemClick callback, ArrayList<FilterModel> filterList, ArrayList<Integer> filterSiteId, ArrayList<HierarchieModel> hierarchieListSite, ArrayList<Integer> siteDuplicateRemove) {
        this.callback = callback;
        this.filterModelList = filterList;
        this.filterSiteId = filterSiteId;
        this.hierarchieListSite = hierarchieListSite;
        this.alist=filterSiteId;
        this.siteDuplicateRemove=siteDuplicateRemove;
    }

    @NonNull
    @Override
    public FilterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new FilterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapterViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder_check:" + filterSiteId.toString()+siteDuplicateRemove.toString());

        model = filterModelList.get(position);
        holder.heading.setText("Site");
        for (HierarchieModel tempModel : model.getList()) {
            Chip chip = new Chip(mContext);
            chip.setText(tempModel.getName());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(mContext,
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Filter);
            chip.setChipDrawable(chipDrawable);
            holder.chipGroup.addView(chip, holder.chipGroup.getChildCount() - 1);
            if (filterSiteId.contains(tempModel.getId())) {
                Log.d(TAG, "onBindViewHolder:" + filterSiteId.toString() + filterSiteId.size());
                chip.setChecked(true);
            } else {
                chip.setChecked(false);
            }

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    chip.setId(tempModel.getId());

                    id = tempModel.getId();
                    callback.OnItemClickSite(id);
//                    if (!alist.contains(id)) {
//                        alist.add(id);
//                    } else {
//                        alist.remove(alist.indexOf(id));
//                    }

                    //check if all chip selected selectall selected  || if single chip unselected then selectall unselected
                      if (alist.size() == hierarchieListSite.size()) {
                        holder.selectAll.setChecked(true);
                    }
                    else {
                        holder.selectAll.setChecked(false);
                    }

                }

            });

        }
        // check for select all when go from block to site

        if (alist.size() == hierarchieListSite.size()) {
            holder.selectAll.setChecked(true);
        } else {
            holder.selectAll.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return filterModelList.size();
    }

    public class FilterAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView heading;
        CheckBox selectAll;
        ChipGroup chipGroup;

        public FilterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.textView69);
            selectAll = itemView.findViewById(R.id.chip8);
            chipGroup = itemView.findViewById(R.id.chipGroup_filter);

            selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectAll.isChecked()) {
                        selectAllChip(false);
                        checkedIds.clear();
                        alist.clear();
                        Log.d(TAG, "onClick:" + checkedIds.toString());
                        Log.d(TAG, "onClick:_alist_"+alist.toString());
                        callback.OnItemClickedSiteid(checkedIds);
                    } else {
                        selectAllChip(true);
                    }
                }

            });

            CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        selectAll.setChecked(false);
                    } else {
                        selectAll.setChecked(true);
                    }
                }
            };

        }

        private void selectAllChip(boolean bool) {
            checkedIds = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);

                for (HierarchieModel tempModel : model.getList()) {
                    chip.setId(tempModel.getId());
                    ids = tempModel.getId();
                    if (!checkedIds.contains(ids)) {
                        checkedIds.add(ids);
                        alist.add(ids);
                    }

                }

                if (!chip.isChecked()) {
                    Log.d(TAG, "onClick: " + i + "is Checked");
                } else {
                    Log.d(TAG, "onClick: " + i + "is Not Checked");
                }
                System.out.println("OnItemClicks1" + checkedIds.toString()+alist.toString());
                callback.OnItemClickedSiteid(checkedIds);
                chip.setChecked(bool);
                Log.d(TAG, "selectAllChip_id:" + model.getList().get(i).getId() + "childsize" + model.getList().size());
            }

        }


    }
}
