package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.NeedleTimeIdleBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.endDateKey;
import static com.rax.iaam.Others.ApplicationClass.reports;
import static com.rax.iaam.Others.ApplicationClass.startDateKey;

public class NeedleTimeIdle extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private ApplicationClass appClass;
    private Context context;
    private NeedleTimeIdleBinding binding;
    private static final String TAG = "NeedleTimeIdle";
    String startDate;
    String endDate ;
    String siteId;
    String blockId;
    String floorId;
    String lineID;
    String shitId;
    ArrayList<String> heirachyID;
    ArrayList<String> ShiftIDlist;
    float j;
    String[] floorName;
    BarChart chart;
    public ArrayList NoOfFloors;
    public ArrayList<Integer> value;
    DateFormat dateFormat;
    Calendar calendar;
    private SharedPreferences preferences;
    BaseActivity baseActivity;
    int statuscode;
    MaterialAlertDialogBuilder dialogBuilder;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean filterhasvalue =true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.needle_time_idle, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        context = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        chart = binding.chartNeedleTimeIdle;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getFilterValue();
        //network
        if (appClass.NetworkConnected()) {   getNeedleTimeIDle(heirachyID,ShiftIDlist, startDate, endDate, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }


        floorName = new String[]{"General allowance", "Maintenance", "No input stops", "Needle change stops"};
        binding.needTimeIdleRefresh.setOnRefreshListener(this);
        binding.bottomDateNeedleTimeIdle.getMenu().setGroupCheckable(0, false, true);

        binding.bottomDateNeedleTimeIdle.setOnNavigationItemSelectedListener(this);
    }


    private void createBarChart(BarChart chart, ArrayList xValues, String[] yValues, String legendName) {
        chart.clear();
        chart.invalidate();
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(yValues));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(10);
        xAxis.setEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(8);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        BarDataSet bardataset = new BarDataSet(xValues, legendName);


        ValueFormatter vf = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (float) value;
            }
        };

        bardataset.setValueFormatter(vf);
        bardataset.setValueTextSize(8);
        chart.animateY(1500);
        BarData data = new BarData(bardataset);
        data.setBarWidth(0.7f);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        bardataset.setColors(Color.rgb(123,104,238),Color.rgb(255,69,0),
                Color.rgb(218,165,32),Color.rgb(65,105,225));
        chart.setData(data);
        chart.setVisibleXRangeMaximum(7);
        chart.moveViewToX(1);
        chart.invalidate();
    }

    private void createPieChart(double runTime, double idleTime) {
        PieChart pieChart = binding.chartNeedleStatus;
        pieChart.setBackgroundColor(Color.TRANSPARENT);
        movePieChart(pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setMaxAngle(180f); // HALF CHART
        pieChart.setRotationAngle(180f);
        pieChart.setCenterTextOffset(10, 10);
        pieChart.animateXY(1400, 1400);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(0f);
        l.setYOffset(25f);
        l.setDrawInside(false);
        l.setXOffset(8f);
        l.setTextSize(10f);
        pieChart.setUsePercentValues(true);
        setPieData(2, 100, pieChart, runTime, idleTime); // set Data
    }


    private void setPieData(int count, float range, PieChart pieChart, double runTime, double idleTime) {
        pieChart.invalidate();
        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry((float) runTime, "Run Time"));
        values.add(new PieEntry((float) idleTime, "Idle Time"));
        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.rgb(0,100,0), Color.rgb(218,165,32));
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setData(data);
        pieChart.setNoDataText("");

    }

    private void movePieChart(PieChart pieChart) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int offset = (int) (height * 0.38); /* percent to move */
        LinearLayout.LayoutParams rlParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        rlParams.setMargins(0, 0, 0, -offset);
        rlParams.gravity = Gravity.BOTTOM;
        pieChart.setLayoutParams(rlParams);

    }


    private void getNeedleTimeIDle(ArrayList<String>heirachyID,ArrayList<String>shiftIDlist, String startDate, String endDate, int mode) {
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
        Log.d(TAG, "getNeedleTimeIDle:"+formedString+formedStringShift);
        try {
            String api = null;
            if (mode == 0) {
                api = reports + "idletime_details?"+formedString+formedStringShift+accessTokenKey + accessToken ;
            } else if (mode == 1) {
                api = reports + "idletime_details?"+formedString+formedStringShift+accessTokenKey+ accessToken + startDateKey + startDate + endDateKey + endDate ;
            }
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess_needleidle"+object);
                    if (object.length() != 0) {
                        binding.progressCircular.setVisibility(View.GONE);
                        Double runTime, idleTime;
                        try {
                            String runtime = object.getString("runtime");
                            String idletime = object.getString("idletime");
                            if (!runtime.equalsIgnoreCase("null")) {
                                runTime = Double.parseDouble(runtime);
                            } else {
                                runTime = Double.parseDouble("0.0");
                            }
                            if (!idletime.equalsIgnoreCase("null")) {
                                idleTime = Double.parseDouble(idletime);
                            } else {
                                idleTime = Double.parseDouble("0.0");
                            }
                            NoOfFloors = new ArrayList();
                            value = new ArrayList();
                            JSONObject tempObj = object.getJSONObject("idle_data");
                            if (tempObj.length() != 0) {
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.put(tempObj);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String k = jsonObject.keys().next();
                                    int a = jsonObject.getInt("allowance");
                                    int b = jsonObject.getInt("Maintenance");
                                    int c = jsonObject.getInt("No Input");
                                    int d = jsonObject.getInt("Needle Change");
                                    value.add(a);
                                    value.add(b);
                                    value.add(c);
                                    value.add(d);
                                    for (j = (float) 0.5; j < value.size(); j = j + 1.0f) {
                                        try {
                                            double valuues = (double) value.get((int) j);
                                            float finalvalue = (float) valuues;
                                            NoOfFloors.add(new BarEntry(j, finalvalue));
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                                createBarChart(chart, NoOfFloors, floorName, "Needle Time Idle");
                                createPieChart(runTime, idleTime);
                            } else {
                                chart.clear();
                                binding.chartNeedleStatus.clear();
                                binding.chartNeedleStatus.invalidate();
                                binding.chartNeedleTimeIdle.clear();
                                binding.chartNeedleTimeIdle.invalidate();

                            }
                        } catch (Exception e) {
                            appClass.showSnackBar(context, "Error occurred");
                            e.printStackTrace();
                            binding.progressCircular.setVisibility(View.GONE);
                        }
                    } else {
                        binding.progressCircular.setVisibility(View.GONE);
                        Toast.makeText(appClass, getString(R.string.no_data_server), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {

                    binding.progressCircular.setVisibility(View.GONE);
                    if (error.networkResponse != null) {
                        statuscode = error.networkResponse.statusCode;
                    }
                    if (statuscode == 401) {
                       // baseActivity.TokenInvalid();
                        if(filterhasvalue==false){
                            alertDialog.dismiss();}
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);


                }
            }, 5000);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        calendar = Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.date4_1:
                String today = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {     getNeedleTimeIDle(heirachyID,ShiftIDlist, today, today, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case R.id.date4_2:
                String current_of_week_day = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                String current_week = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {    getNeedleTimeIDle(heirachyID,ShiftIDlist, current_of_week_day, current_week, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case R.id.date4_3:
                String currentday_last_of_week_ = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -2);
                String last_week = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {    getNeedleTimeIDle(heirachyID,ShiftIDlist, currentday_last_of_week_, last_week, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case R.id.date4_4:
                String currentday_last_of_4week = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -4);
                String last_4week = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {   getNeedleTimeIDle(heirachyID,ShiftIDlist, currentday_last_of_4week, last_4week, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.needTimeIdleRefresh.setRefreshing(false);
                if (appClass.NetworkConnected()) {      getNeedleTimeIDle(heirachyID,ShiftIDlist, startDate, endDate, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                binding.bottomDateNeedleTimeIdle.getMenu().setGroupCheckable(0, false, true);
            }
        }, 2000);
    }


    private void getFilterValue() {
        String value = preferences.getString("filterObject", "");
        heirachyID = new ArrayList<>();
        ShiftIDlist = new ArrayList<>();
        try {
            if (!value.equals("")) {
                JSONObject jsonObject = new JSONObject(value);
                startDate = jsonObject.getString("sDate");
                endDate = jsonObject.getString("eDate");
                siteId = jsonObject.getString("filterSiteID").replace("[", "").replace("]", "").replace(" ","");
                blockId = jsonObject.getString("filterBlockId").replace("[", "").replace("]", "").replace(" ","");
                floorId = jsonObject.getString("filterFloorId").replace("[", "").replace("]", "").replace(" ","");
                shitId = jsonObject.getString("filterShiftId").replace("[", "").replace("]", "").replace(" ","");
                lineID = jsonObject.getString("filterLineId").replace("[", "").replace("]", "").replace(" ","");
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


                Log.e(TAG, "getFilterValue: " + jsonObject.toString());
            }else {
                filterhasvalue=false;
                dialogBuilder =  new MaterialAlertDialogBuilder(context).setTitle("Filter Status!").
                        setMessage(getString(R.string.filterNoValue))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                appClass.navigateTo(getActivity(),R.id.action_needleTimeFragment_to_filterFragment);
                            }
                        });
                alertDialog=dialogBuilder.show();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
