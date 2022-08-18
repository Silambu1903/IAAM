package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEpNosieLevelBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.NOISEQUALITY_ID;
import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.endDateKey;
import static com.rax.iaam.Others.ApplicationClass.epDetails;
import static com.rax.iaam.Others.ApplicationClass.sensorTypeIdKey;
import static com.rax.iaam.Others.ApplicationClass.startDateKey;

public class EnvironmentalParameterNoise extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentEpNosieLevelBinding binding;
    int noiseMinValue, noiseMaxValue, noiseAvgValue, noisecount;
    private static final String TAG = "Environmental_EP";
    String startDate;
    String endDate ;
    String siteId;
    String blockId;
    String floorId;
    ArrayList<String> heirachyID;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String unit = " dB";
    BaseActivity baseActivity;
    int statuscode;
    MaterialAlertDialogBuilder dialogBuilder;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean filterhasvalue =true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_ep_nosie_level, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        mContext = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        getFilterValue();
        //InternetCheck
       // if (appClass.NetworkConnected()) {
            getNoiseDetails(NOISEQUALITY_ID, heirachyID);
//        }
//        else if (!appClass.NetworkConnected()) {
//            appClass.showConnectionLNetworkError(mContext);
//        }


        binding.EpNoiseSwipe.setOnRefreshListener(this);
        ChipGroup chipGroup = binding.chipGroupEnvParamNoise;
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chip_min_noise:
                        binding.EpNoiseValueTv.setText(noiseMinValue + unit);
                        break;
                    case R.id.chip_avg_noise:
                        binding.EpNoiseValueTv.setText(noiseAvgValue + unit);
                        break;
                    case R.id.chip_max_noise:
                        binding.EpNoiseValueTv.setText(noiseMaxValue + unit);
                        break;
                }
            }
        });

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
            }else{
                filterhasvalue=false;
                dialogBuilder =  new MaterialAlertDialogBuilder(mContext).setTitle("Filter Status!").
                        setMessage(getString(R.string.filterNoValue))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog=dialogBuilder.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNoiseDetails(int sensorTypeID, ArrayList<String>heirachyID) {
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
        String index = "hierarchy_id=", formedString = "";
        for (int i = 0; i < newhierachyIdList.size(); i++) {
            if (i < newhierachyIdList.size()) {
                formedString = formedString + index + newhierachyIdList.get(i) + "&";
            } else {
                formedString = formedString + index + newhierachyIdList.get(i);
            }
        }
         api = epDetails +formedString+ accessTokenKey + accessToken + sensorTypeIdKey + sensorTypeID +
                 startDateKey + startDate + endDateKey + endDate;
        binding.progressCircular.setVisibility(View.VISIBLE);
        try {
            appClass.httpRequestNewNightHacks(getContext(), api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    binding.progressCircular.setVisibility(View.GONE);
                    loadLuxData(object);
                    try {
                        noiseMinValue = object.getInt("min");
                        noiseMaxValue = object.getInt("max");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {

                    binding.progressCircular.setVisibility(View.GONE);
                    try{
                        if (error.networkResponse != null) {
                            statuscode = error.networkResponse.statusCode;
                        }
                        if (statuscode == 401) {
                           // baseActivity.TokenInvalid();
                            if(filterhasvalue==false){
                                alertDialog.dismiss();
                            }
                        }
                        Log.d(TAG, "OnFailure1:" + statuscode);

                    }catch (Exception e){

                    }


                }
            }, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLuxData(JSONObject luxObject) {
        try {
            noiseMaxValue = luxObject.getInt("max");
            noiseMinValue = luxObject.getInt("min");
            noiseAvgValue = luxObject.getInt("avg");
            noisecount = luxObject.getInt("boundary_excursions_count");
            String time = luxObject.getString("excursion_time");
            binding.EpNoiseValueTv.setText(noiseAvgValue + unit);
            binding.EpNoiseBoundaryTime.setText(time);
            binding.EpNoiseBoundaryCount.setText(String.valueOf(noisecount));

            JSONArray sensor_details = luxObject.getJSONArray("sensor_details");
            JSONArray datewise_details = sensor_details.getJSONObject(0).getJSONArray("datewise_details");

            String[] xValues = new String[datewise_details.length()];
            int[] yValues = new int[datewise_details.length()];
            List<Float> doubleList = new ArrayList<>();
            ArrayList tempList = new ArrayList();
            for (int i = 0; i < datewise_details.length(); i++) {
                String[] date = formatDate(datewise_details.getJSONObject(i).getString("date")).split("-");
                xValues[i] = date[1] + "/" + date[0];
                yValues[i] = datewise_details.getJSONObject(i).getInt("avg_value");
                for (float j = (float) 0.5f; j < datewise_details.length(); j = j + 1.0f) {
                    if (j <= datewise_details.length() && i == (int) j) {
                        doubleList.add(i, j);
                    }
                }
                tempList.add(new BarEntry(doubleList.get(i), (float) datewise_details.getJSONObject(i).getInt("avg_value")));
            }

            createLineChart(binding.lineChartEnvParamNoise, tempList, xValues, "NOISE %");

        } catch (Exception e) {
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
        String formateDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
        return formateDate;
    }

    private void createLineChart(LineChart chart, ArrayList xValues, String[] yValues, String legendName) {
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setText(legendName);
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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8);


        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        LineDataSet bardataset = new LineDataSet(xValues, legendName);

        ValueFormatter vf = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (float) value;
            }
        };

        bardataset.setValueFormatter(vf);
        bardataset.setValueTextSize(8);
        chart.animateY(700);
        LineData data = new LineData(bardataset);
        bardataset.setColors(getResources().getColor(R.color.color1));
        chart.setData(data);
        chart.setVisibleXRangeMaximum(7);
        chart.moveViewToX(1);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.EpNoiseSwipe.setRefreshing(false);
                //InternetCheck
                if (appClass.NetworkConnected()) {
                    getNoiseDetails(NOISEQUALITY_ID, heirachyID);
                }
                else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }
            }
        }, 2000);
    }
}
