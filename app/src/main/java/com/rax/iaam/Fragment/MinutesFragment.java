package com.rax.iaam.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentMinutesBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.filterChipvalue;
import static com.rax.iaam.Others.ApplicationClass.minutes;

public class MinutesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private ApplicationClass appClass;
    private static final String TAG = "MinutesFragment";
    private FragmentMinutesBinding binding;
    String startDate;
    String endDate ;
    String siteId;
    String blockId;
    String floorId;
    String lineID;
    String shitId;
    String FilterChip;
    ArrayList<String> filterName;
    ChipGroup chipGroup;
    ArrayList<String> heirachyID;
    ArrayList<String> ShiftIDlist;
    private SharedPreferences preferences;
    BaseActivity baseActivity;
    int statuscode;
    MaterialAlertDialogBuilder dialogBuilder;
   androidx.appcompat.app.AlertDialog alertDialog;
    boolean filterhasvalue =true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=DataBindingUtil.inflate(getLayoutInflater(),R.layout.fragment_minutes,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        appClass=(ApplicationClass)getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        mContext=getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        chipGroup = binding.ChipGroup1;
        getFilterValue();
        if (appClass.NetworkConnected()) {
            todayBar(heirachyID,ShiftIDlist,startDate,endDate);
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }

        binding.EpMinutesSwipe.setOnRefreshListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_bn) {
            appClass.navigateTo(getActivity(), R.id.action_minutesFragment_to_filterFragment);
        } else {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.onSupportNavigateUp();

        }
        return true;
    }
    private void setTag(ArrayList<String> filterName) {
        if (filterName != null) {
            String selected_filter_items = filterName.get(0).toString();
            String[] selected_filter_item = selected_filter_items.split(",");

            for (String tagName : selected_filter_item) {
                final Chip chip = new Chip(mContext);
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                chip.setText(tagName);
                chipGroup.addView(chip);
            }


        }
//        binding.horizontalscroll.addView(chipGroup);
//        binding.horizontalscroll.setFillViewport(true);
//        binding.horizontalscroll.setNestedScrollingEnabled(true);


    }

    private void getFilterValue() {
        String value = preferences.getString(filterChipvalue, "");
        heirachyID = new ArrayList<>();
        ShiftIDlist = new ArrayList<>();
        filterName = new ArrayList<>();
        try {
            if (!value.equals("")) {
                JSONObject jsonObject = new JSONObject(value);
                startDate = jsonObject.getString("sDate");
                endDate = jsonObject.getString("eDate");
                siteId = jsonObject.getString("filterSiteID").replace("[", "").replace("]", "").replace(" ","");
                blockId = jsonObject.getString("filterBlockId").replace("[", "").replace("]", "").replace(" ","");
                floorId = jsonObject.getString("filterFloorId").replace("[", "").replace("]", "").replace(" ","");
                lineID = jsonObject.getString("filterLineId").replace("[", "").replace("]", "").replace(" ","");
                shitId = jsonObject.getString("filterShiftId").replace("[", "").replace("]", "").replace(" ","");
                FilterChip = jsonObject.getString("filterChip").replace("[", "").replace("]", "").replace(" ","");

                if (!shitId.isEmpty()) {
                    ShiftIDlist.add(shitId);
                    heirachyID.add(lineID);
                } else if (!lineID.isEmpty()) {
                    heirachyID.add(lineID);
                } else if (!floorId.isEmpty()) {
                    heirachyID.add(floorId);
                } else if (!blockId.isEmpty()) {
                    heirachyID.add(blockId);
                } else if (!siteId.isEmpty()) {
                    heirachyID.add(siteId);
                }
                if (!FilterChip.isEmpty()) {
                    if(!filterName.contains(FilterChip)){
                    filterName.add(FilterChip);}
                }
                setTag(filterName);

                Log.e(TAG, "getFilterValue: " + heirachyID.toString()+shitId.toString()+filterName.toString());
            }else {
                filterhasvalue=false;
                dialogBuilder =  new MaterialAlertDialogBuilder(mContext).setTitle("Filter Status!").
                        setMessage(getString(R.string.filterNoValue))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                appClass.navigateTo(getActivity(),R.id.action_minutesFragment_to_filterFragment);
                            }
                        });
                       alertDialog=dialogBuilder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void todayBar(ArrayList<String>heirachyID,ArrayList<String>shiftIDlist, String startDate, String endDate) {
        try{
            List<Integer> newhierachyIdList = new ArrayList<>();
            List<Integer> newShiftIdList = new ArrayList<>();
            if(heirachyID.size()!=0) {
                String[] newOne = heirachyID.get(0).split(",");
                int j = 0;
                while (j < newOne.length) {
                    newhierachyIdList.add(Integer.parseInt(newOne[j]));
                    j++;
                }
            }else{
                System.out.println("");
            }
            if(shiftIDlist.size()!=0){
                String[] newOneShift = shiftIDlist.get(0).split(",");
                int j = 0;
                while (j < newOneShift.length) {
                    newShiftIdList.add(Integer.parseInt(newOneShift[j]));
                    j++;
                }
            }
            String index = "hierarchy_ids[]=", formedString = "";
            for (int i = 0; i < newhierachyIdList.size(); i++) {
                if (i < newhierachyIdList.size()) {
                    formedString = formedString + index + newhierachyIdList.get(i) + "&";
                } else {
                    formedString = formedString + index + newhierachyIdList.get(i);
                }
            }
            String indexShift = "shift_id[]=", formedStringShift = "";
            for (int i = 0; i < newShiftIdList.size(); i++) {
                if (i < newShiftIdList.size()) {
                    formedStringShift = formedStringShift + indexShift + newShiftIdList.get(i) + "&";
                } else {
                    formedStringShift = formedStringShift + indexShift + newShiftIdList.get(i);
                }
            }
            Log.d(TAG, "todayBar: "+formedString);
            Log.d(TAG, "todayBar: "+formedStringShift);

            String machinesApi = minutes + "?" + formedString+formedStringShift+"access_token=" + accessToken + "&" + "start_date=" + startDate +"&"+ "end_date=" + endDate;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(mContext, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
              //  https://uat-api.esgrobot.com/api/v1/reports/minutes?access_token=AMDr3xj9JBTbgO7vbZtJoWqh_eA2PqzJyE1rWsYsXTw&start_date=2021-2-3&end_date=2021-2-3&hierarchy_ids[]=1&shift_id[]=1
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess_minutes:"+object);
                    if (object.length()!=0){
                        binding.progressCircular.setVisibility(View.GONE);
                        try{
                            JSONObject tempObj = object.getJSONObject("sessions");
                            if (tempObj.length()!=0){
                                String sessions_count= tempObj.getString("sessions_count");
                                if (sessions_count.equalsIgnoreCase("null") ||sessions_count.trim().equals("")){
                                    binding.textView16.setText(" - ");
                                }else {
                                    binding.textView16.setText(sessions_count);
                                }
                                String ontime_percentage= tempObj.getString("ontime_percentage");
                                if (ontime_percentage.equalsIgnoreCase("null")||ontime_percentage.trim().equals("") ||ontime_percentage.equals("NaN")){
                                    binding.textView23.setText(" - ");
                                }else {
                                    binding.textView23.setText(ontime_percentage +"%");
                                }
                                String variance= tempObj.getString("variance");
                                if (variance.equalsIgnoreCase("null")||variance.trim().equals("")){
                                    binding.textView25.setText(" - ");
                                }else {
                                    binding.textView25.setText(variance+"min");
                                }
                                JSONObject tempObj1 = object.getJSONObject("uptime");
                                String minutes_allocated= tempObj1.getString("minutes_allocated");
                                if (minutes_allocated.equalsIgnoreCase("null")||minutes_allocated.trim().equals("")){
                                    binding.textView28.setText(" - ");
                                }else {
                                    binding.textView28.setText(minutes_allocated);
                                }
                                String minutes_actual= tempObj1.getString("minutes_actual");
                                if (minutes_actual.equalsIgnoreCase("null")||minutes_actual.trim().equals("")){
                                    binding.textView31.setText(" - ");
                                }else {
                                    binding.textView31.setText(minutes_actual);
                                }
                                String actual_allocated= tempObj1.getString("actual_allocated");
                                if (actual_allocated.equalsIgnoreCase("null")||actual_allocated.trim().equals("")){
                                    binding.textView33.setText(" - ");
                                }else {
                                    binding.textView33.setText(actual_allocated+"%");
                                }
                            }

                            JSONObject tempObj2 = object.getJSONObject("work_time");
                             if  (tempObj2.length()!=0){
                                 String minutes_allocated= tempObj2.getString("minutes_allocated");
                                if (minutes_allocated.equalsIgnoreCase("null")||minutes_allocated.trim().equals("")){
                                    binding.textView35.setText(" - ");
                                }else {
                                    binding.textView35.setText(minutes_allocated);
                                }
                                 String minutes_actual= tempObj2.getString("minutes_actual");
                                if (minutes_actual.equalsIgnoreCase("null")||minutes_actual.trim().equals("")){
                                    binding.textView38.setText(" - ");
                                }else {
                                    binding.textView38.setText(minutes_actual);
                                }
                                 String actual_allocated= tempObj2.getString("actual_allocated");
                                if (actual_allocated.equalsIgnoreCase("null")||actual_allocated.trim().equals("")){
                                    binding.textView42.setText(" - ");
                                }else {
                                    binding.textView42.setText(actual_allocated+"%");
                                }
                            }
                            JSONObject tempObj3 = object.getJSONObject("break_time");
                            if (tempObj3.length()!=0){
                                String minutes_allocated= tempObj3.getString("minutes_allocated");
                                if (minutes_allocated.equalsIgnoreCase("null")||minutes_allocated.trim().equals("")){
                                    binding.textView44.setText(" - ");
                                }else {
                                    binding.textView44.setText(minutes_allocated);
                                }
                                String minutes_actual= tempObj3.getString("minutes_actual");
                                if (minutes_actual.equalsIgnoreCase("null")||minutes_actual.trim().equals("")){
                                    binding.textView46.setText(" - ");
                                }else {
                                    binding.textView46.setText(minutes_actual);
                                }
                                String actual_allocated= tempObj3.getString("actual_allocated");
                                if (actual_allocated.equalsIgnoreCase("null")||actual_allocated.trim().equals("")){
                                    binding.textView48.setText(" - ");
                                }else {
                                    binding.textView48.setText(actual_allocated+"%");
                                }
                            }
                        }catch (Exception e){
                            binding.progressCircular.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }else {
                        appClass.showSnackBar(mContext, getString(R.string.no_data_server));
                    }
                }
                @Override
                public void OnFailure(VolleyError error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    if (error.networkResponse != null) {
                        statuscode = error.networkResponse.statusCode;
                    }
                    if (statuscode == 401) {
                        baseActivity.TokenInvalid();
                        if(filterhasvalue==false){
                            alertDialog.dismiss();}
                                     }
                    Log.d(TAG, "OnFailure1:" + statuscode);
                }
            }, 5000);
        }catch ( Exception e){
            e.printStackTrace();
            binding.progressCircular.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.EpMinutesSwipe.setRefreshing(false);
                if (appClass.NetworkConnected()) {
                    todayBar(heirachyID,ShiftIDlist,startDate,endDate);
                }
                else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }

            }
        },2000);
    }
}
