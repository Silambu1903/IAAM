package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.NeedleTimeRunBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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


public class NeedleTimeRun extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private ApplicationClass appClass;
    private Context context;
    private NeedleTimeRunBinding binding;
    private static final String TAG = "NeedleTimeRun";
    List<JSONObject> linesArr;
    DateFormat dateFormat;
    Calendar calendar;
    String startDate;
    String endDate;
    String siteId;
    String blockId;
    String floorId;
    String lineID;
    String shitId;
    ArrayList<String> heirachyID;
    ArrayList<String> ShiftIDlist;
    private SharedPreferences preferences;
    String[] lineNames;
    String[] lineNames_location;
    ArrayList<ILineDataSet> dataSets;
    private SharedPreferences.Editor editor;
    ValueFormatter mYValueFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return "" + (float) value;
        }
    };

    public static ArrayList<String> custom;
    BaseActivity baseActivity;
    int statuscode;
    MaterialAlertDialogBuilder dialogBuilder;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean filterhasvalue =true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.needle_time_run, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();

        context = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        getFilterValue();
        //network
        if (appClass.NetworkConnected()) {  getNeedleTime(heirachyID, ShiftIDlist, startDate, endDate, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }


        binding.needTimeRunRefresh.setOnRefreshListener(this);
        binding.bottomDateNeedleTimeRun.setOnNavigationItemSelectedListener(this);
        binding.bottomDateNeedleTimeRun.getMenu().setGroupCheckable(0, false, true);
        // SaveFilterValue();
        custom = new ArrayList<>();

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
                siteId = jsonObject.getString("filterSiteID").replace("[", "").replace("]", "").replace(" ", "");
                blockId = jsonObject.getString("filterBlockId").replace("[", "").replace("]", "").replace(" ", "");
                floorId = jsonObject.getString("filterFloorId").replace("[", "").replace("]", "").replace(" ", "");
                lineID = jsonObject.getString("filterLineId").replace("[", "").replace("]", "").replace(" ", "");
                shitId = jsonObject.getString("filterShiftId").replace("[", "").replace("]", "").replace(" ", "");
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

                Log.e(TAG, "getFilterValue: " + lineID + shitId+startDate+endDate);
            } else {
                filterhasvalue=false;
                dialogBuilder =  new MaterialAlertDialogBuilder(context).setTitle("Filter Status!").
                        setMessage(getString(R.string.filterNoValue))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // appClass.navigateTo(getActivity(),R.id.action_minutesFragment_to_filterFragment);
                            }
                        });
                alertDialog=dialogBuilder.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNeedleTime(ArrayList<String> heirachyID, ArrayList<String> shiftIDlist, String startDate, String endDate, int mode) {
        List<Integer> newhierachyIdList = new ArrayList<>();
        List<Integer> newShiftIdList = new ArrayList<>();
        if (heirachyID.size() != 0) {
            String[] newOne = heirachyID.get(0).split(",");
            int j = 0;
            while (j < newOne.length) {
                newhierachyIdList.add(Integer.parseInt(newOne[j]));

                j++;
            }

        } else {
            System.out.println("");
        }

        if (shiftIDlist.size() != 0) {
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
        Log.d(TAG, "getNeedleTime_1:" + formedString+formedStringShift);
        try {
            String api = null;
            if (mode == 0) {
                api = reports + "runtime_details?" + formedString + formedStringShift + accessTokenKey + accessToken;
            } else if (mode == 1) {
                api = reports + "runtime_details?" + formedString + formedStringShift + accessTokenKey + accessToken + startDateKey + startDate + endDateKey + endDate;
            }
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(context, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess_needlerun:"+object);
                    if (object.length() != 0) {
                        binding.progressCircular.setVisibility(View.GONE);
                        Double runTime, idleTime;
                        String runtime, idletime;
                        try {
                            runtime = object.getString("runtime");
                            idletime = object.getString("idletime");
                            if (!runtime.equalsIgnoreCase("null")) {
                                runTime = Double.parseDouble(runtime);
                            } else {
                                runTime = Double.parseDouble("N/A");
                            }
                            if (!idletime.equalsIgnoreCase("null")) {
                                idleTime = Double.parseDouble(idletime);
                            } else {
                                idleTime = Double.parseDouble("N/A");
                            }
                            JSONArray lines = object.getJSONArray("lines");
                            linesArr = new ArrayList<>();
                            for (int i = 0; i < lines.length(); i++) {
                                linesArr.add(lines.getJSONObject(i));
                            }

                            createPieChart(runTime, idleTime);
                            loadLineGraph();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
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
                        baseActivity.TokenInvalid();
                        if(filterhasvalue==false){
                            alertDialog.dismiss();}
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);


                }
            }, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadLineGraph() {

        lineNames = new String[linesArr.size()];
        lineNames_location= new String[linesArr.size()];
        dataSets = new ArrayList<>();
        try {
            if (linesArr.size() != 0) {
                int shiftCountIndex = 0;
                List<String> shifts = getAllShifts();
                while (shiftCountIndex < shifts.size()) {
                    ArrayList<Entry> dataPoints = new ArrayList<>();
                    for (int i = 0; i < linesArr.size(); i++) {
                        lineNames[i] = linesArr.get(i).getString("name");
                        lineNames_location[i] = linesArr.get(i).getString("location");
                        JSONArray shiftArray = linesArr.get(i).getJSONArray("shifts");
                        for (int j = 0; j < shiftArray.length(); j++) {
                            if (shiftArray.getJSONObject(j).getString("shift_name").equals(shifts.get(shiftCountIndex))) {
                                dataPoints.add(new Entry(i, (BigDecimal.valueOf(shiftArray.getJSONObject(j).getDouble("value")).floatValue())));
                            }
                        }
                    }
                    LineDataSet lineDataSet = new LineDataSet(dataPoints, shifts.get(shiftCountIndex));
                    lineDataSet.setColor(colors[shiftCountIndex % colors.length]);
                    lineDataSet.setCircleColor(colors[shiftCountIndex % colors.length]);
                    dataSets.add(lineDataSet);
                    shiftCountIndex++;
                }
                LineData lineChartData = new LineData(dataSets);
                binding.lineChartNeedleTimeRun.setData(lineChartData);
                lineChartData.setValueFormatter(mYValueFormatter);
                configureLineChart(binding.lineChartNeedleTimeRun, lineNames);

            } else {
                binding.lineChartNeedleTimeRun.clear();
                binding.lineChartNeedleTimeRun.invalidate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<String> getAllShifts() {
        List<String> shifts = new ArrayList<>();
        try {
            for (int i = 0; i < linesArr.size(); i++) {
                JSONArray shiftArray = linesArr.get(i).getJSONArray("shifts");
                for (int j = 0; j < shiftArray.length(); j++) {
                    String shiftName = shiftArray.getJSONObject(j).getString("shift_name");
                    if (!shifts.contains(shiftName)) {
                        shifts.add(shiftName);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shifts;
    }

    private void SaveValue() {
        for (int i = 0; i < lineNames_location.length; i++) {
            String a = lineNames_location[i];

            custom.add(a);

        }
        Log.d(TAG, "loadLineGraph:" + custom.toString());
        JSONObject filterObject = new JSONObject();
        try {
            filterObject.put("linedata", custom);
            editor = preferences.edit();
            editor.putString("filterObjects", filterObject.toString()).apply();
            Log.d(TAG, "SaveFilterValue_run:" + custom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final int[] colors = new int[]{
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };

    private void configureLineChart(LineChart lineChart, String[] xValues) {
        SaveValue();
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setText("");
        lineChart.setScaleEnabled(false);
//        lineChart.setDragEnabled(true);
//        lineChart.setScaleEnabled(true);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        lineChart.setMarker(new CustomMarker(context, R.layout.custom_marker_view));
        lineChart.setDragOffsetX(85);
        lineChart.setVisibleXRangeMaximum(7);
        lineChart.moveViewToX(1);
        lineChart.invalidate();
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
        l.setTextSize(12f);
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
        dataSet.setColors(Color.rgb(0, 100, 0), Color.rgb(218, 165, 32));
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
        int offset = (int) (height * 0.38);
        LinearLayout.LayoutParams rlParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        rlParams.setMargins(0, 0, 0, -offset);
        rlParams.gravity = Gravity.BOTTOM;
        pieChart.setLayoutParams(rlParams);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.bottomDateNeedleTimeRun.getMenu().setGroupCheckable(0, true, true);
        calendar = Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.date4_1:
                String today = dateFormat.format(calendar.getTime());
                //network
                if (appClass.NetworkConnected()) {   getNeedleTime(heirachyID, ShiftIDlist, today, today, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
            case R.id.date4_2:
                String current_of_week_day = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                String current_week = dateFormat.format(calendar.getTime());
                //network
                if (appClass.NetworkConnected()) {   getNeedleTime(heirachyID, ShiftIDlist, current_of_week_day, current_week, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
            case R.id.date4_3:
                String currentday_last_of_week_ = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -2);
                String last_week = dateFormat.format(calendar.getTime());
                //network
                if (appClass.NetworkConnected()) {  getNeedleTime(heirachyID, ShiftIDlist, currentday_last_of_week_, last_week, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
            case R.id.date4_4:
                String currentday_last_of_4week = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.WEEK_OF_YEAR, -4);
                String last_4week = dateFormat.format(calendar.getTime());
                //network
                if (appClass.NetworkConnected()) {   getNeedleTime(heirachyID, ShiftIDlist, currentday_last_of_4week, last_4week, 1); } else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                break;
        }

        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.needTimeRunRefresh.setRefreshing(false);
                //network
                if (appClass.NetworkConnected()) { getNeedleTime(heirachyID, ShiftIDlist, startDate, endDate, 1);} else if (!appClass.NetworkConnected()) { appClass.showConnectionLNetworkError(context); }
                binding.bottomDateNeedleTimeRun.getMenu().setGroupCheckable(0, false, true);
            }
        }, 2000);
    }

    public class CustomMarker extends MarkerView {
        TextView custometext;
        private SharedPreferences preferencess;
        ArrayList<String> TostString;
        ArrayList<String> ShowText;
        ArrayList<String> custom1;

        public CustomMarker(Context context, int layoutResource) {
            super(context, layoutResource);
            custometext = findViewById(R.id.tvcontent);
            preferencess = PreferenceManager.getDefaultSharedPreferences(context);
            TostString = new ArrayList<>();
            ShowText = new ArrayList<>();
            custom1 = new ArrayList<>();
            getFilterValue();
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            super.refreshContent(e, highlight);
            String name_type;
            String[] newOne = TostString.get(0).split(",");
            int j = 0;
            if(newOne.length!=0){
            while (j < newOne.length) {
                ShowText.add(newOne[j]);
                j++;
            }
            for (int i = 0; i < ShowText.size(); i++) {
                name_type = ShowText.get(i);
               String headername = name_type.substring(0, name_type.lastIndexOf(" -"));
                custom1.add(headername);
            }
            String a = custom1.get((int) e.getX());
            custometext.setText(custom1.get((int) e.getX()));
            Log.d(TAG, "refreshContent:_value::-" + a);

        }}



        private void getFilterValue() {
            String value = preferencess.getString("filterObjects", "");
            JSONObject jsonObject;
            try {
                if (!value.equals("")) {
                    jsonObject = new JSONObject(value);
                    String aDek = jsonObject.getString("linedata").replace("[", "")
                            .replace("]", "");
                    String siteId = jsonObject.getString("linedata").replace("[", "")
                            .replace("]", "");
                    Log.d(TAG, "getFilterValue_site:" + siteId + aDek);
                    TostString.add(aDek);
                    Log.d(TAG, "getFilterValue_run:" + TostString);
                } else {
                    Toast.makeText(getContext(), "No value for Marker", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }


}