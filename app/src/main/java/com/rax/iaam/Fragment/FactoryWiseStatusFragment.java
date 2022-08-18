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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.FactoryWiseStatusAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.FactoryWiseStatusModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentFactoryWiseStatusBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;

import static com.rax.iaam.Others.ApplicationClass.clientIDNew;
import static com.rax.iaam.Others.ApplicationClass.clientSecret;
import static com.rax.iaam.Others.ApplicationClass.factoryWS;
import static com.rax.iaam.Others.ApplicationClass.keyAcesstoken;
import static com.rax.iaam.Others.ApplicationClass.keyNewPassword;
import static com.rax.iaam.Others.ApplicationClass.keyNewUsername;
import static com.rax.iaam.Others.ApplicationClass.keyRefreshtoken;
import static com.rax.iaam.Others.ApplicationClass.renew;


public class FactoryWiseStatusFragment extends Fragment implements ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    FragmentFactoryWiseStatusBinding binding;
    private static final String TAG = "FactoryWiseStatus";
    private ApplicationClass appClass;
    private Context mContext;
    private FactoryWiseStatusAdapter adapter;
    private List<FactoryWiseStatusModel> factoryWiseStatusModelList;
    BarChart blockChart, floorChart, lineChart;

BaseActivity baseActivity;

    //block
    public ArrayList noOfBlock ;
    public ArrayList<String> blockNames ;
    //floor
    public ArrayList NoOfFloors;
    public ArrayList<String> floorNames ;
    //line
    public ArrayList NoOfLine;
    public ArrayList<String> lineName ;
    //chartlistner
    private List<Integer> blockId ;
    private List<Integer> floorId;
    private List<Integer> lineId ;
    ImageView imageView;
    float j = (float) 0.5;
    int id, floorID,statuscode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_factory_wise_status, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        mContext = getContext();
        noOfBlock = new ArrayList();
        NoOfFloors = new ArrayList();
        NoOfFloors = new ArrayList();
        NoOfLine = new ArrayList();
        floorNames = new ArrayList<>();
        lineName = new ArrayList<>();
        blockId = new ArrayList<>();
        floorId = new ArrayList<>();
         lineId = new ArrayList<>();
        //initialize the recyclarview for setting the  list of site/block
        adapter = new FactoryWiseStatusAdapter(this);
        binding.rvFWS.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvFWS.setAdapter(adapter);
        binding.factoriWiseSwipe.setOnRefreshListener(this);

        blockChart = binding.chartFwsBlock;
        floorChart = binding.chartFwsFloor;
        lineChart = binding.chartFwsLine;
        imageView = binding.imageView2;

        //getting the list of site/block
        if (appClass.NetworkConnected()) {
            loadBlock();
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }


        binding.chartFwsBlock.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();
                int siteId = floorId.get((int) x);
                //  floorGraph(siteId);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //line_chart_onclick
        binding.chartFwsLine.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();
                int lineid = lineId.get((int) x);
                Bundle b = new Bundle();
                b.putInt("lineid", lineid);
                Log.d(TAG, "onValueSelected:"+lineid);
                appClass.navigateTo(getActivity(), R.id.action_factoryWiseStatusFragment_to_machineDetails, b);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //floor_chart_onclick
        binding.chartFwsFloor.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();
                floorID = floorId.get((int) x);
                //getting the list of line chart
                if (appClass.NetworkConnected()) {
                    lineGraph(floorID);
                }
                else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }

            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void floorGraph(int id) {
            NoOfFloors.clear();
            floorNames.clear();
        try {
            String machinesApi = factoryWS + "?" + "access_token=" + accessToken + "&" + "hierarchy_ids[]=" + id;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(mContext, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    binding.progressCircular.setVisibility(View.GONE);
                    Log.d(TAG, "OnSuccess:floorchart" + object);
                    try {
                        binding.txtFloorMachineCount.setText(object.getString("total_machines"));
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                String value = jsonObject.getString("name");
                                int date = jsonObject.getInt("machines_count");
                                int ids = jsonObject.getInt("id");
                                for (j = (float) 0.5; j < jsonArray.length(); j = j + 1.0f) {
                                    if (j <= jsonArray.length() && i == (int) j) {
                                        NoOfFloors.add(new BarEntry(j, date));
                                    }
                                }
                                floorNames.add(value);
                                floorId.add(ids);
                                createBarChart(floorChart, NoOfFloors, floorNames, "Floor");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        appClass.showSnackBar(mContext, "Error occurred");
                        e.printStackTrace();
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
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);
                   // appClass.showLNetworkError(mContext);
                }
            }, 5000);

        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void lineGraph(int i) {
        NoOfLine.clear();
        lineName.clear();
        try {
            String machinesApi = factoryWS + "?" + "access_token=" + accessToken + "&" + "hierarchy_ids[]=" + i;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(mContext, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    binding.progressCircular.setVisibility(View.GONE);
                    Log.d(TAG, "OnSuccess:linechart" + object);
                    try {
                        binding.txtLineMachineCount.setText(object.getString("total_machines"));
                        JSONArray jsonArray = object.getJSONArray("data");
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                try {
                                    String value = jsonObject.getString("name");
                                    int date = jsonObject.getInt("machines_count");
                                    int id = jsonObject.getInt("id");
                                    for (j = (float) 0.5; j < jsonArray.length(); j = j + 1.0f) {
                                        if (j <= jsonArray.length() && i == (int) j) {
                                            NoOfLine.add(new BarEntry(j, date));
                                        }
                                    }
                                    lineName.add(value);
                                    lineId.add(id);
                                    createBarChart(lineChart, NoOfLine, lineName, "Line");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        appClass.showSnackBar(mContext, "Error occurred");
                        e.printStackTrace();
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
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);
                }
            }, 5000);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }


    private void loadBlock() {
        try {
            String machinesApi = factoryWS + "?" + "access_token=" + accessToken;
            binding.progressCircular.setVisibility(View.VISIBLE);
            appClass.httpRequestNewNightHacks(mContext, machinesApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    if (object!=null){
                        Log.d(TAG, "site_block_list" + object);
                        factoryWiseStatusModelList = new ArrayList<>();
                        binding.progressCircular.setVisibility(View.GONE);
                        try {
                            binding.textView12.setText(object.getString("total_machines"));
                            String url = object.getString("image");
                            Picasso.get().load(url).placeholder(R.drawable.ic_sewing_machine).into(imageView);
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String site_name, block_name, mom;
                                int machine_count, site_id;
                                site_id = jsonObject.getInt("id");
                                site_name = jsonObject.getString("site_name");
                                block_name = jsonObject.getString("name");
                                machine_count = jsonObject.getInt("machines_count");
                                mom = jsonObject.getString("mom");
                                factoryWiseStatusModelList.add(new FactoryWiseStatusModel(site_id, site_name, block_name, machine_count, mom));
                            }
                            adapter.setData(factoryWiseStatusModelList);
                        } catch (Exception e) {
                            appClass.showSnackBar(mContext, "Error occurred");
                            e.printStackTrace();
                        }
                    }else {
                        appClass.showSnackBar(mContext, "Error occurred");
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    Log.d(TAG, "OnFailure:"+error);
                    binding.progressCircular.setVisibility(View.GONE);
                    if (error.networkResponse != null) {
                        statuscode = error.networkResponse.statusCode;
                    }
                    if (statuscode == 401) {
                        baseActivity.TokenInvalid();
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);
                }
            }, 2000);
        } catch (Exception e) {
            appClass.showSnackBar(mContext, "Error occurred");
            e.printStackTrace();
        }
    }


    private void createBarChart(BarChart chart, ArrayList xValues, ArrayList yValues, String legendName) {
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
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setTextSize(8);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        BarDataSet bardataset = new BarDataSet(xValues, legendName);

        //bar value label to int
        ValueFormatter vf = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        bardataset.setValueFormatter(vf);
        bardataset.setValueTextSize(8);
        chart.animateY(1500);
        BarData data = new BarData(bardataset);
        data.setBarWidth(0.4f);
        bardataset.setColors(getResources().getColor(R.color.color6));
        chart.setData(data);
        chart.setVisibleXRangeMaximum(7);
        chart.moveViewToX(1);
    }

    @Override
    public void OnItemClick(int pos) {
        id = factoryWiseStatusModelList.get(pos).getId();
        //getting the list of floor chart
        if (appClass.NetworkConnected()) {
            floorGraph(id);
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }

    }

    @Override
    public void OnItemLongClick(int pos) {
    }

    @Override
    public void onRefresh() {
        binding.factoriWiseSwipe.setRefreshing(false);
        if (appClass.NetworkConnected()) {
            loadBlock();
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (appClass.NetworkConnected()) {

        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }
    }

}
