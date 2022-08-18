package com.rax.iaam.Adapter.FilterAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Callbacks.Callcheck;
import com.rax.iaam.Callbacks.OnItemClick;
import com.rax.iaam.Fragment.FilterFragment;
import com.rax.iaam.Model.SectionModel;
import com.rax.iaam.R;


import static com.rax.iaam.Fragment.FilterFragment.block_selectall_Siteidlist;
import static com.rax.iaam.Fragment.FilterFragment.filterBlockId;
import static com.rax.iaam.Fragment.FilterFragment.filter_floor_ChildIdlist;
import static com.rax.iaam.Fragment.FilterFragment.floor_selectall_Blockidlist;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SectionFloorRecyclerviewAdapter extends RecyclerView.Adapter<SectionFloorRecyclerviewAdapter.SectionViewHolder> {
    private Context mcontext;
    ItemFloorRecyclerviewAdapter adapter;
    private ArrayList<SectionModel> sectionModelArrayList;
    private OnItemClick callback;
    public static final String TAG = "SectionFloorAdapter";
    FilterFragment filterFragment;
    public ArrayList<Integer> filterfloorId;
    private boolean FilteritemClickChange_floor;
    SectionModel sectionModel;
    public boolean isSelectedAll;
    public static ArrayList<Integer> filter_floor_ChildIdlist;
    public static ArrayList<Integer> b;

    public SectionFloorRecyclerviewAdapter(Context mcontext, ArrayList<SectionModel> sectionModelArrayList, boolean FilteritemClickChange_floor, OnItemClick callback, ArrayList<Integer> filterfloorId, ArrayList<Integer> filter_floor_ChildIdlist) {
        this.mcontext = mcontext;
        this.sectionModelArrayList = sectionModelArrayList;
        this.callback = callback;
        this.FilteritemClickChange_floor = FilteritemClickChange_floor;
        this.filterfloorId = filterfloorId;
        this.filter_floor_ChildIdlist = filter_floor_ChildIdlist;

    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        sectionModel = sectionModelArrayList.get(position);
        if (filter_floor_ChildIdlist.size() > 0) {
            Set<Integer> hashSet = new LinkedHashSet(filter_floor_ChildIdlist);
            filter_floor_ChildIdlist = new ArrayList(hashSet);
        }
        holder.bind(sectionModel, position);
    }


    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }

    public void setData(List<SectionModel> list) {
        if (getItemCount() != 0) {
            sectionModelArrayList.clear();
        }
        sectionModelArrayList.addAll(list);
        notifyDataSetChanged();
    }

    public void selectAll() {

        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                filter_floor_ChildIdlist.add(sectionModelArrayList.get(i).getItemArrayList().get(j).getId());
                sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
            }
            floor_selectall_Blockidlist.add(sectionModelArrayList.get(i).getId());
            Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_floor_ChildIdlist);
            filter_floor_ChildIdlist.clear();
            filter_floor_ChildIdlist.addAll(listWithoutDuplicates);
            callback.OnItemClickedFloorid(filter_floor_ChildIdlist);
            Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(floor_selectall_Blockidlist);
            floor_selectall_Blockidlist.clear();
            floor_selectall_Blockidlist.addAll(listWithoutDuplicates1);
        }
        callback.OnItemClickedcheckallfloor(floor_selectall_Blockidlist);
        notifyDataSetChanged();
    }

    public void unselectall() {
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                    floor_selectall_Blockidlist.clear();
                    filter_floor_ChildIdlist.clear();
                }

            }
            callback.OnItemClickedFloorid(filter_floor_ChildIdlist);
            callback.OnItemClickedcheckallfloor(floor_selectall_Blockidlist);
            notifyDataSetChanged();

        }
    }

    public void setListner(FilterFragment filterFragment) {
        this.filterFragment = filterFragment;


    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        private RecyclerView itemRecyclerView;
        CheckBox selectAll;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.sectionheader);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
            selectAll = itemView.findViewById(R.id.chip9);

        }

        public void bind(SectionModel section, int position) {

            if (FilteritemClickChange_floor) {
                selectAll.setChecked(false);
                if (filter_floor_ChildIdlist.size() > 0) {
                    boolean selectall_enable = false;
                    ArrayList<Boolean> selectall_enable_list = new ArrayList<Boolean>();
                    for (int i = 0; i < section.getItemArrayList().size(); i++) {
                        if (filter_floor_ChildIdlist.contains(section.getItemArrayList().get(i).getId())) {
                            section.getItemArrayList().get(i).setChildCheck(true);
                            selectall_enable = true;
                        } else {
                            selectall_enable = false;
                        }
                        selectall_enable_list.add(selectall_enable);
                    }
                    // for single selectall for one site value
                    if (new HashSet<>(selectall_enable_list).size() <= 1 && selectall_enable_list.contains(true)) {
                        floor_selectall_Blockidlist.add(section.getId());
                        selectAll.setChecked(true);
                    } else {
                        if (floor_selectall_Blockidlist.contains(section.getId())) {
                            floor_selectall_Blockidlist.remove(floor_selectall_Blockidlist.indexOf(section.getId()));
                            selectAll.setChecked(false);
                        }

                    }
                }

                else{
                    if (floor_selectall_Blockidlist.contains(section.getId())) {
                        floor_selectall_Blockidlist.remove(floor_selectall_Blockidlist.indexOf(section.getId()));

                        selectAll.setChecked(false);
                    }}
                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(floor_selectall_Blockidlist);
                floor_selectall_Blockidlist.clear();
                floor_selectall_Blockidlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallfloor(floor_selectall_Blockidlist);
                //}
            }
            selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!selectAll.isChecked()) {
                        selectAllChip(section, false, position);
                    } else {
                        selectAllChip(section, true, position);
                    }
                }
            });
            label.setText(section.getSectionLabel());
            itemRecyclerView.setLayoutManager(new GridLayoutManager(mcontext, 2));
            adapter = new ItemFloorRecyclerviewAdapter(mcontext, section.getItemArrayList(), filterFragment, filterfloorId, new Callcheck() {
                @Override
                public void OnItemClickcheck(int pos) {

                    for (int i = 0; i < sectionModelArrayList.size(); i++) {
                        for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                            if (sectionModelArrayList.get(i).getItemArrayList().get(j).getId() == pos) {
                                if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                                    filter_floor_ChildIdlist.remove(filter_floor_ChildIdlist.indexOf(section.getItemArrayList().get(j).getId()));
                                } else {
                                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                                    filter_floor_ChildIdlist.add(section.getItemArrayList().get(j).getId());
                                }
                            }
                        }
                        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_floor_ChildIdlist);
                        filter_floor_ChildIdlist.clear();
                        filter_floor_ChildIdlist.addAll(listWithoutDuplicates);

                    }
                    callback.OnItemClickedFloorid(filter_floor_ChildIdlist);
                    notifyDataSetChanged();
                }
            });
            itemRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(callback);
            adapter.notifyDataSetChanged();
        }

        private void selectAllChip(SectionModel section, boolean bool, int position) {
            if (bool) {
                floor_selectall_Blockidlist.add(section.getId());
                for (int j = 0; j < section.getItemArrayList().size(); j++) {
                    section.getItemArrayList().get(j).setChildCheck(true);
                    filter_floor_ChildIdlist.add(section.getItemArrayList().get(j).getId());
                    Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_floor_ChildIdlist);
                    filter_floor_ChildIdlist.clear();
                    filter_floor_ChildIdlist.addAll(listWithoutDuplicates);
                    callback.OnItemClickedFloorid(filter_floor_ChildIdlist);
                }

                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(floor_selectall_Blockidlist);
                floor_selectall_Blockidlist.clear();
                floor_selectall_Blockidlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallfloor(floor_selectall_Blockidlist);
                notifyDataSetChanged();
            } else {
                if (floor_selectall_Blockidlist.contains(section.getId())) {
                    floor_selectall_Blockidlist.remove(floor_selectall_Blockidlist.indexOf(section.getId()));
                    for (int j = 0; j < section.getItemArrayList().size(); j++) {
                        section.getItemArrayList().get(j).setChildCheck(false);
                        int blockremovepos = filter_floor_ChildIdlist.indexOf(section.getItemArrayList().get(j).getId());
                        filter_floor_ChildIdlist.remove(blockremovepos);
                        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_floor_ChildIdlist);
                        filter_floor_ChildIdlist.clear();
                        filter_floor_ChildIdlist.addAll(listWithoutDuplicates);
                        callback.OnItemClickedFloorid(filter_floor_ChildIdlist);
                    }
                    Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(floor_selectall_Blockidlist);
                    floor_selectall_Blockidlist.clear();
                    floor_selectall_Blockidlist.addAll(listWithoutDuplicates);
                    callback.OnItemClickedcheckallfloor(floor_selectall_Blockidlist);
                    notifyDataSetChanged();
                }
            }

        }

    }
}
