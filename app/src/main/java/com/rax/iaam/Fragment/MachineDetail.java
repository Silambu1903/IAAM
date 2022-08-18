package com.rax.iaam.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.MachineDetailPageAdapter;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.MachineDetailHistoryModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentMachineDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.machineDetail;

public class MachineDetail extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentMachineDetailBinding binding;
    private MachineDetailPageAdapter pageAdapter;
    private ViewPager pager;
    BaseActivity baseActivity;
    int statuscode;
    private static final String TAG = "FWS_MachineDetail";

    List<MachineDetailHistoryModel> utilisationList = new ArrayList<>();
    List<MachineDetailHistoryModel> maintenanceList = new ArrayList<>();
    List<MachineDetailHistoryModel> operatorList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_machine_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity= (BaseActivity) getActivity();
        mContext = getContext();
        pager = (ViewPager) binding.viewPagerMD;
        Bundle b = getArguments();
        int a = b.getInt("MachineID");
        Log.d(TAG, "onViewCreated:"+b.getInt("MachineID"));
        if (appClass.NetworkConnected()) {
            getMachineDetail(b.getInt("MachineID"));
        }
        else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }
        binding.MachineDetailRefresh.setOnRefreshListener(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getMachineDetail(int machine_id) {
        try {
            showProgress();
            appClass.httpRequestNewNightHacks(mContext, machineDetail + machine_id + "?" + accessTokenKey + accessToken, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    if (object.length() != 0) {
                        Log.d(TAG, "OnSuccess:machine_detail" + object);
                        try {
                            binding.noData.setVisibility(View.GONE);
                            JSONObject machine = object.getJSONObject("machine");
                            if (machine.getString("make").equals("") || machine.getString("make").equalsIgnoreCase("null")) {
                                binding.make.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.make.setTooltipText(machine.getString("make"));
                                }
                                binding.make.setText(machine.getString("make"));
                            }
                            if (machine.getString("model").equals("") || machine.getString("model").equalsIgnoreCase("null")) {
                                binding.model.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.model.setTooltipText(machine.getString("model"));
                                }
                                binding.model.setText(machine.getString("model"));
                            }

                            if (machine.getString("serial_number").equals("") || machine.getString("serial_number").equalsIgnoreCase("null")) {
                                binding.sno.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.sno.setTooltipText(machine.getString("serial_number"));
                                }
                                binding.sno.setText(machine.getString("serial_number"));
                            }

                            String url = machine.getString("image_url");
                            Picasso.get().load(url).placeholder((R.drawable.ic_sewing_machine)).into(binding.imageView3);
                            String status = machine.getString("status");
                            if (status.equals("") || status.equalsIgnoreCase("null")) {
                                binding.status.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.status.setTooltipText(status);
                                }
                                binding.status.setText(status);
                                if (status.equalsIgnoreCase("Commissioned")) {
                                    binding.status.setTextColor(Color.parseColor("#0F9D58"));
                                } else {
                                    binding.status.setTextColor(Color.parseColor("#DB4437"));
                                }
                            }
                            String availability = String.valueOf(machine.getDouble("availability"));
                        if (availability.equalsIgnoreCase("null")) {
                            binding.meterMDAvailability.setText("0%");
                        } else {
                            binding.meterMDAvailability.setText(availability+"%");
                        }
                            String uptime = String.valueOf(machine.getDouble("uptime"));
                        if (uptime.equalsIgnoreCase("null")) {
                            binding.meterMDUptime.setText("0%");
                        } else {
                            binding.meterMDUptime.setText(uptime+"%");
                        }
                            String utilisation = String.valueOf(machine.getDouble("utilisation"));
                        if (utilisation.equalsIgnoreCase("null")) {
                            binding.meterMDUtilization.setText("0%");
                        } else {
                            binding.meterMDUtilization.setText(utilisation+"%");
                        }
                            JSONArray utilisationHistory = machine.getJSONArray("utilisation_history");
                            if (utilisationHistory.length() != 0) {
                                getUtilisationHistory(utilisationHistory);
                            }
                            JSONArray operatorHistory = machine.getJSONArray("operator_history");
                            if (operatorHistory.length() != 0) {
                                getOperatorHistory(operatorHistory);
                            }
                            JSONArray maintenanceHistory = machine.getJSONArray("maintenance_history");
                            if (maintenanceHistory.length() != 0) {
                                getMaintenanceHistory(maintenanceHistory);
                            }
                            tabConfig();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dismissProgress();
                    } else {
                        binding.noData.setVisibility(View.VISIBLE);
                        appClass.showSnackBar(mContext, "No Data From Server");
                    }

                }

                @Override
                public void OnFailure(VolleyError error) {
                    dismissProgress();

                    binding.noData.setVisibility(View.VISIBLE);
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
            e.printStackTrace();
            dismissProgress();
        }
    }

    private void getUtilisationHistory(JSONArray utilisationHistory) {
        utilisationList.clear();
        try {
            for (int i = 0; i < utilisationHistory.length(); i++) {
                String fromDate;
                if (!utilisationHistory.getJSONObject(i).getString("from_date").equalsIgnoreCase("null")) {
                    fromDate = formatDate(utilisationHistory.getJSONObject(i).getString("from_date"));
                } else {
                    fromDate = " - ";
                }
                String upTime = utilisationHistory.getJSONObject(i).getString("uptime");
                if (upTime.trim().equals("") || upTime.equalsIgnoreCase("null")) {
                    upTime = " - ";
                } else {
                    upTime = utilisationHistory.getJSONObject(i).getString("uptime");
                }
                String utilisation = utilisationHistory.getJSONObject(i).getString("utilisation");
                if (utilisation.trim().equals("") || utilisation.equalsIgnoreCase("null")) {
                    utilisation = " - ";
                } else {
                    utilisation = utilisationHistory.getJSONObject(i).getString("utilisation");
                }
                String operator = utilisationHistory.getJSONObject(i).getString("operator");
                if (operator.trim().equals("") || operator.equalsIgnoreCase("null")) {
                    operator = " - ";
                } else {
                    operator = utilisationHistory.getJSONObject(i).getString("operator");
                }

                if (i == 0) {
                    utilisationList.add(new MachineDetailHistoryModel(MachineDetailHistoryModel.UTILIZATION_HEAD_TYPE, "", "", "", ""));
                }
                utilisationList.add(new MachineDetailHistoryModel(MachineDetailHistoryModel.UTILIZATION_TYPE, fromDate, upTime, utilisation, operator));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOperatorHistory(JSONArray operatorHistory) {
        operatorList.clear();
        try {
            for (int i = 0; i < operatorHistory.length(); i++) {
                String fromDate = operatorHistory.getJSONObject(i).getString("from_date");
                if (fromDate.trim().equals("") || fromDate.equalsIgnoreCase("null")) {
                    fromDate = " - ";
                } else {
                    fromDate = formatDate(operatorHistory.getJSONObject(i).getString("from_date"));
                }
                String endDate = operatorHistory.getJSONObject(i).getString("end_date");
                if (endDate.trim().equals("") ||  endDate.equalsIgnoreCase("null")) {
                    endDate = " - ";
                } else {
                    endDate = formatDate(operatorHistory.getJSONObject(i).getString("end_date"));
                }
                String empCode = operatorHistory.getJSONObject(i).getString("employee_code");
                if (empCode.trim().equals("") || empCode.equalsIgnoreCase("null")) {
                    empCode = " - ";
                } else {
                    empCode = operatorHistory.getJSONObject(i).getString("employee_code");
                }
                String operator = operatorHistory.getJSONObject(i).getString("name");
                if (operator.trim().equals("") || operator.equalsIgnoreCase("null")) {
                    operator = " - ";
                } else {
                    operator = operatorHistory.getJSONObject(i).getString("name");
                }

                if (i == 0) {
                    operatorList.add(new MachineDetailHistoryModel(MachineDetailHistoryModel.OPERATOR_HEAD_TYPE, "", "", "", ""));
                }
                operatorList.add(new MachineDetailHistoryModel(0, MachineDetailHistoryModel.OPERATOR_TYPE, fromDate, endDate, operator, empCode, ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMaintenanceHistory(JSONArray maintenanceHistory) {
        maintenanceList.clear();
        try {
            for (int i = 0; i < maintenanceHistory.length(); i++) {
                int issueID;
                String fromDate = maintenanceHistory.getJSONObject(i).getString("start_date");
                if (fromDate.trim().equals("") || fromDate.equalsIgnoreCase("null")) {
                    fromDate = " - ";
                } else {
                    fromDate = formatDate(maintenanceHistory.getJSONObject(i).getString("start_date"));
                }

                issueID = maintenanceHistory.getJSONObject(i).getInt("issue_id");
                String code = maintenanceHistory.getJSONObject(i).getString("reason_code");
                if (code.trim().equals("") || code.equalsIgnoreCase("null")) {
                    code = " - ";
                } else {
                    code = maintenanceHistory.getJSONObject(i).getString("reason_code");
                }
                String status = maintenanceHistory.getJSONObject(i).getString("status");
                if (status.trim().equals("") || status.equalsIgnoreCase("null")) {
                    status = " - ";
                } else {
                    status = maintenanceHistory.getJSONObject(i).getString("status");
                }
                String assignedTo = maintenanceHistory.getJSONObject(i).getString("assigned_to");
                if (assignedTo.trim().equals("") || assignedTo.equalsIgnoreCase("null")) {
                    assignedTo = " - ";
                } else {
                    assignedTo = maintenanceHistory.getJSONObject(i).getString("assigned_to");
                }

                if (i == 0) {
                    maintenanceList.add(new MachineDetailHistoryModel(MachineDetailHistoryModel.MAINTENANCE_HEAD_TYPE, "", "", "", ""));
                }
                maintenanceList.add(new MachineDetailHistoryModel(MachineDetailHistoryModel.MAINTENANCE_TYPE, fromDate, issueID, code, status, assignedTo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatDate(String fromDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = format.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formateDate = new SimpleDateFormat("MM-dd-yyyy").format(date);
        Log.d(TAG, "formatDate: MyDate" + date);
        return formateDate;
    }

    private void showProgress() {
        binding.progressCircular.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircular.setVisibility(View.GONE);
    }

    private void tabConfig() {
        try {
            pageAdapter = new MachineDetailPageAdapter(getChildFragmentManager(), binding.tabLayoutMD.getTabCount(), utilisationList, operatorList, maintenanceList);
            pager.setAdapter(pageAdapter);
            pager.setOffscreenPageLimit(3);
            binding.tabLayoutMD.setupWithViewPager(pager, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.MachineDetailRefresh.setRefreshing(false);
                Bundle b = getArguments();
                if (appClass.NetworkConnected()) {
                    getMachineDetail(b.getInt("MachineID"));
                }
                else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }
            }
        }, 2000);
    }
//LastDone ProgressCircle Lib
}
