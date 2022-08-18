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
import com.rax.iaam.Model.Child;
import com.rax.iaam.Model.SectionModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.rax.iaam.Fragment.FilterFragment.block_selectall_Siteidlist;


public class SectionBlockRecyclerViewAdapter extends RecyclerView.Adapter<SectionBlockRecyclerViewAdapter.SectionViewHolder> {
    private Context mcontext;
    ItemBlockRecyclerViewAdapter adapter;
    private ArrayList<SectionModel> sectionModelArrayList;
    private OnItemClick callback;
    private static final String TAG = "SectionRecyclerAdapter";
    FilterFragment filterFragment;
    ArrayList<Integer> filterBlockId;
    ArrayList<Child> hierarchieListChildBlock;
    Map<Integer, ArrayList<Child>> hasMap = new HashMap<>();
    SectionModel sectionModel;
    public boolean isSelectedAll;
    private boolean filteritemClickChange;
    public static ArrayList<Integer> filterChildId;
    ArrayList<Integer> filterSiteId;

    public SectionBlockRecyclerViewAdapter(Context mcontext, ArrayList<SectionModel> hierarchieListHeaderBlock, boolean filteritemClickChange, OnItemClick callback, ArrayList<Integer> filterBlockId,
                                           ArrayList<Child> hierarchieListChildBlock, ArrayList<Integer> filterChildId, ArrayList<Integer> filterSiteId) {
        this.mcontext = mcontext;
        this.sectionModelArrayList = hierarchieListHeaderBlock;
        this.filteritemClickChange = filteritemClickChange;
        this.callback = callback;
        this.filterBlockId = filterBlockId;
        this.hierarchieListChildBlock = hierarchieListChildBlock;
        this.filterChildId = filterChildId;
        this.filterSiteId = filterSiteId;
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
        if (filterChildId.size() > 0) {
            Set<Integer> hashSet = new LinkedHashSet(filterChildId);
            filterChildId = new ArrayList(hashSet);
        }
        holder.bind(sectionModel, position);
    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }

    public void selectAll() {
        // isSelectedAll = true;
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                filterChildId.add(sectionModelArrayList.get(i).getItemArrayList().get(j).getId());
                block_selectall_Siteidlist.add(sectionModelArrayList.get(i).getId());
            }
            Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filterChildId);
            filterChildId.clear();
            filterChildId.addAll(listWithoutDuplicates);
            callback.OnItemClickedBlockid(filterChildId);
            Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(block_selectall_Siteidlist);
            block_selectall_Siteidlist.clear();
            block_selectall_Siteidlist.addAll(listWithoutDuplicates1);

        }
        callback.OnItemClickedcheckallblock(block_selectall_Siteidlist);
        notifyDataSetChanged();
    }

    public void unselectall() {
        //    isSelectedAll = false;
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                    filterChildId.clear();
                    block_selectall_Siteidlist.clear();
                }
            }

            Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filterChildId);
            filterChildId.clear();
            filterChildId.addAll(listWithoutDuplicates);
            callback.OnItemClickedBlockid(filterChildId);
            Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(block_selectall_Siteidlist);
            block_selectall_Siteidlist.clear();
            block_selectall_Siteidlist.addAll(listWithoutDuplicates1);
            callback.OnItemClickedcheckallblock(block_selectall_Siteidlist);
            notifyDataSetChanged();
        }

    }

    public void setData(List<SectionModel> list) {
        if (getItemCount() != 0) {
            sectionModelArrayList.clear();
        }
        sectionModelArrayList.addAll(list);
        notifyDataSetChanged();
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
            selectAll = itemView.findViewById(R.id.chip9);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }

        public void bind(SectionModel section, int position) {
            if (filteritemClickChange) {

                selectAll.setChecked(false);
                if (filterChildId.size() > 0) {
                    boolean selectall_enable = false;
                    ArrayList<Boolean> selectall_enable_list = new ArrayList<Boolean>();
                    for (int i = 0; i < section.getItemArrayList().size(); i++) {
                        if (filterChildId.contains(section.getItemArrayList().get(i).getId())) {
                            section.getItemArrayList().get(i).setChildCheck(true);
                            selectall_enable = true;
                        } else {
                            selectall_enable = false;

                        }
                        selectall_enable_list.add(selectall_enable);
                    }
                    // for single selectall for one site value
                    if (new HashSet<>(selectall_enable_list).size() <= 1 && selectall_enable_list.contains(true)) {
                        block_selectall_Siteidlist.add(section.getId());
                        selectAll.setChecked(true);
                    } else {
                        if (block_selectall_Siteidlist.contains(section.getId())) {
                            block_selectall_Siteidlist.remove(block_selectall_Siteidlist.indexOf(section.getId()));

                            selectAll.setChecked(false);
                        }

                    }
                }
                else{
                    if (block_selectall_Siteidlist.contains(section.getId())) {
                        block_selectall_Siteidlist.remove(block_selectall_Siteidlist.indexOf(section.getId()));

                        selectAll.setChecked(false);
                    }
                }
                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(block_selectall_Siteidlist);
                block_selectall_Siteidlist.clear();
                block_selectall_Siteidlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallblock(block_selectall_Siteidlist);
                //}
            }

            selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectAll.isChecked()) {
                        selectAllChip(section, false, position);
                    } else {
                        selectAllChip(section, true, position);
                    }
                }
            });

            label.setText(section.getSectionLabel());
            itemRecyclerView.setLayoutManager(new GridLayoutManager(mcontext, 2));
            adapter = new ItemBlockRecyclerViewAdapter(mcontext, section.getItemArrayList(), filterFragment, filterBlockId, sectionModelArrayList, new Callcheck() {
                @Override
                public void OnItemClickcheck(int pos) {
                    for (int i = 0; i < sectionModelArrayList.size(); i++) {
                        for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                            if (sectionModelArrayList.get(i).getItemArrayList().get(j).getId() == pos) {
                                if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                                    int blockid = sectionModelArrayList.get(i).getItemArrayList().get(j).getId();
                                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                                    filterChildId.remove(filterChildId.indexOf(blockid));

                                } else {

                                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                                    filterChildId.add(section.getItemArrayList().get(j).getId());

                                }
                            }
                        }
                        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filterChildId);
                        filterChildId.clear();
                        filterChildId.addAll(listWithoutDuplicates);
                    }
                    callback.OnItemClickedBlockid(filterChildId);
                    notifyDataSetChanged();
                }
            });
            itemRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(callback);
            adapter.notifyDataSetChanged();
        }

        private void selectAllChip(SectionModel section, boolean bool, int position) {
            if (bool) {
                block_selectall_Siteidlist.add(section.getId());
                for (int j = 0; j < section.getItemArrayList().size(); j++) {
                    section.getItemArrayList().get(j).setChildCheck(true);
                    filterChildId.add(section.getItemArrayList().get(j).getId());
                    Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filterChildId);
                    filterChildId.clear();
                    filterChildId.addAll(listWithoutDuplicates);
                    callback.OnItemClickedBlockid(filterChildId);
                    Log.d(TAG, "selectAllChip_a: " + filterChildId.toString());
                }
                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(block_selectall_Siteidlist);
                block_selectall_Siteidlist.clear();
                block_selectall_Siteidlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallblock(block_selectall_Siteidlist);
                notifyDataSetChanged();
            } else {
                if (block_selectall_Siteidlist.contains(section.getId())) {
                    block_selectall_Siteidlist.remove(block_selectall_Siteidlist.indexOf(section.getId()));
                    for (int j = 0; j < section.getItemArrayList().size(); j++) {
                        section.getItemArrayList().get(j).setChildCheck(false);
                        int blockremovepos = filterChildId.indexOf(section.getItemArrayList().get(j).getId());
                        filterChildId.remove(blockremovepos);
                        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filterChildId);
                        filterChildId.clear();
                        filterChildId.addAll(listWithoutDuplicates);
                        callback.OnItemClickedBlockid(filterChildId);
                        Log.d(TAG, "selectAllChip_r: " + filterChildId.toString());
                    }
                }
                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(block_selectall_Siteidlist);
                block_selectall_Siteidlist.clear();
                block_selectall_Siteidlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallblock(block_selectall_Siteidlist);
                notifyDataSetChanged();
            }
        }

    }

}
