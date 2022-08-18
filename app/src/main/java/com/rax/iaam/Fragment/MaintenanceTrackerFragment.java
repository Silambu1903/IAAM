package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.MaintenanceTrackerAdapter;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.MaintenanceTrackerModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentMaintenanceTrackerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.maintenance;
public class MaintenanceTrackerFragment extends Fragment {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentMaintenanceTrackerBinding binding;
    private List<MaintenanceTrackerModel> maintenanceTrackerModelList = new ArrayList<>();
    private static final String TAG = "MaintenanceTracker";
    BaseActivity baseActivity;
    int statuscode;
    int totalpage, page, size,sizeone;
    Button prev, next;
    int i = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_maintenance_tracker, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        baseActivity= (BaseActivity) getActivity();
        if (appClass.NetworkConnected()) {

            getMaintenanceTracker(10, 1, 0);
        } else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }

        binding.refreshFMT.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (appClass.NetworkConnected()) {

                    getMaintenanceTracker(10, 1, 0);
                } else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }
                binding.refreshFMT.setRefreshing(false);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvMaintenanceTracker.setLayoutManager(layoutManager);

        MaintenanceTrackerAdapter maintenanceTrackerAdapter = new MaintenanceTrackerAdapter(getContext(), maintenanceTrackerModelList);
        binding.rvMaintenanceTracker.setAdapter(maintenanceTrackerAdapter);
        intialize();


    }

    private void intialize() {
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextinit();
            }
        });
        binding.checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextClick();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.filter_menu_bn){
            appClass.navigateTo(getActivity(),R.id.action_maintenanceTrackerFragment_to_filterFragment);
        }else {
            BaseActivity activity = (BaseActivity)getActivity();
            activity.onSupportNavigateUp();
        }
        return true;
    }

    private void init() {
        if (maintenanceTrackerModelList.size() >= 0) {
            if (page <= totalpage) {
                page = page - 1;
            }
            if (page == 0) {

                appClass.showSnackBar(mContext, "No Previous page ");
            } else {
                getMaintenanceTracker(10, page, 1);

            }
        }
    }

    private void nextinit() {
        if (i <= maintenanceTrackerModelList.size()) {
            if (totalpage >= page) {
                if (page == totalpage && sizeone < totalpage) {
                    sizeone = totalpage;
                } else {
                    sizeone = page + 1;
                }
            }

            if (page == totalpage && sizeone > totalpage) {
                appClass.showSnackBar(mContext, "No next page ");
            } else {
                getMaintenanceTracker(10, sizeone, 1);
                // appClass.showSnackBar(mContext,"No next page ");

            }
        }

    }

    private void editTextClick() {
        if (binding.page.getText().toString().length() != 0) {
            String temp = binding.page.getText().toString();
            size = Integer.parseInt(temp);
            // Toast.makeText(appClass, ""+size, Toast.LENGTH_SHORT).show();

            if (size > totalpage) {
                appClass.showSnackBar(mContext, "Source Page Not found");
            } else {
                getMaintenanceTracker(10, size, 1);
            }
        } else {
            appClass.showSnackBar(mContext, "Enter the Page No");
        }

    }

    private void getMaintenanceTracker(int pagesize, int current_pageno, int mode) {
        maintenanceTrackerModelList.clear();
        showProgress();
        try {
            String api = null;
            if (mode == 0) {
                api = maintenance + "?" + "access_token=" + accessToken + "&per_page=" + pagesize;
            } else if (mode == 1) {

                api = maintenance + "?" + "access_token=" + accessToken+ "&per_page=" + pagesize + "&page=" + current_pageno;
            }
          //  String machines_Api = maintenance + "?" + "access_token=" + accessToken;
            appClass.httpRequestNewNightHacks(mContext, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess:_maintance"+object);
                    if (object.length()!=0){
                        dismissProgress();
                        try {
                            JSONArray jsonArray = object.getJSONArray("issues");
                            if(jsonArray.length()==0){
binding.text.setVisibility(View.VISIBLE);
                            }else{
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String RequestTime;
                                String RequestID = jsonObject.getString("request_id");
                                if (RequestID.trim().equals("") || RequestID.equalsIgnoreCase("null")) {
                                    RequestID = " - ";
                                } else {
                                    RequestID = jsonObject.getString("request_id");
                                }
                                String Location = jsonObject.getString("location");
                                if (Location.trim().equals("") || Location.equalsIgnoreCase("null")) {
                                    Location = " - ";
                                } else {
                                    Location = jsonObject.getString("location");


                                }
                                String Desk = jsonObject.getString("desk");
                                if (Desk.trim().isEmpty() || Desk.equalsIgnoreCase("null")) {
                                    Desk = " - ";
                                } else {
                                    Desk = jsonObject.getString("desk");
                                }
                                String a = jsonArray.getJSONObject(i).getString("request_time");
                                if (!formatDate(jsonArray.getJSONObject(i).getString("request_time")).equals("null")) {
                                    RequestTime = formatDate(jsonArray.getJSONObject(i).getString("request_time"));
                                } else {
                                    RequestTime = " - ";
                                }

                                String Status = jsonObject.getString("status");
                                if (Status.trim().equals("") || Status.equalsIgnoreCase("null")) {

                                    Status = " - ";
                                } else {
                                    Status = jsonObject.getString("status");
                                }
                                String Assigned = jsonObject.getString("assigned");
                                if (Assigned.trim().equals("") || Assigned.equalsIgnoreCase("null")) {
                                    Assigned = " - ";
                                } else {
                                    Assigned = jsonObject.getString("assigned");
                                }
                                maintenanceTrackerModelList.add(new MaintenanceTrackerModel
                                        (RequestID, Location, Desk, RequestTime, Status, Assigned));

                            }}
                        } catch (JSONException e) {
                            dismissProgress();
                            appClass.showLocalError(mContext);
                            e.printStackTrace();
                        }

                    } else {
                        appClass.showSnackBar(mContext, getString(R.string.no_data_server));
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    dismissProgress();
                    if (error.networkResponse != null) {
                        statuscode = error.networkResponse.statusCode;
                    }
                    if (statuscode == 401) {
                        baseActivity.TokenInvalid();
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);
                }
            }, 0);

        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
        }

    }

    private String formatDate(String fromDate) {
        SimpleDateFormat formatter, formatterdateTime;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        Date dates = null;
        try {
            dates = formatter.parse(fromDate.substring(0, 26) + fromDate.substring(27, 29));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatterdateTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        System.out.println("OldDate-->" + fromDate);
        System.out.println("NewDate-->" + formatterdateTime.format(dates));
        String datetimeformate = formatterdateTime.format(dates);
        return datetimeformate;
    }

    private void showProgress() {
        binding.progressCircularFloor.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularFloor.setVisibility(View.GONE);
    }

}
