package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.rax.iaam.Adapter.LineNewAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.NewLineModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.LineFragmentNewBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.hierarchies;

public class NewLineFragment extends Fragment implements ItemClickListener {
    private LineFragmentNewBinding binding;
    private ApplicationClass appClass;
    private Context mContext;
    private List<NewLineModel> lineModelList;
    private ArrayList<Integer> lineCheck;
    private LineNewAdapter adapter;
    private static final String TAG = "NewLineFragmentak";
    int LineId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.line_fragment_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        adapter = new LineNewAdapter(this);
        binding.lineRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        binding.lineRecycler.setAdapter(adapter);
        Bundle b = getArguments();
        LineId = b.getInt("ids");
        getLineList();
        binding.refreshNewLine.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLineList();
                binding.refreshNewLine.setRefreshing(false);
            }
        });
    }

    private void getLineList() {
        showProgress();
        String LineApi = hierarchies + LineId + "&" + "access_token=" + accessToken;
        try {
            appClass.httpRequestNewNightHacks(mContext, LineApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess:line " + object);
                    try {
                        dismissProgress();
                        JSONArray jsonArray = object.getJSONArray("hierarchies");
                        lineModelList = new ArrayList<>();
                        lineCheck = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                int LineId;
                                String LineName;
                                LineId = jsonObject.getInt("id");
                                LineName = jsonObject.getString("name");
                                if (!lineCheck.contains(LineId)) {
                                    lineCheck.add(LineId);
                                    lineModelList.add(new NewLineModel(LineId, LineName));
                                }
                                adapter.setData(lineModelList);
                                Log.d(TAG, "OnSuccess: list" + lineModelList.size());
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

    private void showProgress() {
        binding.progressCircularNewLine.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularNewLine.setVisibility(View.GONE);

    }

    @Override
    public void OnItemClick(int pos) {
        Bundle b = new Bundle();
        b.putInt("line_id", pos);
        Log.d(TAG, "OnItemClick: ak" + b);
        appClass.navigateTo(getActivity(), R.id.action_newLineFragment_to_newShiftFragment, b);
    }

    @Override
    public void OnItemLongClick(int pos) {

    }
}
