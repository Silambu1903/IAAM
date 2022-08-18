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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Adapter.SessionPunctualityAdapter;
import com.rax.iaam.Adapter.SessionPunctualityLineAdapter;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentSessionPunctualityWorkBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.punctuality;
import static com.rax.iaam.Others.ApplicationClass.sensorTypeIdKey;
import static com.rax.iaam.Others.ApplicationClass.sessionType;

public class SessionPunctualityWork extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private ApplicationClass appClass;
    private Context context;
    private FragmentSessionPunctualityWorkBinding binding;
    private SessionPunctualityAdapter adapter;
    private SessionPunctualityLineAdapter lineAdapter;
    private String startDate;
    private String endDate;
    String dateXvalue;
    String onTimeData;
    String lineName;
    String siteId;
    String blockId;
    String floorId;
    ArrayList<String> heirachyID;
    private static final String TAG = "SessionPunctualityWork";
    ArrayList<String> ndate;
    ArrayList<String> onTime;
    DateFormat dateFormat;
    Calendar calendar;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_session_punctuality_work, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        context = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        getFilterValue();
        if (appClass.NetworkConnected()) {     getSessionworkApiData(heirachyID, startDate, endDate);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binding.rvSessionPWorkHeader.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        binding.rvSessionPWorkHeatMap.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        binding.SessionWorkRefresh.setOnRefreshListener(this);
        binding.bottomDateSessionPWork.setOnNavigationItemSelectedListener(this);
        binding.bottomDateSessionPWork.getMenu().setGroupCheckable(0, false, true);

    }

    private void getFilterValue() {
        String value = preferences.getString("filterObject", "");
        heirachyID = new ArrayList<>();
        try {
            if (!value.equals("")) {
                JSONObject jsonObject = new JSONObject(value);
                startDate = jsonObject.getString("sDate");
                endDate = jsonObject.getString("eDate");
                siteId = jsonObject.getString("filterSiteID").replace("[", "").replace("]", "").replace(" ","");
                blockId = jsonObject.getString("filterBlockId").replace("[", "").replace("]", "").replace(" ","");
                floorId = jsonObject.getString("filterFloorId").replace("[", "").replace("]", "").replace(" ","");
                if (!floorId.isEmpty()) {
                    heirachyID.add(floorId);
                } else if (!blockId.isEmpty()) {
                    heirachyID.add(blockId);
                } else if (!siteId.isEmpty()) {
                    heirachyID.add(siteId);
                }
                Log.e(TAG, "getFilterValue: " + heirachyID.toString());
            }else {
//                new MaterialAlertDialogBuilder(context).setTitle("Filter Status!").
//                        setMessage(getString(R.string.filterNoValue))
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                               // appClass.navigateTo(getActivity(),R.id.action_sessionPunctuality_to_filterFragment);
//                            }
//                        }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSessionworkApiData(ArrayList<String>heirachyID, String startDate, String endDate) {
        binding.chartSessionPWork.clear();
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
        try {
            String WorkSessionApi = punctuality + formedString+ accessTokenKey + accessToken + "&start_date=" + startDate + "&end_date=" + endDate  + sessionType + "work";
            Log.d(TAG, "getBreakAPi_111:"+WorkSessionApi);
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, WorkSessionApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "getBreakAPi_111 "+object);
                    if (object.length()!=0){
                        binding.progressCircular.setVisibility(View.GONE);
                        try {
                            String timeSession, deviatedSession;
                            double timesession, deviatedsession;
                            timeSession = object.getString("on_time_sessions");
                            deviatedSession = object.getString("deviated_sessions");
                            if (!timeSession.equalsIgnoreCase("null")) {
                                timesession = Double.parseDouble(timeSession);
                            } else {
                                timesession = 0.0;
                            }
                            if (!deviatedSession.equalsIgnoreCase("null")) {
                                deviatedsession = Double.parseDouble(deviatedSession);
                            } else {
                                deviatedsession = 0.0;
                            }
                            String total_sessions= object.getString("total_sessions");
                            if (!total_sessions.equalsIgnoreCase("null")) {
                                binding.textView64.setText(object.getString("total_sessions"));
                            }else {
                                binding.textView64.setText(" - ");
                            }
                            String boundary_excursions= object.getString("boundary_excursions");
                            if (!boundary_excursions.equalsIgnoreCase("null")) {
                                binding.textView68.setText(object.getString("boundary_excursions"));
                            }else {
                                binding.textView68.setText(" - ");
                            }
                            String time_outside_boundaries= object.getString("time_outside_boundaries");
                            if (!time_outside_boundaries.equalsIgnoreCase("null")) {
                              int a = object.getInt("time_outside_boundaries");
                                int hours = a / 60; //since both are ints, you get an int
                                int minutes = a % 60;
                                Log.d(TAG, "OnSuccess:_run"+hours+"hrs"+minutes+"min");
                                String value = hours+" hrs"+minutes+" min";
                                binding.textView67.setText(value);
                            }else {
                                binding.textView67.setText(" - ");
                            }
                            JSONArray lines = object.getJSONObject("punctuality_heatmap").getJSONArray("lines");
                            if (lines.length() != 0) {
                                binding.worknodata.setVisibility(View.GONE);
                                binding.horizontalwork.setVisibility(View.VISIBLE);
                                ndate = new ArrayList<>();
                                try {
                                    JSONArray dateWise = null;
                                    List<ArrayList<String>> newList = new ArrayList<>();

                                    for (int i = 0; i < lines.length(); i++) {
                                        onTime = new ArrayList<>();
                                        JSONObject jsonObject = lines.getJSONObject(i);
                                        lineName = jsonObject.getString("name");
                                        dateWise = jsonObject.getJSONArray("datewise_details");
                                        onTime.add(lineName);
                                        for (int j = 0; j < dateWise.length(); j++) {
                                            JSONObject jsonObject1 = dateWise.getJSONObject(j);
                                            dateXvalue = formatDate(jsonObject1.getString("date"));
                                            onTimeData = jsonObject1.getString("on_time");
                                            if((j==0) && (!ndate.contains("Date"))){
                                            ndate.add("Date");}
                                            if (!ndate.contains(dateXvalue)) {
                                               ndate.add(dateXvalue);
                                            }
                                            onTime.add(onTimeData);
                                        }
                                        newList.add(onTime);

                                    }
                                    adapter = new SessionPunctualityAdapter(ndate);
                                    lineAdapter = new SessionPunctualityLineAdapter(newList);
                                    binding.rvSessionPWorkHeader.setAdapter(adapter);
                                    binding.rvSessionPWorkHeatMap.setAdapter(lineAdapter);
                                    adapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    binding.progressCircular.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                                createPieChart(timesession, deviatedsession);
                            } else {
                                binding.progressCircular.setVisibility(View.GONE);
                                binding.worknodata.setVisibility(View.VISIBLE);
                                binding.horizontalwork.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            binding.progressCircular.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(appClass, getString(R.string.no_data_server), Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void OnFailure(VolleyError error) {
                    binding.progressCircular.setVisibility(View.GONE);
                    error.printStackTrace();
                    appClass.showLNetworkError(context);
                }
            }, 20000);

        } catch (Exception e) {
            binding.progressCircular.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }


    private String formatDate(String fromDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = format.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formateDate = new SimpleDateFormat("MM-dd-yy").format(date);
        return formateDate;
    }

    private void createPieChart(double timeSession, double deviatedSession) {
        PieChart pieChart = binding.chartSessionPWork;
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
        setPieData(2, 100, pieChart, timeSession, deviatedSession); // set Data
    }

    private void setPieData(int count, float range, PieChart pieChart, double timeSession, double deviatedSession) {
        pieChart.invalidate();
        ArrayList<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry((float) timeSession , "On time sessions"));
        values.add(new PieEntry((float) deviatedSession, "Deviated sessions"));
        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.rgb(0,100,0), Color.RED);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setData(data);
        pieChart.setNoDataText("");
        pieChart.invalidate();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.bottomDateSessionPWork.getMenu().setGroupCheckable(0, true, true);
        calendar = Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.date4_1:
                String today = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {    getSessionworkApiData(heirachyID, today,today);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case R.id.date4_2:
                String current_of_week_day = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                String current_week = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {   getSessionworkApiData(heirachyID, current_of_week_day, current_week);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case R.id.date4_3:
                String currentday_last_of_week_ = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -2);
                String last_week = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {    getSessionworkApiData(heirachyID, currentday_last_of_week_, last_week);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;
            case R.id.date4_4:
                String currentday_last_of_4week = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -4);
                String last_4week = dateFormat.format(calendar.getTime());
                if (appClass.NetworkConnected()) {    getSessionworkApiData(heirachyID, currentday_last_of_4week, last_4week);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                break;

        }
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.SessionWorkRefresh.setRefreshing(false);
                if (appClass.NetworkConnected()) {    getSessionworkApiData(heirachyID, startDate, endDate);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }

                binding.bottomDateSessionPWork.getMenu().setGroupCheckable(0, true, false);
            }
        },2000);
    }
}

