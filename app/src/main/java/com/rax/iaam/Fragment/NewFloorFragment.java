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
import com.rax.iaam.Adapter.FloorNewAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.NewFloorModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FloorFragmentNewBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.hierarchies;

public class NewFloorFragment extends Fragment implements ItemClickListener {
    private FloorFragmentNewBinding binding;
    private ApplicationClass appClass;
    private Context mContext;
    private List<NewFloorModel> floorList;
    private ArrayList<Integer> floorCheck;
    private FloorNewAdapter adapter;
    private static final String TAG = "NewFloorFragmentav";
    int floorId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.floor_fragment_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        adapter = new FloorNewAdapter(this);
        binding.floorRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        binding.floorRecycler.setAdapter(adapter);
        Bundle b = getArguments();
        floorId = b.getInt("ids");
        getFloorList();
        binding.refreshNewFloor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFloorList();
                binding.refreshNewFloor.setRefreshing(false);
            }
        });
    }

    private void getFloorList() {
        showProgress();
        try {
            //https://api.esgrobot.com/api/v1/hierarchies?hierarchy_id[]=2&access_token=fDbIzAsqbNE8oVu48lvDbg57iq9Mx9yT4aMYH8n_bBA
            String floorApi = hierarchies + floorId + "&" + "access_token=" + accessToken;
            appClass.httpRequestNewNightHacks(mContext, floorApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess: rax" + object);
                    try {
                        dismissProgress();
                        JSONArray jsonArray = object.getJSONArray("hierarchies");
                        floorList = new ArrayList<>();
                        floorCheck = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            try {
                                int FloorId;
                                String FloorName;
                                FloorId = jsonObject.getInt("id");
                                FloorName = jsonObject.getString("name");
                                if (!floorCheck.contains(FloorId)) {
                                    floorCheck.add(FloorId);
                                    floorList.add(new NewFloorModel(FloorId, FloorName));
                                }
                                adapter.setData(floorList);
                                Log.d(TAG, "OnSuccess: raxad" + floorList.toString());

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
        binding.progressCircularNewFloor.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularNewFloor.setVisibility(View.GONE);

    }

    @Override
    public void OnItemClick(int pos) {
        Bundle b = new Bundle();
        b.putInt("ids", pos);
        appClass.navigateTo(getActivity(), R.id.action_newFloorFragment_to_newLineFragment, b);

    }

    @Override
    public void OnItemLongClick(int pos) {

    }
}
