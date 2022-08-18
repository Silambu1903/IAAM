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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.ramijemli.percentagechartview.callback.ProgressTextFormatter;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.EmployeeProfilePageAdapter;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.EmployeeProfileAssignmentModel;
import com.rax.iaam.Model.EmployeeProfileSkillHsitoryModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEmployeeProfileBinding;
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
import static com.rax.iaam.Others.ApplicationClass.employee_profile;

public class EmployeeProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentEmployeeProfileBinding binding;
    private EmployeeProfilePageAdapter pageAdapter;
    private List<EmployeeProfileSkillHsitoryModel> skillHistoryList;
    private List<EmployeeProfileAssignmentModel> assigmentsList;
    private String[] performanceGraphName;
    private ArrayList<Double> performanceGraphValue = new ArrayList<>();
    int empId;
    Double sewingHours;
    int totalHours;
    ImageView imageView;
    private static final String TAG = "EmployeeProfileFragment";
    BaseActivity baseActivity;
    int statuscode;
    EmpProfilePerformanceChart empProfilePerformanceChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_employee_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        baseActivity = (BaseActivity) getActivity();
        mContext = getContext();
        Bundle b = getArguments();
        empId = b.getInt("ids");
        Log.d(TAG, "onViewCreated:" + empId);
        //networkCheck
        if (appClass.NetworkConnected()) {
            getEmployeeProfile();
        } else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }

        binding.EmployeeProfileRefresh.setOnRefreshListener(this);
        imageView = binding.imageView3;
    }

    public void getEmployeeProfile() {
        showProgress();
        try {
            String machine_Api = employee_profile + empId + "?" + "access_token=" + accessToken;
            Log.d(TAG, "getEmployeeProfile:" + machine_Api);
            appClass.httpRequestNewNightHacks(mContext, machine_Api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess_emp:" + object);
                    if (object != null) {
                        try {
                            dismissProgress();
                            JSONObject jsonObject = object.getJSONObject("employee");
                            String name = jsonObject.getString("name");
                            if (name.trim().equals("") || name.equalsIgnoreCase("null")) {
                                binding.textView19.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView19.setTooltipText(name);
                                }
                                binding.textView19.setText(name);
                            }
                            String status = jsonObject.getString("status");
                            if (status.trim().equals("") || status.equalsIgnoreCase("null")) {
                                binding.textView20.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView20.setTooltipText(status);
                                }
                                binding.textView20.setText(status);
                                if (status.equalsIgnoreCase("Assigned")) {
                                    binding.textView20.setTextColor(Color.parseColor("#0F9D58"));
                                } else {
                                    binding.textView20.setTextColor(Color.parseColor("#DB4437"));
                                }
                            }
                            String machine = jsonObject.getString("machine");
                            if (machine.trim().equals("") || machine.equalsIgnoreCase("null")) {
                                binding.textView21.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView21.setTooltipText(machine);
                                }
                                binding.textView21.setText(machine);
                            }
                            String site = jsonObject.getString("site");
                            if (site.trim().equals("") || site.equalsIgnoreCase("null")) {
                                binding.textView26.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView26.setTooltipText(site);
                                }
                                binding.textView26.setText(site);
                            }
                            String block = jsonObject.getString("block");
                            if (block.trim().equals("") || block.equalsIgnoreCase("null")) {
                                binding.textView17.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView17.setTooltipText(block);
                                }
                                binding.textView17.setText(block);
                            }
                            String floor = jsonObject.getString("floor");
                            if (floor.trim().equals("") || floor.equalsIgnoreCase("null")) {
                                binding.textView16.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView17.setTooltipText(floor);
                                }
                                binding.textView16.setText(floor);
                            }
                            String line = jsonObject.getString("line");
                            if (line.trim().equals("") || line.equalsIgnoreCase("null")) {
                                binding.textView22.setText(" - ");
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    binding.textView17.setTooltipText(line);
                                }
                                binding.textView22.setText(line);
                            }
                            //image url
                            String url = jsonObject.getString("image_url");
                            Picasso.get().load(url).placeholder((R.drawable.ic_employee)).into(imageView);
                            String utilisation = String.valueOf(jsonObject.getDouble("utilisation"));
                            if (utilisation.equalsIgnoreCase("null")){
                                binding.meterEPNeedleUtilization.setText("0%");
                            }else {
                                binding.meterEPNeedleUtilization.setText(utilisation+"%");
                            }
                            String sewingHrs = String.valueOf(jsonObject.getDouble("sewing_hours"));
                            if (sewingHrs.equalsIgnoreCase("null")){
                                binding.meterEPSewingHrs.setText("0%");

                            }else {
                                binding.meterEPSewingHrs.setText(sewingHrs+"%");

                            }
                            String totalHrs = String.valueOf(jsonObject.getDouble("total_hours"));
                            if (totalHrs.equalsIgnoreCase("null")){
                                binding.meterEPTotalHrs.setText("0%");
                            }else {
                                binding.meterEPTotalHrs.setText(totalHrs + "%");
                            }

                            //view pager Table api data pass
                            JSONArray skillHistory = jsonObject.getJSONArray("skill_history");
                            if (skillHistory.length() != 0) {
                                getSkillHistory(skillHistory);
                            }
                            JSONArray assignments = jsonObject.getJSONArray("assignments");
                            if (assignments.length() != 0) {
                                getassigments(assignments);
                            }

                            JSONArray performanceDetails = jsonObject.getJSONArray("performance_details");
                            Log.d(TAG, "OnSuccess_binf:" + performanceDetails);
                            if (performanceDetails.length() != 0 || performanceDetails.length() > 0) {
                                getperformance(performanceDetails);
                            }
                            //data passing
                            tabConfig();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dismissProgress();
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
            }, 2000);
        } catch (Exception e) {
            dismissProgress();
            e.printStackTrace();
        }
    }


    /*  private void setDataToView(Double sewinghrs, int totalHours) {
          binding.meterEPSewingHrs.setTextFormatter(new ProgressTextFormatter() {
              @NonNull
              @Override
              public String provideFormattedText(float progress) {
                  return String.valueOf(sewinghrs);
              }
          });
          binding.meterEPTotalHrs.setTextFormatter(new ProgressTextFormatter() {
              @NonNull
              @Override
              public String provideFormattedText(float progress) {
                  return String.valueOf(totalHours);
              }
          });

          binding.meterEPFirstAid.setTextFormatter(new ProgressTextFormatter() {
              @NonNull
              @Override
              public String provideFormattedText(float progress) {
                  return "99";
              }
          });
          binding.meterEPNeedleBreaks.setTextFormatter(new ProgressTextFormatter() {
              @NonNull
              @Override
              public String provideFormattedText(float progress) {
                  return "5897";
              }
          });
      }
  */
    private void getperformance(JSONArray performanceDetails) {
        showProgress();
        try {
            dismissProgress();
            performanceGraphName = new String[performanceDetails.length()];
            performanceGraphValue = new ArrayList<>();
            for (int i = 0; i < performanceDetails.length(); i++) {
                String month = performanceDetails.getJSONObject(i).getString("month");
                double value = performanceDetails.getJSONObject(i).getDouble("value");
                performanceGraphName[i] = month;
                performanceGraphValue.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
            appClass.showLocalError(mContext);
        }
    }

    private void getassigments(JSONArray assignments) {
        showProgress();
        try {
            dismissProgress();
            assigmentsList = new ArrayList<>();
            for (int i = 0; i < assignments.length(); i++) {
                String date;
                if (!formatDate(assignments.getJSONObject(i).getString("date")).equalsIgnoreCase("null")) {
                    date = formatDate(assignments.getJSONObject(i).getString("date"));
                } else {
                    date = " - ";
                }
                String status = assignments.getJSONObject(i).getString("status");
                if (status.trim().equals("") || status.equalsIgnoreCase("null")) {
                    status = " - ";
                } else {
                    status = assignments.getJSONObject(i).getString("status");
                }
                String site = assignments.getJSONObject(i).getString("site");
                if (site.trim().equals("") || site.equalsIgnoreCase("null")) {
                    site = " - ";
                } else {
                    site = assignments.getJSONObject(i).getString("site");
                }

                String line = assignments.getJSONObject(i).getString("line");
                if (line.trim().equals("") || line.equalsIgnoreCase("null")) {
                    line = " - ";
                } else {
                    line = assignments.getJSONObject(i).getString("line");
                }
                String machine = assignments.getJSONObject(i).getString("machine");
                if (machine.trim().equals("") || machine.equalsIgnoreCase("null")) {
                    machine = " - ";
                } else {
                    machine = assignments.getJSONObject(i).getString("machine");
                }
                String operation = assignments.getJSONObject(i).getString("operation");
                if (operation.trim().equals("") || operation.equalsIgnoreCase("null")) {
                    operation = " - ";
                } else {
                    operation = assignments.getJSONObject(i).getString("operation");
                }

                assigmentsList.add(new EmployeeProfileAssignmentModel(date, status, site, line, machine, operation));
            }
        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
            appClass.showLocalError(mContext);
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
        return formateDate;
    }

    public void getSkillHistory(JSONArray skillHistory) {
        showProgress();
        try {
            skillHistoryList = new ArrayList<>();
            for (int i = 0; i < skillHistory.length(); i++) {
                String machineCode = skillHistory.getJSONObject(i).getString("machine_id");
                if (machineCode.trim().equals("") || machineCode.equalsIgnoreCase("null")) {
                    machineCode = " - ";
                } else {
                    machineCode = skillHistory.getJSONObject(i).getString("machine_id");
                }
                String operationCode = skillHistory.getJSONObject(i).getString("operation_id");
                if (operationCode.trim().equals("") || operationCode.equalsIgnoreCase("null")) {
                    operationCode = " - ";
                } else {
                    operationCode = skillHistory.getJSONObject(i).getString("operation_id");
                }
                String operationName = skillHistory.getJSONObject(i).getString("operation_name");
                if (operationName.trim().equals("") || operationName.equalsIgnoreCase("null")) {
                    operationName = " - ";
                } else {
                    operationName = skillHistory.getJSONObject(i).getString("operation_name");
                }
                String needleRun = skillHistory.getJSONObject(i).getString("needle_run");
                if (needleRun.trim().equals("") || needleRun.equalsIgnoreCase("null")) {
                    needleRun = " - ";
                } else {
                    needleRun = skillHistory.getJSONObject(i).getString("needle_run");
                }

                skillHistoryList.add(new EmployeeProfileSkillHsitoryModel(machineCode, operationCode, operationName, needleRun));
            }
        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
            appClass.showLocalError(mContext);
        }
    }

    private void showProgress() {
        binding.progressCircularFloor.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularFloor.setVisibility(View.GONE);
    }


    private void tabConfig() {
        ViewPager pager = (ViewPager) binding.viewPagerEmpProf;
        pageAdapter = new EmployeeProfilePageAdapter(getChildFragmentManager(),
                binding.tabLayoutEmpProf.getTabCount(),
                skillHistoryList, assigmentsList, performanceGraphName, performanceGraphValue, empId);
        pager.setAdapter(pageAdapter);
        binding.tabLayoutEmpProf.setupWithViewPager(pager, true);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.EmployeeProfileRefresh.setRefreshing(false);
                //networkCheck
                if (appClass.NetworkConnected()) {
                    getEmployeeProfile();
                } else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }
            }
        }, 2000);
    }
}

