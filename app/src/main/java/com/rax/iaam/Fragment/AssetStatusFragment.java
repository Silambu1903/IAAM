package com.rax.iaam.Fragment;

import android.app.AlertDialog;
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
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.AssetStatusPageAdapter;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentAssetStatusBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.assetHeatMap;
import static com.rax.iaam.Others.ApplicationClass.assetStatus;
import static com.rax.iaam.Others.ApplicationClass.endDateKey;
import static com.rax.iaam.Others.ApplicationClass.reports;
import static com.rax.iaam.Others.ApplicationClass.startDateKey;
import static com.rax.iaam.Others.ApplicationClass.yearChipValue;
import static com.rax.iaam.Others.ApplicationClass.yearKey;

public class AssetStatusFragment extends Fragment implements ChipGroup.OnCheckedChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private ApplicationClass appClass;
    private Context context;
    private FragmentAssetStatusBinding binding;
    private AssetStatusPageAdapter pageAdapter;
    private String startDate;
    private String endDate;
    private int currentYear;
    private static final String TAG = "AssetStatusFragment";
    DateFormat dateFormat;
    Calendar calendar;
    String siteId;
    String blockId;
    String FilterChip;
    ArrayList<String> heirachyID;
    ArrayList<String> filterName;
    ChipGroup chipGroup;
    private SharedPreferences preferences;
    boolean firsTime = true;
    BaseActivity baseActivity;
    int statuscode;
    MaterialAlertDialogBuilder dialogBuilder;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean filterhasvalue =true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_asset_status, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        context = getContext();
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        chipGroup = binding.chipGroup;
        init(Calendar.getInstance().get(Calendar.YEAR));
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        getFilterValue();
        //network
        if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, null, startDate, endDate, 0); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }


        binding.assetStatusRefresh.setOnRefreshListener(this);
        binding.bottomDateAssetStatus.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_bn) {
            appClass.navigateTo(getActivity(), R.id.action_assetStatusFragment_to_filterFragment);
        } else {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.onSupportNavigateUp();
        }
        return true;
    }

    private void init(int currentYear) {
        binding.bottomDateAssetStatus.getMenu().setGroupCheckable(0, false, true);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int year = currentYear - 4;
        int i = 1;
        while (year <= currentYear) {
            Chip chip = new Chip(getContext());
            chip.setText(String.valueOf(year));
            chip.setCheckable(true);
            chip.setId(100 + i);
            if (year == currentYear) {
                chip.setChecked(true);
            }
            if (year > currentYear) {
                chip.setText("Custom");
                chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_date, getContext().getTheme()));
                chip.setIconStartPadding(12f);
            }
            binding.heatMapChipGroup.addView(chip);
            i++;
            year++;
        }
        binding.heatMapChipGroup.setOnCheckedChangeListener(this);
        binding.customYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPicker();
            }
        });
    }

    private void getDataFromApi(ArrayList<String> heirachyID, String year, String startDate, String endDate, int mode) {
        List<JSONObject> availCount = new ArrayList<>();
        List<JSONObject> upTimeCount = new ArrayList<>();
        List<JSONObject> utilizationCount = new ArrayList<>();
        try {
            List<Integer> newhierachyIdList = new ArrayList<>();
            if(heirachyID.size()!=0) {
                String[] newOne = heirachyID.get(0).split(",");
                int j = 0;
                while (j < newOne.length) {
                    newhierachyIdList.add(Integer.parseInt(newOne[j]));
                    j++;                }
            }else{
                System.out.println("");
            }
            String api = null;
            String index = "hierarchy_ids[]=", formedString = "";
            for (int i = 0; i < newhierachyIdList.size(); i++) {
                if (i < newhierachyIdList.size()) {
                    formedString = formedString + index + newhierachyIdList.get(i) + "&";
                } else {
                    formedString = formedString + index + newhierachyIdList.get(i);
                }
            }
            if (mode == 0) {
                api = reports + assetStatus + "?" + formedString + accessTokenKey + accessToken + startDateKey + startDate
                        + endDateKey + endDate;
            } else if (mode == 1) {
                api = reports + assetHeatMap + "?" + formedString + accessTokenKey + accessToken + "&"
                         + yearKey + year;
            }
            Log.d(TAG, "Asset Status" + api);
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    if (object != null) {
                        binding.progressCircular.setVisibility(View.GONE);
                        try {
                            JSONArray objAvailHeatMap = object.getJSONArray("availability_heat_map");
                            if (objAvailHeatMap.length() != 0) {
                                for (int i = 0; i < objAvailHeatMap.length(); i++) {
                                    availCount.add(objAvailHeatMap.getJSONObject(i));
                                }
                            }
                            JSONArray objUpTimeHeatMap = object.getJSONArray("uptime_heat_map");
                            if (objUpTimeHeatMap.length() != 0) {
                                for (int i = 0; i < objUpTimeHeatMap.length(); i++) {
                                    upTimeCount.add(objUpTimeHeatMap.getJSONObject(i));
                                }
                            }
                            JSONArray objUtilizationHeatMap = object.getJSONArray("utilisation_heat_map");
                            if (objUtilizationHeatMap.length() != 0) {
                                for (int i = 0; i < objUtilizationHeatMap.length(); i++) {
                                    utilizationCount.add(objUtilizationHeatMap.getJSONObject(i));
                                }
                            } else {
                                appClass.showSnackBar(context, getString(R.string.no_data_server));
                            }
                            tabConfig(availCount, upTimeCount, utilizationCount);

                            if (mode == 0) {
                                String availabity, upTime, utilization;
                                availabity = object.getString("availability");
                                upTime = object.getString("uptime");
                                utilization = object.getString("utilisation");
                                if (availabity.equalsIgnoreCase("null")) {
                                    availabity = String.valueOf(Double.parseDouble("00"));
                                } else {
                                    availabity = String.valueOf(object.getDouble(("availability")));
                                }
                                if (upTime.equalsIgnoreCase("null")) {
                                    upTime = String.valueOf(Double.parseDouble("00"));
                                } else {
                                    upTime = String.valueOf(object.getDouble(("uptime")));
                                }
                                if (utilization.equalsIgnoreCase("null")) {
                                    utilization = String.valueOf(Double.parseDouble("00"));
                                } else {
                                    utilization = String.valueOf(object.getDouble(("utilisation")));
                                }
                               binding.availabilityValue.setText(availabity+"%");
                                binding.uptimeValue.setText(upTime+"%");
                                binding.utilizationValue.setText(utilization+"%");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        appClass.showSnackBar(context, getString(R.string.no_data_server));
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {
                    error.printStackTrace();
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
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tabConfig(List<JSONObject> availCount, List<JSONObject> upTimeCount, List<JSONObject> utilizationCount) {
        try {
            ViewPager pager = (ViewPager) binding.viewPagerAssetStatus;
            pager.invalidate();
            pageAdapter = new AssetStatusPageAdapter(getChildFragmentManager(), binding.tabLayoutAssetStatus.getTabCount(),
                    availCount, upTimeCount, utilizationCount);
            pageAdapter.notifyDataSetChanged();
            pager.setAdapter(pageAdapter);
            binding.tabLayoutAssetStatus.setupWithViewPager(pager, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {
        Chip chip = getView().findViewById(group.getCheckedChipId());
        //year wise filter
        switch (checkedId) {
            case 101:
                if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, chip.getText().toString(), null, null, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case 102:
                if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, chip.getText().toString(), null, null, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
            case 103:
                if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, chip.getText().toString(), null, null, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
            case 104:
                if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, chip.getText().toString(), null, null, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
            case 105:
                if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, chip.getText().toString(), null, null, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
        }
    }
    private void showYearPicker() {
        if (firsTime) {
            yearChipValue = currentYear;
            firsTime = false;
        }
        View alertLayout = getLayoutInflater().inflate(R.layout.heatmap_yearpicker_dialog, null);
        NumberPicker numberPicker = alertLayout.findViewById(R.id.heatMapNumberPicker);
        numberPicker.setMinValue(currentYear - 20);
        numberPicker.setMaxValue(currentYear);
        numberPicker.setValue(yearChipValue);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                yearChipValue = newVal;
            }
        });
        new AlertDialog.Builder(getContext())
                .setTitle("Select Year")
                .setView(alertLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yearChipValue == 0) {
                            Toast.makeText(getContext(), "Something went Wrong !", Toast.LENGTH_SHORT).show();
                        } else {
                            getDataFromApi(heirachyID, String.valueOf(yearChipValue), null, null, 1);
                        }

                        dialog.dismiss();
                        binding.heatMapChipGroup.clearCheck();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDataFromApi(heirachyID, String.valueOf(currentYear), startDate, endDate, 0);

                dialog.dismiss();
            }
        }).setIcon((ResourcesCompat.getDrawable(getResources(), R.drawable.ic_date, getContext().getTheme())))
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.bottomDateAssetStatus.getMenu().setGroupCheckable(0, true, true);
        calendar = Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.date_1:
                Date date = new Date();
                String current = dateFormat.format(date);
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                String current_week = dateFormat.format(calendar.getTime());
              if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, String.valueOf(currentYear), current, current_week, 0); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                return true;
            case R.id.date_2:
                calendar.add(Calendar.MONTH, -1);
                String current_day_month = dateFormat.format(calendar.getTime());
                String current_month = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {   getDataFromApi(heirachyID, String.valueOf(currentYear), current_day_month, current_month, 0); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                return true;
            case R.id.date_3:
                calendar.add(Calendar.MONTH, -3);
                String current_day_byMonth = dateFormat.format(calendar.getTime());
                String three_month = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) { getDataFromApi(heirachyID, String.valueOf(currentYear), current_day_byMonth, three_month, 0); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.assetStatusRefresh.setRefreshing(false);

                if (appClass.NetworkConnected()) {  getDataFromApi(heirachyID, null, startDate, endDate, 0); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                binding.bottomDateAssetStatus.getMenu().setGroupCheckable(0, false, true);
            }
        }, 2000);
    }

    private void setTag(ArrayList<String> filterName) {
        if (filterName != null) {
            String selected_filter_items = filterName.get(0).toString();
            String[] selected_filter_item = selected_filter_items.split(",");

            for (String tagName : selected_filter_item) {
                final Chip chip = new Chip(context);
                int paddingDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()
                );
                chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                chip.setText(tagName);
                chipGroup.addView(chip);
            }


        }

    }

    private void getFilterValue() {
        String value = preferences.getString("filterObject", "");
        heirachyID = new ArrayList<>();
        filterName = new ArrayList<>();
        try {
            if (!value.equals("")) {
                JSONObject jsonObject = new JSONObject(value);
                startDate = jsonObject.getString("sDate");
                endDate = jsonObject.getString("eDate");
                siteId = jsonObject.getString("filterSiteID").replace("[", "").replace("]", "").replace(" ","");
                blockId = jsonObject.getString("filterBlockId").replace("[", "").replace("]", "").replace(" ","");
                FilterChip = jsonObject.getString("filterChip").replace("[", "").replace("]", "").replace(" ","");
                if (!blockId.isEmpty()) {
                    heirachyID.add(blockId);
                } else if (!siteId.isEmpty()) {
                    heirachyID.add(siteId);
                }

                if (!FilterChip.isEmpty()) {
                    filterName.add(FilterChip);
                }
                setTag(filterName);

                Log.e(TAG, "getFilterValue: " + heirachyID.toString());
            }else {
                filterhasvalue=false;
                dialogBuilder =  new MaterialAlertDialogBuilder(context).setTitle("Filter Status!").
                        setMessage(getString(R.string.filterNoValue))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                appClass.navigateTo(getActivity(),R.id.action_assetStatusFragment_to_filterFragment);
                            }
                        });
                alertDialog=dialogBuilder.show();



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
