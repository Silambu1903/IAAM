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

import com.rax.iaam.Callbacks.CallCheckShift;
import com.rax.iaam.Callbacks.Callcheck;
import com.rax.iaam.Callbacks.OnItemClick;
import com.rax.iaam.Fragment.FilterFragment;
import com.rax.iaam.Model.SectionModel;
import com.rax.iaam.Model.SectionModelShift;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.rax.iaam.Fragment.FilterFragment.line_selectall_Flooridlist;
import static com.rax.iaam.Fragment.FilterFragment.shift_headerlist;
import static com.rax.iaam.Fragment.FilterFragment.shift_selectall_Shiftidlist;


public class SectionShiftRecyclerviewAdapter extends RecyclerView.Adapter<SectionShiftRecyclerviewAdapter.SectionViewHolder> {

    private Context mcontext;
    ItemShiftRecyclerviewAdapter adapter;
    private ArrayList<SectionModelShift> sectionModelArrayList;
    private OnItemClick callback;
    private static final String TAG = "SectionShiftAdapter";
    FilterFragment filterFragment;
    public ArrayList<Integer> filterShiftId;
    private boolean FilteriltemClickChange_shift;
    ArrayList<Integer> filter_shift_ChildIdlist;
    SectionModelShift sectionModel;

    private  ArrayList<Integer> filterShiftParentId;
    public SectionShiftRecyclerviewAdapter(Context mcontext, ArrayList<SectionModelShift> sectionModelArrayList, boolean FilteriltemClickChange_shift, OnItemClick callback, ArrayList<Integer> filterShiftId, ArrayList<Integer> filter_shift_ChildIdlist,ArrayList<Integer> filterShiftParentId) {
        this.mcontext = mcontext;
        this.sectionModelArrayList = sectionModelArrayList;
        this.callback = callback;
        this.FilteriltemClickChange_shift = FilteriltemClickChange_shift;
        this.filterShiftId = filterShiftId;
        this.filter_shift_ChildIdlist = filter_shift_ChildIdlist;
        this.filterShiftParentId=filterShiftParentId;
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
   holder.bind(sectionModel, position);
    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }

    public void selectAll() {
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                shift_selectall_Shiftidlist.add(sectionModelArrayList.get(i).getItemArrayList().get(j).getParent_hierarchy_id());
                shift_headerlist.add(sectionModelArrayList.get(i).getItemArrayList().get(j).getParent_hierarchy_id());
                sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                filter_shift_ChildIdlist.add(sectionModelArrayList.get(i).getItemArrayList().get(j).getId());
            }
            callback.OnItemClickedShiftid(filter_shift_ChildIdlist, shift_headerlist);
        }
        Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(shift_selectall_Shiftidlist);
        shift_selectall_Shiftidlist.clear();
        shift_selectall_Shiftidlist.addAll(listWithoutDuplicates1);
        callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);

        notifyDataSetChanged();
    }
    public void unselectall() {
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                    filter_shift_ChildIdlist.clear();
                    shift_headerlist.clear();
                    shift_selectall_Shiftidlist.clear();
                }
            }
            Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_shift_ChildIdlist);
            filter_shift_ChildIdlist.clear();
            filter_shift_ChildIdlist.addAll(listWithoutDuplicates);
            callback.OnItemClickedShiftid(filter_shift_ChildIdlist, shift_headerlist);
            Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(shift_selectall_Shiftidlist);
            shift_selectall_Shiftidlist.clear();
            shift_selectall_Shiftidlist.addAll(listWithoutDuplicates1);
            callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);
            notifyDataSetChanged();
        }

        notifyDataSetChanged();

    }

    public void setData(List<SectionModelShift> list) {
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
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
            selectAll = itemView.findViewById(R.id.chip9);
        }
        public void bind(SectionModelShift section, int position) {
            if (FilteriltemClickChange_shift) {
                    selectAll.setChecked(false);
                    if (filter_shift_ChildIdlist.size() > 0) {
                        Log.d(TAG, "bind:"+filter_shift_ChildIdlist.toString());
                        boolean selectall_enable = false;
                        ArrayList<Boolean> selectall_enable_list = new ArrayList<Boolean>();
                        for (int i = 0; i < section.getItemArrayList().size(); i++) {
                            for (int j = 0; j < filter_shift_ChildIdlist.size(); j++) {
                                try{
                                if (filter_shift_ChildIdlist.get(j).equals(section.getItemArrayList().get(i).getId()) && shift_headerlist.get(j).equals(section.getItemArrayList().get(i).getParent_hierarchy_id())) {
                                    section.getItemArrayList().get(i).setChildCheck(true);
                                                                   }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            if (section.getItemArrayList().get(i).isChildCheck()) {
                                selectall_enable = true;
                            } else {
                                selectall_enable = false;
                            }
                            selectall_enable_list.add(selectall_enable);
                        }

                        if (new HashSet<>(selectall_enable_list).size() <= 1 && selectall_enable_list.contains(true)) {
                            shift_selectall_Shiftidlist.add(section.getId());

                            selectAll.setChecked(true);
                        } else {
                            if (shift_selectall_Shiftidlist.contains(section.getId())) {
                                shift_selectall_Shiftidlist.remove(shift_selectall_Shiftidlist.indexOf(section.getId()));
                                selectAll.setChecked(false);
                            }
                        }
                    }

                    else{
                        if (shift_selectall_Shiftidlist.contains(section.getId())) {
                            shift_selectall_Shiftidlist.remove(shift_selectall_Shiftidlist.indexOf(section.getId()));

                            selectAll.setChecked(false);
                        }}
                Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(shift_selectall_Shiftidlist);
                shift_selectall_Shiftidlist.clear();
                shift_selectall_Shiftidlist.addAll(listWithoutDuplicates1);
                callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);
                callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);


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
                adapter = new ItemShiftRecyclerviewAdapter(mcontext, section.getItemArrayList(), filterFragment, filterShiftId, new CallCheckShift() {
                    @Override
                    public void OnItemClickcheckShift(int pos, int parent_id) {
                        try {
                            for (int i = 0; i < sectionModelArrayList.size(); i++) {
                                for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {

                                    if (sectionModelArrayList.get(i).getItemArrayList().get(j).getId() == pos && sectionModelArrayList.get(i).getId() == parent_id) {

                                        if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {

                                            sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                                            shift_headerlist.remove(shift_headerlist.indexOf(section.getItemArrayList().get(j).getParent_hierarchy_id()));
                                            filter_shift_ChildIdlist.remove(filter_shift_ChildIdlist.indexOf(sectionModelArrayList.get(i).getItemArrayList().get(j).getId()));
                                        } else {
                                            shift_headerlist.add(section.getItemArrayList().get(j).getParent_hierarchy_id());
                                            sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                                            filter_shift_ChildIdlist.add(section.getItemArrayList().get(j).getId());
                                        }
                                    }
                                }

                                callback.OnItemClickedShiftid(filter_shift_ChildIdlist, shift_headerlist);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }

                });
                itemRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(callback);
                adapter.notifyDataSetChanged();




        }


        private void selectAllChip(SectionModelShift section, boolean bool, int position) {
            if (bool) {
                shift_selectall_Shiftidlist.add(section.getId());
                for (int j = 0; j < section.getItemArrayList().size(); j++) {
                    if(!section.getItemArrayList().get(j).isChildCheck()){
                        filter_shift_ChildIdlist.add(section.getItemArrayList().get(j).getId());
                        shift_headerlist.add(section.getItemArrayList().get(j).getParent_hierarchy_id());
                        section.getItemArrayList().get(j).setChildCheck(true);
                    }

                    callback.OnItemClickedShiftid(filter_shift_ChildIdlist,shift_headerlist);
                }
                Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(shift_selectall_Shiftidlist);
                shift_selectall_Shiftidlist.clear();
                shift_selectall_Shiftidlist.addAll(listWithoutDuplicates1);
                callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);
                callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);
                notifyDataSetChanged();
            } else {
                try {
                    if (shift_selectall_Shiftidlist.contains(section.getId())) {
                        shift_selectall_Shiftidlist.remove(shift_selectall_Shiftidlist.indexOf(section.getId()));
                        for (int j = 0; j < section.getItemArrayList().size(); j++) {

                            int blockremovepos = filter_shift_ChildIdlist.indexOf(section.getItemArrayList().get(j).getId());
                            if (section.getItemArrayList().get(j).isChildCheck()) {
                                section.getItemArrayList().get(j).setChildCheck(false);
                                filter_shift_ChildIdlist.remove(blockremovepos);

                                shift_headerlist.remove(shift_headerlist.indexOf(section.getItemArrayList().get(j).getParent_hierarchy_id()));
                            }

                            callback.OnItemClickedShiftid(filter_shift_ChildIdlist, shift_headerlist);
                        }
                        Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(shift_selectall_Shiftidlist);
                        shift_selectall_Shiftidlist.clear();
                        shift_selectall_Shiftidlist.addAll(listWithoutDuplicates1);
                        callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);
                       callback.OnItemClickedcheckallshift(shift_selectall_Shiftidlist);

                        notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }


        }
    }
}
