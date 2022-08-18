package com.rax.iaam.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rax.iaam.Activity.BaseActivity;
import com.rax.iaam.Adapter.EmployeeDetailsAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.EmployeeDetailsModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEmployeeDetailsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.employee_details;
import static com.rax.iaam.Others.ApplicationClass.endDateKey;
import static com.rax.iaam.Others.ApplicationClass.reports;
import static com.rax.iaam.Others.ApplicationClass.startDateKey;

public class EmployeeDetailsFragment extends Fragment implements ItemClickListener {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentEmployeeDetailsBinding binding;
    private List<EmployeeDetailsModel> employeeDetailsList = new ArrayList<>();
    private EmployeeDetailsAdapter adapter;
    private static final String TAG = "EmployeeDetails";
    public String id;
    String siteId;
    String blockId;
    String floorId;
    ArrayList<String> heirachyID;
    String FilterChip;
    ArrayList<String> filterName = new ArrayList<>();
    ChipGroup chipGroup;
    private SharedPreferences preferences;
    int totalpage, page, size,sizeone;
    Button prev, next;
    int i = 0;
    BaseActivity baseActivity;
    int statuscode;
    MaterialAlertDialogBuilder dialogBuilder;
    androidx.appcompat.app.AlertDialog alertDialog;
    boolean filterhasvalue =true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_employee_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        chipGroup = binding.chip4;
        baseActivity= (BaseActivity) getActivity();
        setHasOptionsMenu(true);
        adapter = new EmployeeDetailsAdapter(this);
        binding.rvEmployeeDetails.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvEmployeeDetails.setAdapter(adapter);
        prev = binding.previous;
//       binding.page.setOnTouchListener(new View.OnTouchListener() {
//           @Override
//           public boolean onTouch(View view, MotionEvent motionEvent) {
//               if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                   if(motionEvent.getRawX() >=  binding.page.getRight() -  binding.page.getTotalPaddingRight()) {
//                       // your action for drawable click event
//                       Toast.makeText(mContext, ""+size, Toast.LENGTH_SHORT).show();
//                       return true;
//                   }
//               }
//               return false;
//           }
//       });
        next = binding.next;
        getFilterValue();

        //networkCheck
        if (appClass.NetworkConnected()) {
            getEmployeeDetails(heirachyID, 10, 1, 0);
        } else if (!appClass.NetworkConnected()) {
            appClass.showConnectionLNetworkError(mContext);
        }
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
        binding.refreshFED.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //networkCheck
                if (appClass.NetworkConnected()) {
                    getEmployeeDetails(heirachyID, 10, 1, 0);
                } else if (!appClass.NetworkConnected()) {
                    appClass.showConnectionLNetworkError(mContext);
                }
                binding.refreshFED.setRefreshing(false);
            }
        });
    }

    private void init() {

        if (employeeDetailsList.size() >= 0) {
            if (page <= totalpage) {
                page = page - 1;
            }
            if (page == 0) {
                appClass.showSnackBar(mContext, "No Previous page ");
            } else {
                getEmployeeDetails(heirachyID, 10, page, 1);

            }
        }
    }

    private void nextinit() {
        if (i <= employeeDetailsList.size()) {
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
                getEmployeeDetails(heirachyID, 10, sizeone, 1);
            }
        }

    }

    private void editTextClick() {
        if (binding.page.getText().toString().length()!=0) {
            String temp = binding.page.getText().toString();
            size = Integer.parseInt(temp);
            if(size>totalpage){
                appClass.showSnackBar(mContext, "Source Page Not found");
            }else{
            getEmployeeDetails(heirachyID, 10, size, 1);}
        } else {
            appClass.showSnackBar(mContext, "Enter the Page No");
        }

    }

    private void setTag(ArrayList<String> filterName) {
        if (filterName != null) {
            String selected_filter_items = filterName.get(0).toString();
            String[] selected_filter_item = selected_filter_items.split(",");

            for (String tagName : selected_filter_item) {
                final Chip chip = new Chip(mContext);
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
        try {
            if (!value.equals("")) {
                JSONObject jsonObject = new JSONObject(value);
                siteId = jsonObject.getString("filterSiteID").replace("[", "").replace("]", "").replace(" ", "");
                blockId = jsonObject.getString("filterBlockId").replace("[", "").replace("]", "").replace(" ", "");
                floorId = jsonObject.getString("filterFloorId").replace("[", "").replace("]", "").replace(" ", "");
                FilterChip = jsonObject.getString("filterChip").replace("[", "").replace("]", "").replace(" ", "");

                if (!floorId.isEmpty()) {
                    heirachyID.add(floorId);
                } else if (!blockId.isEmpty()) {
                    heirachyID.add(blockId);
                } else if (!siteId.isEmpty()) {
                    heirachyID.add(siteId);
                }
                if (!FilterChip.isEmpty()) {
                    filterName.add(FilterChip);
                }
                setTag(filterName);
                Log.e(TAG, "getFilterValue: " + heirachyID.toString());
            } else {
                filterhasvalue=false;
                dialogBuilder =  new MaterialAlertDialogBuilder(mContext).setTitle("Filter Status!").
                        setMessage(getString(R.string.filterNoValue))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                appClass.navigateTo(getActivity(), R.id.action_employeeDetailsFragment_to_filterFragment);
                            }
                        });
                alertDialog=dialogBuilder.show();



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_bn) {
            appClass.navigateTo(getActivity(), R.id.action_employeeDetailsFragment_to_filterFragment);
        } else {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.onSupportNavigateUp();
        }
        return true;
    }

    private void getEmployeeDetails(ArrayList<String> heirachyID, int pagesize, int current_pageno, int mode) {
        employeeDetailsList.clear();
        List<Integer> newhierachyIdList = new ArrayList<>();
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
        String api = null;

        String index = "hierarchy_ids[]=", formedString = "";
        for (int i = 0; i < newhierachyIdList.size(); i++) {
            if (i < newhierachyIdList.size()) {
                formedString = formedString + index + newhierachyIdList.get(i) + "&";
            } else {
                formedString = formedString + index + newhierachyIdList.get(i);
            }
        }
        showProgress();
        try {

            if (mode == 0) {
                api = employee_details + "?" + formedString + "access_token=" + accessToken + "&per_page=" + pagesize;
            } else if (mode == 1) {

                api = employee_details + "?" + formedString + "access_token=" + accessToken + "&per_page=" + pagesize + "&page=" + current_pageno;
            }
            //String machinesApi = employee_details + "?" + formedString + "access_token=" + accessToken+"&per_page="+10;
            appClass.httpRequestNewNightHacks(mContext, api, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess_employee:" + object);
                    if (object != null) {

                        try {
                            dismissProgress();
                            JSONArray jsonArray = object.getJSONArray("employees");
                            totalpage = object.getInt("total_pages");
                            page = object.getInt("page");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id;
                                id = jsonObject.getInt("id");
                                String empCode = jsonObject.getString("employee_code");
                                if (empCode.trim().equals("") || empCode.equalsIgnoreCase("null")) {
                                    empCode = " - ";
                                } else {
                                    empCode = jsonObject.getString("employee_code");
                                }
                                String operator = jsonObject.getString("operator");
                                if (operator.trim().equals("") || operator.equalsIgnoreCase("null")) {
                                    operator = " - ";
                                } else {
                                    operator = jsonObject.getString("operator");
                                }
                                String status = jsonObject.getString("status");
                                if (status.trim().equals("") || status.equalsIgnoreCase("null")) {
                                    status = " - ";
                                } else {
                                    status = jsonObject.getString("status");
                                }
                                String site = jsonObject.getString("site");
                                if (site.trim().equals("") || site.equalsIgnoreCase("null")) {
                                    site = " - ";
                                } else {
                                    site = jsonObject.getString("site");
                                }
                                String lineNo = jsonObject.getString("line_id");
                                if (lineNo.trim().equals("") || lineNo.equalsIgnoreCase("null")) {
                                    lineNo = " - ";
                                } else {
                                    lineNo = jsonObject.getString("line_id");
                                }
                                String machinesNo = jsonObject.getString("machine_id");
                                if (machinesNo.trim().equals("") || lineNo.equalsIgnoreCase("null")) {
                                    machinesNo = " - ";
                                } else {
                                    machinesNo = jsonObject.getString("machine_id");
                                }
                                employeeDetailsList.add(new EmployeeDetailsModel(empCode, operator, status, site, lineNo, machinesNo, id));
                                adapter.setData(employeeDetailsList);

                            }
                            Log.d(TAG, "OnSuccess:_totlapage" + totalpage + "--------" + page);
                        } catch (Exception e) {
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
                        if(filterhasvalue==false){
                            alertDialog.dismiss();}
                    }
                    Log.d(TAG, "OnFailure1:" + statuscode);
                    appClass.showLNetworkError(mContext);

                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
            dismissProgress();
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
        b.putInt("ids", pos);
        Log.d(TAG, "OnItemClick:" + pos);
        appClass.navigateTo(getActivity(), R.id.action_employeeDetailsFragment_to_employeeProfileFragment, b);
    }

    @Override
    public void OnItemLongClick(int pos) {

    }

}

