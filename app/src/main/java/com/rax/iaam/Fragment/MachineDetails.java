package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.MachineDetailsAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.MachineDetailsModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentMachineDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.employee_details;
import static com.rax.iaam.Others.ApplicationClass.machines_id;

public class MachineDetails extends Fragment implements ItemClickListener {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentMachineDetailsBinding binding;
    private List<MachineDetailsModel> machineDetailsList = new ArrayList<>();
    private MachineDetailsAdapter adapter;
    String aname;
    private static final String TAG = "FWS_MachineList";
    int lineID;
    BaseActivity baseActivity;
    int statuscode;
    int totalpage, page, size, sizeone;
    Button prev, next;
    int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_machine_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        baseActivity = (BaseActivity) getActivity();
        adapter = new MachineDetailsAdapter(this);
        binding.rvMachineDetails.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvMachineDetails.setAdapter(adapter);

        //getting the line id value from factory wise status line chart
        Bundle b = getArguments();
        lineID = b.getInt("lineid");

        //getting the list of machine details

        if (appClass.NetworkConnected()) {

            getMachineDetails(10, 1, 0);
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }
        binding.refreshMDs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (appClass.NetworkConnected()) {
                    getMachineDetails(10, 1, 0);
                }
                else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }
                binding.refreshMDs.setRefreshing(false);
            }
        });
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

    private void getMachineDetails(int pagesize, int current_pageno, int mode) {

        machineDetailsList.clear();
        showProgress();
        try {
            String api = null;
            if (mode == 0) {
                api = machines_id + "?" + "line_id=" + lineID +"&" +accessTokenKey + accessToken + "&per_page=" + pagesize;
            } else if (mode == 1) {

                api = machines_id + "?" + "line_id=" + lineID +"&" +accessTokenKey + accessToken+ "&per_page=" + pagesize + "&page=" + current_pageno;
            }
          //  String machinesApi = machines_id + "?" + "line_id=" + lineID +"&" +accessTokenKey + accessToken ;
            appClass.httpRequestNewNightHacks(mContext, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess:_line" + object);
                    if (object.length() != 0) {
                        try {
                            dismissProgress();
                            JSONArray jsonArray = object.getJSONArray("machines");
                            if(jsonArray.length()==0){
                                binding.text.setVisibility(View.VISIBLE);
                            }else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id;
                                    id = jsonObject.getInt("id");
                                    String sno = jsonObject.getString("s_no");
                                    if (sno == null || sno.trim().equals("") || sno.equalsIgnoreCase("null")) {
                                        sno = " - ";
                                    } else {
                                        sno = jsonObject.getString("s_no");
                                    }
                                    String make = jsonObject.getString("make");
                                    if (make == null || make.trim().equals("") || make.equalsIgnoreCase("null")) {
                                        make = " - ";
                                    } else {
                                        make = jsonObject.getString("make");
                                    }
                                    String model = jsonObject.getString("model");
                                    if (model == null || model.trim().equals("") || model.equalsIgnoreCase("null")) {
                                        model = " - ";
                                    } else {
                                        model = jsonObject.getString("model");
                                    }
                                    String status = jsonObject.getString("status");
                                    if (status == null || status.trim().equals("") || status.equalsIgnoreCase("null")) {
                                        status = " - ";
                                    } else {
                                        status = jsonObject.getString("status");
                                    }

                                JSONArray jsonArray1 = jsonObject.getJSONArray("operators");
                                String[] operator = new String[0];
                                if (jsonArray1.length() > 0) {
                                    operator = new String[jsonArray1.length()];
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object1 = jsonArray1.getJSONObject(j);
                                        operator[j] = object1.getString("name");
                                        aname = operator[j];
                                    }
                                    Log.d(TAG, "OnSuccess:_string " + aname);
                                }

                                machineDetailsList.add(new MachineDetailsModel(sno, make, model, status, operator, id));
                                adapter.setData(machineDetailsList);
                                Log.d(TAG, "OnSuccess_list:" + machineDetailsList.toString());

                                }
                            }
                        } catch (JSONException e) {
                            dismissProgress();
                            appClass.showLocalError(mContext);
                            e.printStackTrace();
                        }
                    } else {
                        appClass.showSnackBar(mContext, "No data From Server");
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    dismissProgress();
                    error.printStackTrace();
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

    private void init() {

        if (machineDetailsList.size() >= 0) {

            if (page <= totalpage) {
                page = page - 1;
            }
            if (page == 0) {

                appClass.showSnackBar(mContext, "No Previous page ");
            } else {
                getMachineDetails(10, page, 1);

            }
        }
    }

    private void nextinit() {
        if (i <= machineDetailsList.size()) {
            if (totalpage >= page ) {
                if (page == totalpage && sizeone<totalpage) {
                    sizeone = totalpage;
                } else {
                    sizeone = page + 1;
                }
            }

            if (page == totalpage && sizeone>totalpage)  {
                appClass.showSnackBar(mContext, "No next page ");
            } else {

                getMachineDetails(10, sizeone, 1);
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
            }else{
                getMachineDetails(10, size, 1);
               }
        } else {
            appClass.showSnackBar(mContext, "Enter the Page No");
        }

    }

    private void showProgress() {
        binding.progressCircularFloor.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularFloor.setVisibility(View.GONE);
    }

    @Override
    public void OnItemClick(int pos) {
        Bundle b = new Bundle();
        b.putInt("MachineID", pos);
        Log.d(TAG, "MachineID=" + pos);
        appClass.navigateTo(getActivity(), R.id.action_machineDetails_to_machineDetail, b);
    }


    @Override
    public void OnItemLongClick(int pos) {

    }


}
