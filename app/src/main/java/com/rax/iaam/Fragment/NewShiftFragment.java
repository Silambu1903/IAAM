package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.rax.iaam.Adapter.ShiftNewAdpater;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.ShiftNewModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.ShiftFragmentNewBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.accessTokenKey;
import static com.rax.iaam.Others.ApplicationClass.shiftHierarchies;

public class NewShiftFragment extends Fragment implements ItemClickListener {
    private ShiftFragmentNewBinding binding;
    private ApplicationClass appClass;
    private Context mContext;
    private List<ShiftNewModel> shiftNewModelList;
    private ShiftNewAdpater adpater;
    private static final String TAG = "NewShiftFragmentasd";
    int shiftId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.shift_fragment_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        adpater = new ShiftNewAdpater(this);
        binding.shiftRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        binding.shiftRecycler.setAdapter(adpater);
        Bundle b = getArguments();
        shiftId = b.getInt("line_id");
        Log.d(TAG, "onViewCreated: lineid" + shiftId);
        getShiftList();
        binding.refreshNewShift.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShiftList();
                binding.refreshNewShift.setRefreshing(false);
            }
        });

    }

    private void getShiftList() {
        //https://api.esgrobot.com/api/v1/hierarchies/43/shifts?access_token=-9hovN4_xRQXzPlcvv_y_NZv42iP-TGo-vldyQzg7TQ
        showProgress();
        String ShiftApi = shiftHierarchies + shiftId + "/shifts?" + accessTokenKey + accessToken;
        try {
            appClass.httpRequestNewNightHacks(mContext, ShiftApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess: raxshift" + object);
                    try {
                        dismissProgress();
                        JSONArray jsonArray = object.getJSONArray("shifts");
                        shiftNewModelList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                int shiftId;
                                String shiftName, loginTime, logoutTime;
                                shiftId = jsonObject.getInt("id");
                                shiftName = jsonObject.getString("name");
                                loginTime = formatDate(jsonObject.getString("start_time"));
                                logoutTime = formatDate(jsonObject.getString("end_time"));
                                shiftNewModelList.add(new ShiftNewModel(shiftId, shiftName, loginTime, logoutTime));
                                adpater.setData(shiftNewModelList);
                            } catch (Exception e) {
                                appClass.showLocalError(mContext);
                            }
                        }
                    } catch (Exception e) {
                        appClass.showLocalError(mContext);
                    }
                }

                @Override
                public void OnFailure(VolleyError error) {
                    dismissProgress();
                    appClass.showLocalError(mContext);
                }
            }, 2000);

        } catch (Exception e) {
            dismissProgress();
            appClass.showLocalError(mContext);
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
        String formateDate = new SimpleDateFormat("MM/dd/yy-HH:mm:ss").format(date);
        Log.d(TAG, "formatDate: MyDate" + date);
        return formateDate;
    }

    private void showProgress() {
        binding.progressCircularNewShift.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularNewShift.setVisibility(View.GONE);

    }

    @Override
    public void OnItemClick(int pos) {

    }

    @Override
    public void OnItemLongClick(int pos) {

    }
}
