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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.rax.iaam.Fragment.FilterFragment.block_selectall_Siteidlist;
import static com.rax.iaam.Fragment.FilterFragment.filter_line_ChildIdlist;
import static com.rax.iaam.Fragment.FilterFragment.floor_selectall_Blockidlist;
import static com.rax.iaam.Fragment.FilterFragment.line_selectall_Flooridlist;

public class SectionLineRecyclerviewAdapter extends RecyclerView.Adapter<SectionLineRecyclerviewAdapter.SectionViewHolder> {

    private Context mcontext;
    ItemLineRecyclerviewAdapter adapter;
    private ArrayList<SectionModel> sectionModelArrayList;
    private OnItemClick callback;
    private static final String TAG = "SectionLineAdapter";
    public ArrayList<Integer> filterLineId;
    ArrayList<Integer> getBlockedId = new ArrayList<>();
    private boolean FilteriltemClickChange_line;
    ArrayList<Integer> filter_line_ChildIdlist;
     SectionModel sectionModel;
    ArrayList<Child> hierarchieListChildLine;
    FilterFragment filterFragment;
   SectionViewHolder sectionViewHolder;
    public SectionLineRecyclerviewAdapter(Context mcontext, ArrayList<SectionModel> sectionModelArrayList,  boolean FilteriltemClickChange_line, OnItemClick callback,  ArrayList<Child> hierarchieListChildLine,ArrayList<Integer> filter_line_ChildIdlist) {
        this.mcontext = mcontext;
        this.sectionModelArrayList = sectionModelArrayList;
        this.callback = callback;
        this.FilteriltemClickChange_line=FilteriltemClickChange_line;
        this.filter_line_ChildIdlist=filter_line_ChildIdlist;
        this.hierarchieListChildLine=hierarchieListChildLine;
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
        if (filter_line_ChildIdlist.size() > 0) {
            Set<Integer> hashSet = new LinkedHashSet(filter_line_ChildIdlist);
            filter_line_ChildIdlist = new ArrayList(hashSet);
        }
        holder.bind(sectionModel,position);
    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }
    public void selectAll() {
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
                for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                    filter_line_ChildIdlist.add(sectionModelArrayList.get(i).getItemArrayList().get(j).getId());
                }
            line_selectall_Flooridlist.add(sectionModelArrayList.get(i).getId());
        }
        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_line_ChildIdlist);
        filter_line_ChildIdlist.clear();
        filter_line_ChildIdlist.addAll(listWithoutDuplicates);
        callback.OnItemClickedLineid(filter_line_ChildIdlist);
        Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(line_selectall_Flooridlist);
        line_selectall_Flooridlist.clear();
        line_selectall_Flooridlist.addAll(listWithoutDuplicates1);
        callback.OnItemClickedcheckallLine(line_selectall_Flooridlist);
        notifyDataSetChanged();
    }
    public void unselectall() {
        for (int i = 0; i < sectionModelArrayList.size(); i++) {
            for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                    sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                    filter_line_ChildIdlist.clear();
                    line_selectall_Flooridlist.clear();
                }
            }
            Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_line_ChildIdlist);
            filter_line_ChildIdlist.clear();
            filter_line_ChildIdlist.addAll(listWithoutDuplicates);
            callback.OnItemClickedLineid(filter_line_ChildIdlist);
            Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(line_selectall_Flooridlist);
            line_selectall_Flooridlist.clear();
            line_selectall_Flooridlist.addAll(listWithoutDuplicates1);
            callback.OnItemClickedcheckallLine(line_selectall_Flooridlist);
            notifyDataSetChanged();
        }
        notifyDataSetChanged();

    }
    public void setData(List<SectionModel> list) {
        if (getItemCount() != 0) {
            sectionModelArrayList.clear();
        }
        sectionModelArrayList.addAll(list);
        notifyDataSetChanged();
    }
    public void setListner(FilterFragment filterFragment) {
        this. filterFragment=filterFragment;
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
                if (FilteriltemClickChange_line) {
                selectAll.setChecked(false);
                if (filter_line_ChildIdlist.size() > 0) {
                    boolean selectall_enable = false;
                    ArrayList<Boolean> selectall_enable_list = new ArrayList<Boolean>();
                    for (int i = 0; i < section.getItemArrayList().size(); i++) {
                        if (filter_line_ChildIdlist.contains(section.getItemArrayList().get(i).getId())) {
                            section.getItemArrayList().get(i).setChildCheck(true);
                            selectall_enable = true;
                        } else {
                            selectall_enable = false;
                        }
                        selectall_enable_list.add(selectall_enable);
                    }
                    // for single selectall for one site value
                    if (new HashSet<>(selectall_enable_list).size() <= 1 && selectall_enable_list.contains(true)) {
                        line_selectall_Flooridlist.add(section.getId());
                        selectAll.setChecked(true);
                    } else {
                        if (line_selectall_Flooridlist.contains(section.getId())) {
                            line_selectall_Flooridlist.remove(line_selectall_Flooridlist.indexOf(section.getId()));
                            selectAll.setChecked(false);
                        }

                    }
                }


                else{
                    if (line_selectall_Flooridlist.contains(section.getId())) {
                        line_selectall_Flooridlist.remove(line_selectall_Flooridlist.indexOf(section.getId()));

                        selectAll.setChecked(false);
                    }}
                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(line_selectall_Flooridlist);
                line_selectall_Flooridlist.clear();
                line_selectall_Flooridlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallLine(line_selectall_Flooridlist);
                //}
            }
            selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!selectAll.isChecked()) {
                        selectAllChip(section, false,position);
                    } else {
                        selectAllChip(section, true,position);
                    }
                }
            });

            label.setText(section.getSectionLabel());
            itemRecyclerView.setLayoutManager(new GridLayoutManager(mcontext,2));


            adapter = new ItemLineRecyclerviewAdapter(mcontext, section.getItemArrayList(), filterFragment, new Callcheck() {
                @Override
                public void OnItemClickcheck(int pos) {
                    for (int i = 0; i < sectionModelArrayList.size(); i++) {
                        for (int j = 0; j < sectionModelArrayList.get(i).getItemArrayList().size(); j++) {
                            if (sectionModelArrayList.get(i).getItemArrayList().size() > 0){
                                if (sectionModelArrayList.get(i).getItemArrayList().get(j).getId() == pos) {
                                    if (sectionModelArrayList.get(i).getItemArrayList().get(j).isChildCheck()) {
                                        sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(false);
                                        filter_line_ChildIdlist.remove(filter_line_ChildIdlist.indexOf(section.getItemArrayList().get(j).getId()));
                                    } else {
                                        sectionModelArrayList.get(i).getItemArrayList().get(j).setChildCheck(true);
                                        filter_line_ChildIdlist.add(section.getItemArrayList().get(j).getId());
                                    }
                                }

                        }
                            else{
                                label.setVisibility(View.GONE);
                                selectAll.setVisibility(View.GONE);
                            }
                        }
                        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_line_ChildIdlist);
                        filter_line_ChildIdlist.clear();
                        filter_line_ChildIdlist.addAll(listWithoutDuplicates);

                    }
                    callback.OnItemClickedLineid(filter_line_ChildIdlist);
                    notifyDataSetChanged();
                }
            });
            itemRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(callback);
            adapter.notifyDataSetChanged();
        }


        private void selectAllChip(SectionModel section, boolean bool,int position) {
            if (bool) {
                line_selectall_Flooridlist.add(section.getId());
                for (int j = 0; j < section.getItemArrayList().size(); j++) {
                    section.getItemArrayList().get(j).setChildCheck(true);
                    filter_line_ChildIdlist.add(section.getItemArrayList().get(j).getId());
                    Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_line_ChildIdlist);
                    filter_line_ChildIdlist.clear();
                    filter_line_ChildIdlist.addAll(listWithoutDuplicates);
                    callback.OnItemClickedLineid(filter_line_ChildIdlist);
                }
                Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(line_selectall_Flooridlist);
                line_selectall_Flooridlist.clear();
                line_selectall_Flooridlist.addAll(listWithoutDuplicates);
                callback.OnItemClickedcheckallLine(line_selectall_Flooridlist);
                notifyDataSetChanged();
            }
            else {
                if (line_selectall_Flooridlist.contains(section.getId())) {
                    line_selectall_Flooridlist.remove(line_selectall_Flooridlist.indexOf(section.getId()));
                    for (int j = 0; j < section.getItemArrayList().size(); j++) {
                        section.getItemArrayList().get(j).setChildCheck(false);
                        int blockremovepos = filter_line_ChildIdlist.indexOf(section.getItemArrayList().get(j).getId());
                        filter_line_ChildIdlist.remove(blockremovepos);
                        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filter_line_ChildIdlist);
                        filter_line_ChildIdlist.clear();
                        filter_line_ChildIdlist.addAll(listWithoutDuplicates);
                        callback.OnItemClickedLineid(filter_line_ChildIdlist);
                    }
                    Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(line_selectall_Flooridlist);
                    line_selectall_Flooridlist.clear();
                    line_selectall_Flooridlist.addAll(listWithoutDuplicates);
                    callback.OnItemClickedcheckallLine(line_selectall_Flooridlist);
                    notifyDataSetChanged();
                }
            }

        }
    }
}
