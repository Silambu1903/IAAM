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
import com.rax.iaam.Adapter.BlockNewAdapter;
import com.rax.iaam.Adapter.SiteNewAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.BlockNewModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.BlockFragmentNewBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.hierarchies;

public class NewBlockFragment extends Fragment implements ItemClickListener {
    private BlockFragmentNewBinding binding;
    private ApplicationClass appClass;
    private Context mContext;
    private List<BlockNewModel> blockNewModelList;
    private BlockNewAdapter adapter;
    private static final String TAG = "NewBlockFragmentList";
    int siteId;
    private ArrayList<Integer> blockCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.block_fragment_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        adapter = new BlockNewAdapter(this);
        binding.blockWiseList.setLayoutManager(new LinearLayoutManager(mContext));
        binding.blockWiseList.setAdapter(adapter);
        Bundle b = getArguments();
        siteId = b.getInt("ids");
        Log.d(TAG, "onViewCreated: siteq" + siteId);
        getBlockList();
        binding.refreshNewBlock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBlockList();
                binding.refreshNewBlock.setRefreshing(false);
            }
        });
    }

    private void getBlockList() {
        //https://api.esgrobot.com/api/v1/hierarchies?hierarchy_id[]=1&access_token=7kLUrgEVqnAowlpXkMVKye0fM2n_Jk2ySDMLyU0EL6k
        showProgress();
        try {
            String blockList = hierarchies + siteId + "&" + "access_token=" + accessToken;
            appClass.httpRequestNewNightHacks(mContext, blockList, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess:block" + object);
                    try {
                        dismissProgress();
                        JSONArray jsonSiteArray = object.getJSONArray("hierarchies");
                        blockNewModelList = new ArrayList<>();
                        blockCheck = new ArrayList<>();
                        for (int i = 0; i < jsonSiteArray.length(); i++) {
                            JSONObject jsonObject = jsonSiteArray.getJSONObject(i);
                            try {
                                String blockName;
                                int blockId;
                                blockId = jsonObject.getInt("id");
                                blockName = jsonObject.getString("name");
                                if (!blockCheck.contains(blockId)) {
                                    blockCheck.add(blockId);
                                    blockNewModelList.add(new BlockNewModel(blockId, blockName));
                                }
                                adapter.setData(blockNewModelList);

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
        binding.progressCircularNewBlock.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        binding.progressCircularNewBlock.setVisibility(View.GONE);

    }


    @Override
    public void OnItemClick(int pos) {
        Bundle b = new Bundle();
        b.putInt("ids", pos);
        appClass.navigateTo(getActivity(), R.id.action_newBlockFragment_to_newFloorFragment, b);
    }

    @Override
    public void OnItemLongClick(int pos) {

    }
}
