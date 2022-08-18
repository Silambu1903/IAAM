package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.rax.iaam.Adapter.SiteNewAdapter;
import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Callbacks.VolleyCallback;
import com.rax.iaam.Model.SiteNewModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.SiteFragmentNewBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rax.iaam.Others.ApplicationClass.accessToken;
import static com.rax.iaam.Others.ApplicationClass.factoryWS;

public class NewSiteFragment extends Fragment implements ItemClickListener {
    private ApplicationClass appClass;
    SiteFragmentNewBinding mBinding;
    private Context mContext;
    private List<SiteNewModel> siteNewList;
    private ArrayList<Integer> siteCheck;
    private SiteNewAdapter adapter;
    private static final String TAG = "SiteNew";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.site_fragment_new, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        adapter = new SiteNewAdapter(this);
        mBinding.siteRecyelerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.siteRecyelerView.setAdapter(adapter);
        getSiteList();
        mBinding.refreshNewSite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSiteList();
                mBinding.refreshNewSite.setRefreshing(false);
            }
        });


    }

    private void getSiteList() {
        showProgress();
        try {
            String SiteApi = factoryWS + "?" + "access_token=" + accessToken;
            appClass.httpRequestNewNightHacks(mContext, SiteApi, null, Request.Method.GET, new VolleyCallback() {
                @Override
                public void OnSuccess(JSONObject object) {
                    Log.d(TAG, "OnSuccess: silam" + object);
                    try {
                        dismissProgress();
                        JSONArray jsonSiteArray = object.getJSONArray("data");
                        siteNewList = new ArrayList<>();
                        siteCheck = new ArrayList<>();
                        for (int i = 0; i < jsonSiteArray.length(); i++) {
                            JSONObject jsonObject = jsonSiteArray.getJSONObject(i);
                            try {
                                String siteName;
                                int siteId;
                                siteName = jsonObject.getString("site_name");
                                siteId = jsonObject.getInt("site_id");
                                if (!siteCheck.contains(siteId)) {
                                    siteCheck.add(siteId);
                                    siteNewList.add(new SiteNewModel(siteId, siteName));
                                }
                                adapter.setData(siteNewList);
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
                    appClass.showLNetworkError(mContext);
                }
            }, 2000);

        } catch (Exception e) {
            dismissProgress();
            appClass.showLocalError(mContext);
        }
    }

    private void showProgress() {
        mBinding.progressCircularNewSite.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mBinding.progressCircularNewSite.setVisibility(View.GONE);

    }

    @Override
    public void OnItemClick(int pos) {
        Bundle b = new Bundle();
        b.putInt("ids", pos);
        Log.d(TAG, "OnItemClick: posi" + b);
        appClass.navigateTo(getActivity(), R.id.action_newSiteFragment_to_newBlockFragment, b);

    }

    @Override
    public void OnItemLongClick(int pos) {

    }
}
