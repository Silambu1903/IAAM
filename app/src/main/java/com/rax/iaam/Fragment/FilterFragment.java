package com.rax.iaam.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.FilterAdapter.FilterAdapter;

import com.rax.iaam.Adapter.FilterAdapter.SectionBlockRecyclerViewAdapter;
import com.rax.iaam.Adapter.FilterAdapter.SectionFloorRecyclerviewAdapter;
import com.rax.iaam.Adapter.FilterAdapter.SectionLineRecyclerviewAdapter;

import com.rax.iaam.Adapter.FilterAdapter.SectionShiftRecyclerviewAdapter;
import com.rax.iaam.Callbacks.OnItemClick;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.Child;
import com.rax.iaam.Model.FilterModel;
import com.rax.iaam.Model.HierarchieModel;
import com.rax.iaam.Model.SectionModel;
import com.rax.iaam.Model.SectionModelShift;
import com.rax.iaam.Model.ShiftChild;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentFilterBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.factoryWS;

import static com.rax.iaam.Others.ApplicationClass.filterChipvalue;
import static com.rax.iaam.Others.ApplicationClass.hierarchies;
import static com.rax.iaam.Others.ApplicationClass.shiftHierarchies;

public class FilterFragment extends Fragment implements OnItemClick, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "FilterFragment";
    String sdate = "", edate = "";
    BaseActivity baseActivity;
    int statuscode;

    public ArrayList<Integer> alist;
    public ArrayList<FilterModel> filterList;
    public static ArrayList<Integer> block_selectall_Siteidlist = new ArrayList<>();
    public static ArrayList<Integer> floor_selectall_Blockidlist = new ArrayList<>();
    public static ArrayList<Integer> line_selectall_Flooridlist = new ArrayList<>();
    public static ArrayList<Integer> shift_selectall_Flooridlist;
    public static ArrayList<Integer> shift_selectall_Shiftidlist;
    public static List<Integer> filterChildId = new ArrayList<>();
    public static List<Integer> filter_floor_ChildIdlist = new ArrayList<>();
    public static List<Integer> filter_line_ChildIdlist = new ArrayList<>();
    public static List<Integer> filter_line_value = new ArrayList<>();
    public static List<Integer> filter_shift_ChildIdlist;
    public static ArrayList<Integer> filterShiftParentId;
    public static ArrayList<HierarchieModel> hierarchieListSite;
    ArrayList<Integer> siteDuplicateRemove;
    ArrayList<String> radioValue;
    private OnItemClick listner;
    private ApplicationClass appClass;
    private LoginNewFragment loginNewFragment;
    private Context context;
    private FragmentFilterBinding binding;
    private FilterAdapter adapter;
    private SectionBlockRecyclerViewAdapter mBlockadapter;
    private SectionFloorRecyclerviewAdapter mFlooradapter;
    private SectionLineRecyclerviewAdapter mLineAdapter;
    private SectionShiftRecyclerviewAdapter mShiftAdapter;
    ArrayList<SectionModel> hierarchieListHeaderBlock;
    public static ArrayList<SectionModel> hierarchieListHeaderFloor;
    public static ArrayList<Integer> shift_headerlist;
    ArrayList<SectionModel> hierarchieListHeaderLine;
    ArrayList<SectionModelShift> hierarchieListHeaderShift;
    ArrayList<Child> hierarchieListChildBlock;
    ArrayList<Child> hierarchieListChildFloor;
    ArrayList<Child> hierarchieListChildLine;
    ArrayList<ShiftChild> hierarchieListChildShift;
    FilterFragment filterFragment;
    String name, headername, name_type;
    public static ArrayList<Integer> filterSiteId;
    public static ArrayList<Integer> filterBlockId;
    public static ArrayList<Integer> filterfloorId;
    public static ArrayList<Integer> filterLineId;
    public static ArrayList<Integer> filterShiftId;
    public static CheckBox chk_all_filter, chk_all_filter_block, Chk_all_filter_floor, check_all_filter_line, chk_all_filter_shift;
    public boolean filteritemClickChange;//moving from block to site
    public boolean FilteritemClickChange_floor;//moving from floor to block
    public boolean FilteriltemClickChange_line;//moving from line to floor
    public boolean FilteriltemClickChange_shift;//moving from shift to line
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_filter, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        Bundle b = getArguments();
        listner = this;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        appClass = (ApplicationClass) getActivity().getApplication();        //site
        baseActivity= (BaseActivity) getActivity();
        binding.rvFilterSite.setLayoutManager(new LinearLayoutManager(context));
        binding.rvFilterBlock.setLayoutManager(new LinearLayoutManager(context));
        binding.rvFilterFloor.setLayoutManager(new LinearLayoutManager(context));
        binding.rvFilterline.setLayoutManager(new LinearLayoutManager(context));
        binding.rvFilterShift.setLayoutManager(new LinearLayoutManager(context));
        binding.dateLayout.setVisibility(View.GONE);
        chk_all_filter = binding.chkAllFilter;
        Chk_all_filter_floor = binding.chkAllFilterFloor;
        check_all_filter_line = binding.chkAllFilterLine;
        chk_all_filter_shift = binding.chkAllFilterShift;
        binding.chkAllFilter.setVisibility(View.GONE);
        filterSiteId = new ArrayList<>();
        alist = new ArrayList<>();
        filterBlockId = new ArrayList<>();
        filterfloorId = new ArrayList<>();
        filterLineId = new ArrayList<>();
        filterShiftId = new ArrayList<>();
        shift_headerlist = new ArrayList<>();
        shift_headerlist = new ArrayList<>();
        filterShiftParentId = new ArrayList<>();
        shift_selectall_Shiftidlist = new ArrayList<>();
        filter_shift_ChildIdlist = new ArrayList<>();
        radioValue = new ArrayList<>();
        getFilterValue();

        if (appClass.NetworkConnected()) {
        } else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(context);
        }
        binding.filterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        binding.selectFilter.setOnCheckedChangeListener(this);
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFilterValue();

                appClass.goBack(getActivity());

            }
        });


        chk_all_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binding.chkAllFilter.isChecked()) {
                        mBlockadapter.selectAll();

                    } else {
                        mBlockadapter.unselectall();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Chk_all_filter_floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binding.chkAllFilterFloor.isChecked()) {
                        mFlooradapter.selectAll();

                    } else {
                        mFlooradapter.unselectall();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        check_all_filter_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binding.chkAllFilterLine.isChecked()) {
                        mLineAdapter.selectAll();

                    } else {
                        mLineAdapter.unselectall();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        chk_all_filter_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (binding.chkAllFilterShift.isChecked()) {
                        mShiftAdapter.selectAll();

                    } else {
                        mShiftAdapter.unselectall();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getFilterValue() {
        String value = preferences.getString("filterObject", "");
        JSONObject jsonObject;
        try {
            if (!value.equals("")) {
                jsonObject = new JSONObject(value);
                sdate = jsonObject.getString("sDate");
                edate = jsonObject.getString("eDate");
                binding.startdatepicker.setText(sdate);
                binding.enddatepicker.setText(edate);
                binding.dateLayout.setVisibility(View.VISIBLE);
                String siteId = jsonObject.getString("filterSiteID").replace("[", "")
                        .replace("]", "").replace(",", "");
                String[] newArr = siteId.split(" ");
                ArrayList<Integer> SavedSiteInt = new ArrayList<>();
                for (int i = 0; i < newArr.length; i++) {
                    SavedSiteInt.add(Integer.parseInt(newArr[i]));
                }
                filterSiteId.addAll(SavedSiteInt);
                String blockId = jsonObject.getString("filterBlockId").replace("[", "")
                        .replace("]", "").replace(",", "");
                String[] blockIdArr = blockId.split(" ");
                ArrayList<Integer> SavedBlockInt = new ArrayList<>();
                for (int i = 0; i < blockIdArr.length; i++) {
                    SavedBlockInt.add(Integer.parseInt(blockIdArr[i]));
                }
                filterBlockId.addAll(SavedBlockInt);
                String floorId = jsonObject.getString("filterFloorId").replace("[", "")
                        .replace("]", "").replace(",", "");
                String[] floorIdArr = floorId.split(" ");
                ArrayList<Integer> SavedFloorInt = new ArrayList<>();
                for (int i = 0; i < floorIdArr.length; i++) {
                    SavedFloorInt.add(Integer.parseInt(floorIdArr[i]));
                }
                filterfloorId.addAll(SavedFloorInt);

                String lineId = jsonObject.getString("filterLineId").replace("[", "")
                        .replace("]", "").replace(",", "");
                String[] lineIdArr = lineId.split(" ");
                ArrayList<Integer> SavedLineInt = new ArrayList<>();
                for (int i = 0; i < lineIdArr.length; i++) {
                    SavedLineInt.add(Integer.parseInt(lineIdArr[i]));
                }
                filterLineId.addAll(SavedLineInt);

                String ShitId = jsonObject.getString("filterShiftId").replace("[", "")
                        .replace("]", "").replace(",", "");

                String[] shiftIdArr = ShitId.split(" ");
                ArrayList<Integer> SavedShiftInt = new ArrayList<>();
                for (int i = 0; i < shiftIdArr.length; i++) {
                    SavedShiftInt.add(Integer.parseInt(shiftIdArr[i]));
                }
                String ShitParentId = jsonObject.getString("filterShiftParentId").replace("[", "")
                        .replace("]", "").replace(",", "");
                System.out.println("the Array shift:" + ShitId);
                String[] shiftParentIdArr = ShitParentId.split(" ");
                ArrayList<Integer> SavedShiftParentInt = new ArrayList<>();
                for (int i = 0; i < shiftParentIdArr.length; i++) {
                    SavedShiftParentInt.add(Integer.parseInt(shiftParentIdArr[i]));
                }
                Log.d(TAG, "getFilterValue:" + SavedShiftParentInt.toString());
                filterShiftId.addAll(SavedShiftInt);
                shift_headerlist.addAll(SavedShiftParentInt);
                filterShiftParentId.addAll(SavedShiftParentInt);
                Log.d(TAG, "getFilterValue:" + filterShiftId.toString() + shift_headerlist.toString());
                String chipId = jsonObject.getString("filterChip").replace("[", "")
                        .replace("]", "").replace(" ","");;
                ArrayList aList = new ArrayList(Arrays.asList(chipId.split(",")));

                radioValue.addAll(aList);
            } else {
                Toast.makeText(context, "Filter is Empty !", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SaveFilterValue() {
        Log.d(TAG, "SaveFilterValue: eda" + sdate + edate + filterSiteId.toString());
        JSONObject filterObject = new JSONObject();
        try {
            filterObject.put("sDate", sdate);
            filterObject.put("eDate", edate);
            filterObject.put("filterSiteID", filterSiteId);
            filterObject.put("filterBlockId", filterBlockId);
            filterObject.put("filterFloorId", filterfloorId);
            filterObject.put("filterLineId", filterLineId);
            filterObject.put("filterShiftId", filterShiftId);
            filterObject.put("filterChip", radioValue);
            filterObject.put("filterShiftParentId", filterShiftParentId);

            System.out.println("the Arraysave shift:" + filterShiftId.size() + filterShiftId.toString() + filterShiftParentId.size() + filterShiftParentId.toString()+radioValue.toString());
            editor = preferences.edit();
            editor.putString(filterChipvalue, filterObject.toString()).apply();
            Log.d(TAG, "SaveFilterValue:" + filterShiftId.toString() + filterShiftParentId.toString() + filterfloorId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int ids = radioGroup.getCheckedRadioButtonId();

        switch (ids) {
            case R.id.filter_date:
                if(binding.filterDate.isChecked()){
                    if(!radioValue.contains(binding.filterDate.getText().toString().trim())){
                        radioValue.add(binding.filterDate.getText().toString());
                    }
                }
                break;
            case R.id.filter_site:
                if(binding.filterSite.isChecked()){
                    if(!radioValue.contains(binding.filterSite.getText().toString().trim())){
                        radioValue.add(binding.filterSite.getText().toString());
                    }
                }
                filteritemClickChange = false;
                if (sdate.isEmpty() && edate.isEmpty()) {
                    appClass.showSnackBar(context, "Select  Date");
                } else {
                    if (appClass.NetworkConnected()) {
                        binding.chkAllFilter.setVisibility(View.VISIBLE);
                        getSite();
                    } else if (!appClass.NetworkConnected()) {
                        appClass.showConnectionLNetworkError(context);
                    }

                }
                break;
            case R.id.filter_block:
                if(binding.filterBlock.isChecked()){
                    if(!radioValue.contains(binding.filterBlock.getText().toString())){
                        radioValue.add(binding.filterBlock.getText().toString());
                    }
                }
                filteritemClickChange = true;
                FilteritemClickChange_floor = false;
                FilteriltemClickChange_line = false;
                FilteriltemClickChange_shift = false;
                if (filterSiteId.isEmpty()) {
                    appClass.showSnackBar(context, "Select SiteID");
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.VISIBLE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.GONE);

                    break;
                } else {
                    Log.d(TAG, "onCheckedChanged:" + filterSiteId.toString() + filterBlockId.toString());
                    if (appClass.NetworkConnected()) {
                        getBlockdDetail(filterSiteId);
                    } else if (!appClass.NetworkConnected()) {
                        appClass.showConnectionLNetworkError(context);
                    }
                    break;
                }
            case R.id.filter_floor:
                if(binding.filterFloor.isChecked()){
                    if(!radioValue.contains(binding.filterFloor.getText().toString().trim())){
                        radioValue.add(binding.filterFloor.getText().toString());
                    }
                }
                FilteritemClickChange_floor = true;
                FilteriltemClickChange_line = false;
                FilteriltemClickChange_shift = false;
                if (filterBlockId.isEmpty()) {
                    appClass.showSnackBar(context, "Select BlockID");
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    binding.chkAllFilterBlock.setVisibility(View.GONE);
                    break;
                } else {
                    Log.d(TAG, "onCheckedChanged:" + filterBlockId.toString());
                    if (appClass.NetworkConnected()) {
                        Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(filterBlockId);
                        filterBlockId.clear();
                        filterBlockId.addAll(listWithoutDuplicates1);
                        getfloorDetail(filterBlockId);
                    } else if (!appClass.NetworkConnected()) {
                        appClass.showConnectionLNetworkError(context);
                    }

                }
                break;
            case R.id.filter_line:
                if(binding.filterLine.isChecked()){
                    if(!radioValue.contains(binding.filterLine.getText().toString().trim())){
                        radioValue.add(binding.filterLine.getText().toString());
                    }
                }
                FilteritemClickChange_floor = false;
                FilteriltemClickChange_line = true;
                FilteriltemClickChange_shift = false;
                if (filterfloorId.isEmpty()) {
                    appClass.showSnackBar(context, "Select FloorID");
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.rvFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.GONE);
                    break;
                } else {
                    if (appClass.NetworkConnected()) {
                        Set<Integer> listWithoutDuplicates1 = new LinkedHashSet<Integer>(filterfloorId);
                        filterfloorId.clear();
                        filterfloorId.addAll(listWithoutDuplicates1);
                        getLineDetail(filterfloorId);
                    } else if (!appClass.NetworkConnected()) {
                        appClass.showConnectionLNetworkError(context);
                    }

                }
                break;
            case R.id.filter_shift:
                if(binding.filterShift.isChecked()){
                    if(!radioValue.contains(binding.filterShift.getText().toString().trim())){
                        radioValue.add(binding.filterShift.getText().toString());
                    }
                }
                FilteritemClickChange_floor = false;
                FilteriltemClickChange_line = false;
                FilteriltemClickChange_shift = true;
                if (filterLineId.isEmpty()) {
                    appClass.showSnackBar(context, "Select LineID");
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.rvFilterFloor.setVisibility(View.GONE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    break;
                } else {
                    Log.d(TAG, "onCheckedChanged:" + filterLineId.toString() + filterLineId.size());
                    if (appClass.NetworkConnected()) {
                        getShiftDetail(filterLineId);
                    } else if (!appClass.NetworkConnected()) {
                        appClass.showConnectionLNetworkError(context);
                    }

                }
                break;
        }


        HashSet<String> hashSetradio = new HashSet<>();
        hashSetradio.addAll(radioValue);
        radioValue.clear();
        radioValue.addAll(hashSetradio);
        System.out.println("the Array is:" + radioValue.toString());
    }


    private void showDate() {
        binding.dateLayout.setVisibility(View.VISIBLE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cldr = Calendar.getInstance();
        DatePickerDialog picker;
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        final Date sdates;
        picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // startdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        sdate = +year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        String startdate = " " + sdate + "";
                        Log.d("hi", "----------->>>>>>" + sdate);
                        Date today = new Date();
                        String abc = dateFormat.format(today);
                        try {
                            if (dateFormat.parse(startdate).after(dateFormat.parse(abc))) {
                                Toast.makeText(context, "Please Select Valid date", Toast.LENGTH_SHORT).show();
                            } else {
                                binding.startdatepicker.setText(sdate);
                                DatePickerDialog pickers = new DatePickerDialog(context,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                edate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                try {
                                                    if (dateFormat.parse(edate).after(dateFormat.parse(abc))) {
                                                        Toast.makeText(context, "Please Select Valid date", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        binding.enddatepicker.setText(edate);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, year, month, day);
                                pickers.setTitle("Select EndDate");
                                pickers.show();
                                if (sdate != null && edate != null) {
                                    binding.dateLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        picker.setTitle("Select StartDate");


        picker.show();
    }

    @Override
    public void OnItemClickedSiteid(ArrayList<Integer> siteid) {
        Log.d(TAG, "OnItemClickedSiteid:" + siteid);
        filterSiteId = siteid;
        Log.d(TAG, "OnItemClickedSiteid:_dell" + filterSiteId.toString());
    }

    @Override
    public void OnItemClickedBlockid(ArrayList<Integer> blockid) {
        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(blockid);
        blockid.clear();
        blockid.addAll(listWithoutDuplicates);
        filterBlockId = blockid;
        Log.d(TAG, "OnItemClickedBlockid: " + filterBlockId.toString());
    }


    @Override
    public void OnItemClickedFloorid(ArrayList<Integer> floorid) {
        Log.d(TAG, "OnItemClickedFloorid:" + floorid);
        filterfloorId = floorid;
    }

    @Override
    public void OnItemClickedLineid(ArrayList<Integer> lineid) {
        Log.d(TAG, "OnItemClickedLineid:" + lineid);
        filterLineId = lineid;
    }


    @Override
    public void OnItemClickSite(int id) {
        Log.d(TAG, "OnItemClickSite:" + id);
        if (!filterSiteId.contains(id)) {
            filterSiteId.add(id);
            if (!alist.containsAll(filterSiteId)) {
                alist.addAll(filterSiteId);
            }


        } else {
            try {
                filterSiteId.remove(Integer.valueOf(id));
                alist.remove(alist.indexOf(filterSiteId));
                Log.d(TAG, "OnItemClickSite_r:" + filterSiteId.toString());
                for (int i = 0; i < hierarchieListHeaderBlock.size(); i++) {
                    if (hierarchieListHeaderBlock.get(i).getId() == id) {
                        ArrayList<Child> blockRemove = new ArrayList<>();
                        blockRemove.addAll(hierarchieListHeaderBlock.get(i).getItemArrayList());
                        for (int j = 0; j < blockRemove.size(); j++) {
                            if (filterChildId.contains(blockRemove.get(j).getId())) {
                                int blockremovepos = filterChildId.indexOf(blockRemove.get(j).getId());
                                filterChildId.remove(blockremovepos);

                            } else {
                                filterChildId.clear();
                            }
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void OnItemClickedShiftid(ArrayList<Integer> shiftid, ArrayList<Integer> shift_parent_id) {
        Log.d(TAG, "OnItemClickedShiftid:" + shiftid.toString() + shift_parent_id.toString());

        filterShiftId = shiftid;

        filterShiftParentId = shift_parent_id;


    }

    @Override
    public void OnItemClickedcheckallblock(ArrayList<Integer> blockid) {
        Log.d(TAG, "OnItemClickedcheckallblock:" + blockid + filterBlockId);
        if (filterSiteId.size() == blockid.size()) {
            chk_all_filter.setChecked(true);
        } else {
            chk_all_filter.setChecked(false);
        }
    }

    @Override
    public void OnItemClickedcheckallfloor(ArrayList<Integer> floorid) {
        Log.d(TAG, "OnItemClickedcheckallfloor:" + floorid + filterBlockId);
        Set<Integer> listWithoutDuplicates = new LinkedHashSet<Integer>(filterBlockId);
        filterBlockId.clear();
        filterBlockId.addAll(listWithoutDuplicates);
        if (filterBlockId.size() == floorid.size()) {
            Chk_all_filter_floor.setChecked(true);

        } else {
            Chk_all_filter_floor.setChecked(false);
        }
    }

    @Override
    public void OnItemClickedcheckallLine(ArrayList<Integer> lineid) {
        Log.d(TAG, "OnItemClickedcheckallLine:" + lineid);
        int aline_id;
        for (int i = 0; i < hierarchieListHeaderLine.size(); i++) {
            aline_id = hierarchieListHeaderLine.get(i).getId();

            if (!filter_line_value.contains(aline_id)) {
                filter_line_value.add(aline_id);
            }
            Log.d(TAG, "OnItemClickedcheckallLine:" + aline_id);
        }

        //   if (hierarchieListHeaderLine.size() == lineid.size()) {
        if (lineid.containsAll(filter_line_value)) {
            check_all_filter_line.setChecked(true);
        } else {
            check_all_filter_line.setChecked(false);
        }
    }

    @Override
    public void OnItemClickedcheckallshift(ArrayList<Integer> shiftid) {

        if (shiftid.size() == hierarchieListHeaderShift.size()) {
            chk_all_filter_shift.setChecked(true);
        } else {
            chk_all_filter_shift.setChecked(false);
        }
    }

    private void getSite() {
        try {
            String machinesApi = factoryWS + "?" + "access_token=" + accessToken;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, machinesApi, null, Request.Method.GET, new VolleyCallback() {

                @SuppressLint("NewApi")
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d("hi", "getMachineDetails:acesstoken" + object);
                    binding.rvFilterSite.setVisibility(View.VISIBLE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    binding.chkAllFilterBlock.setVisibility(View.GONE);
                    binding.chkAllFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilterLine.setVisibility(View.GONE);
                    binding.chkAllFilterShift.setVisibility(View.GONE);
                    binding.rvFilterFloor.setVisibility(View.GONE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.GONE);

                    hierarchieListSite = new ArrayList<>();
                    filterList = new ArrayList<>();
                    siteDuplicateRemove = new ArrayList<>();
                    String sname;
                    int id;
                    int temp;
                    JSONObject jsonObject;
                    binding.progressCircular.setVisibility(View.GONE);

                    try {
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            sname = jsonObject.getString("site_name");
                            id = jsonObject.getInt("site_id");
                            if (!siteDuplicateRemove.contains(id)) {
                                siteDuplicateRemove.add(id);
                                hierarchieListSite.add(new HierarchieModel(id, sname));
                            }
                        }
                        Log.d(TAG, "OnSuccess:" + siteDuplicateRemove.size());
                        filterList.add(new FilterModel(hierarchieListSite));

                        setData(filterList);

                    } catch (Exception e) {
                        appClass.showSnackBar(context, "Error occurred");
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {

                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, 5000);
        } catch (Exception e) {
            appClass.showSnackBar(context, "Error occurred");
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.selectFilter.setOnCheckedChangeListener(this);
    }

    private void setData(ArrayList<FilterModel> filterList) {
        adapter = new FilterAdapter(this, filterList, filterSiteId, hierarchieListSite, siteDuplicateRemove);
        binding.rvFilterSite.setAdapter(adapter);
    }

    public void getBlockdDetail(List<Integer> SiteId) {
        try {
            Log.d(TAG, "getBlockdDetail:" + SiteId.toString());
            String index = "hierarchy_id[]=", formedString = "";
            for (int i = 0; i < SiteId.size(); i++) {
                if (i < SiteId.size()) {
                    formedString = formedString + index + SiteId.get(i) + "&";
                } else {
                    formedString = formedString + index + SiteId.get(i);
                }
            }
            String machinesApi = hierarchies + formedString + "access_token=" + accessToken;
            Log.d(TAG, "getBlockdDetail:" + formedString);
            Log.d(TAG, "getBlockdDetail:" + machinesApi);
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @SuppressLint("NewApi")
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccessBlock:" + object);
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.VISIBLE);
                    binding.rvFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilterBlock.setVisibility(View.GONE);
                    binding.chkAllFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilterLine.setVisibility(View.GONE);
                    binding.chkAllFilterShift.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.VISIBLE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.GONE);
                    hierarchieListHeaderBlock = new ArrayList<>();
                    List<Integer> filterBlockId_temp = new ArrayList<>();
                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = object.getJSONArray("hierarchies");
                        JSONArray jsonArray1 = object.getJSONArray("root");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                            name = jsonObject.getString("name");
                            int root_id = jsonObject.getInt("id");
                            hierarchieListChildBlock = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String names = jsonObject1.getString("name");
                                int id = jsonObject1.getInt("id");
                                int parent_hiherachy = jsonObject1.getInt("parent_hierarchy_id");
                                if (root_id == parent_hiherachy) {
                                    hierarchieListChildBlock.add(new Child(id, names));
                                }
                                if (filterBlockId != null && filterBlockId.size() > 0 && filterBlockId.contains(id)) {
                                    filterBlockId_temp.add(id);
                                }
                            }
                            hierarchieListHeaderBlock.add(new SectionModel(root_id, name, hierarchieListChildBlock));

                        }
                        if (filterBlockId != null && filterBlockId.size() > 0)
                            filterBlockId.clear();
                        filterBlockId = new ArrayList<>();
                        filterBlockId.addAll(filterBlockId_temp);
                        mBlockadapter = new SectionBlockRecyclerViewAdapter(context, hierarchieListHeaderBlock, filteritemClickChange, listner, filterBlockId, hierarchieListChildBlock, filterBlockId, filterSiteId);
                        binding.rvFilterBlock.setAdapter(mBlockadapter);
                    } catch (Exception e) {
                        appClass.showSnackBar(context, "Error occurred");
                        e.printStackTrace();
                    }
                    mBlockadapter.setListner(filterFragment);
                }

                @Override
                public void OnFailure(VolleyError error) {

                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5000);
        } catch (Exception e) {
            appClass.showSnackBar(context, "Error occurred");
            e.printStackTrace();
        }

    }

    public void getfloorDetail(List<Integer> floorId) {
        try {
            String index = "hierarchy_id[]=", formedString = "";
            for (int i = 0; i < floorId.size(); i++) {
                if (i < floorId.size()) {
                    formedString = formedString + index + floorId.get(i) + "&";
                } else {
                    formedString = formedString + index + floorId.get(i);
                }
            }
            String machinesApi = hierarchies + formedString + "access_token=" + accessToken;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @SuppressLint("NewApi")
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccessfloor:" + object);
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.rvFilterFloor.setVisibility(View.VISIBLE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    binding.chkAllFilterFloor.setVisibility(View.VISIBLE);
                    binding.chkAllFilterShift.setVisibility(View.GONE);
                    binding.chkAllFilterLine.setVisibility(View.GONE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.GONE);
                    hierarchieListHeaderFloor = new ArrayList<>();
                    List<Integer> filterBlockId_temp = new ArrayList<>();
                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = object.getJSONArray("hierarchies");
                        JSONArray jsonArray1 = object.getJSONArray("root");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                name = jsonObject.getString("name");
                                int root_id = jsonObject.getInt("id");
                                hierarchieListChildFloor = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    String names = jsonObject1.getString("name");
                                    int id = jsonObject1.getInt("id");
                                    String name_type = jsonObject1.getString("full_hierarchy_level");
                                    int parent_hiherachy = jsonObject1.getInt("parent_hierarchy_id");
                                    if (root_id == parent_hiherachy) {
                                        headername = name_type.substring(0, name_type.lastIndexOf(" -"));
                                        hierarchieListChildFloor.add(new Child(id, names));
                                    }
                                    if (filterfloorId != null && filterfloorId.size() > 0 && filterfloorId.contains(id)) {
                                        filterBlockId_temp.add(id);
                                    }
                                }
                                hierarchieListHeaderFloor.add(new SectionModel(root_id, headername, hierarchieListChildFloor));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (filterfloorId != null && filterfloorId.size() > 0)
                            filterfloorId.clear();
                        filterfloorId = new ArrayList<>();
                        filterfloorId.addAll(filterBlockId_temp);
                        mFlooradapter = new SectionFloorRecyclerviewAdapter(context, hierarchieListHeaderFloor, FilteritemClickChange_floor, listner, filterfloorId, filterfloorId);
                        binding.rvFilterFloor.setAdapter(mFlooradapter);

                        mFlooradapter.setListner(filterFragment);
                    } catch (Exception e) {
                        appClass.showSnackBar(context, "Error occurred");
                        e.printStackTrace();
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5000);
        } catch (Exception e) {
            appClass.showSnackBar(context, "Error occurred");
            e.printStackTrace();
        }
    }

    public void getLineDetail(List<Integer> lineId) {
        try {
            String index = "hierarchy_id[]=", formedString = "";
            for (int i = 0; i < lineId.size(); i++) {
                if (i < lineId.size()) {
                    formedString = formedString + index + lineId.get(i) + "&";
                } else {
                    formedString = formedString + index + lineId.get(i);
                }
            }
            String machinesApi = hierarchies + formedString + "access_token=" + accessToken;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @SuppressLint("NewApi")
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccessLine:" + object);
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.rvFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    binding.chkAllFilterBlock.setVisibility(View.GONE);
                    binding.chkAllFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilterLine.setVisibility(View.VISIBLE);
                    binding.chkAllFilterShift.setVisibility(View.GONE);
                    binding.rvFilterline.setVisibility(View.VISIBLE);
                    binding.rvFilterShift.setVisibility(View.GONE);
                    hierarchieListHeaderLine = new ArrayList<>();
                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        JSONArray jsonArray = object.getJSONArray("hierarchies");
                        JSONArray jsonArray1 = object.getJSONArray("root");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                            name = jsonObject.getString("name");
                            int root_id = jsonObject.getInt("id");
                            hierarchieListChildLine = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String names = jsonObject1.getString("name");
                                int id = jsonObject1.getInt("id");
                                name_type = jsonObject1.getString("full_hierarchy_level");
                                int parent_hiherachy = jsonObject1.getInt("parent_hierarchy_id");
                                if (root_id == parent_hiherachy) {
                                    headername = name_type.substring(0, name_type.lastIndexOf(" -"));
                                    hierarchieListChildLine.add(new Child(id, names));
                                }
                            }
                            if (hierarchieListChildLine.size() > 0) {
                                hierarchieListHeaderLine.add(new SectionModel(root_id, headername, hierarchieListChildLine));
                            }
                        }
                        Log.d(TAG, "OnSuccess_child:" + hierarchieListChildLine.size() + hierarchieListHeaderLine.size());
                        mLineAdapter = new SectionLineRecyclerviewAdapter(context, hierarchieListHeaderLine, FilteriltemClickChange_line, listner, hierarchieListChildLine, filterLineId);
                        binding.rvFilterline.setAdapter(mLineAdapter);


                    } catch (Exception e) {
                        appClass.showSnackBar(context, "Error occurred");
                        e.printStackTrace();
                    }

                    mLineAdapter.setListner(filterFragment);
                }

                @Override
                public void OnFailure(VolleyError error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5000);
        } catch (Exception e) {
            appClass.showSnackBar(context, "Error occurred");
            e.printStackTrace();
        }
    }

    public void getShiftDetail(List<Integer> shiftid) {
        try {
            String index = "hierarchy_id[]=", formedString = "";
            for (int i = 0; i < shiftid.size(); i++) {
                if (i < shiftid.size()) {
                    formedString = formedString + index + shiftid.get(i) + "&";
                } else {
                    formedString = formedString + index + shiftid.get(i);
                }
            }
            String ShiftApi = shiftHierarchies + "shifts?" + formedString + accessTokenKey + accessToken;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, ShiftApi, null, Request.Method.GET, new VolleyCallback() {
                @SuppressLint("NewApi")
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccessShift:" + object);
                    binding.rvFilterSite.setVisibility(View.GONE);
                    binding.rvFilterBlock.setVisibility(View.GONE);
                    binding.chkAllFilter.setVisibility(View.GONE);
                    binding.chkAllFilterBlock.setVisibility(View.GONE);
                    binding.chkAllFilterFloor.setVisibility(View.GONE);
                    binding.chkAllFilterLine.setVisibility(View.GONE);
                    binding.chkAllFilterShift.setVisibility(View.VISIBLE);
                    binding.rvFilterFloor.setVisibility(View.GONE);
                    binding.rvFilterline.setVisibility(View.GONE);
                    binding.rvFilterShift.setVisibility(View.VISIBLE);
                    hierarchieListHeaderShift = new ArrayList<SectionModelShift>();
                    binding.progressCircular.setVisibility(View.GONE);
                    hierarchieListChildShift = new ArrayList<ShiftChild>();

                    try {
                        JSONArray jsonArray = object.getJSONArray("hierarchies");
                        JSONArray jsonArray1 = object.getJSONArray("root");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                            name = jsonObject.getString("name");
                            int root_id = jsonObject.getInt("id");
                            hierarchieListChildShift = new ArrayList<ShiftChild>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String names = jsonObject1.getString("name");
                                int id = jsonObject1.getInt("id");
                                int parent_hiherachy = jsonObject1.getInt("parent_hierarchy_id");
                                if (root_id == parent_hiherachy) {
                                    headername = jsonObject1.getString("full_hierarchy_level");
                                    hierarchieListChildShift.add(new ShiftChild(id, names, parent_hiherachy));
                                }
                            }
                            Log.d(TAG, "OnSuccess:");
                            hierarchieListHeaderShift.add(new SectionModelShift(root_id, headername, hierarchieListChildShift));
                        }
                        Log.d(TAG, "OnSuccess:" + hierarchieListChildShift.toString());
                        mShiftAdapter = new SectionShiftRecyclerviewAdapter(context, hierarchieListHeaderShift, FilteriltemClickChange_shift, listner, filterShiftId, filterShiftId, shift_headerlist);
                        binding.rvFilterShift.setAdapter(mShiftAdapter);


                    } catch (Exception e) {
                        appClass.showSnackBar(context, "Error occurred");
                        e.printStackTrace();
                    }

                    mShiftAdapter.setListner(filterFragment);
                }

                @Override
                public void OnFailure(VolleyError error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    try {
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                            baseActivity.TokenInvalid();

                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5000);

        } catch (Exception e) {
            appClass.showSnackBar(context, "Error occurred");
            e.printStackTrace();
        }

    }
}
