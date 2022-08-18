package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Build;
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
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Adapter.AssetStatusHeatMapAdapter;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentAssetStatusHeatmapBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssetStatusHeatMapUptime extends Fragment {
    private Context context;
    private FragmentAssetStatusHeatmapBinding binding;
    private AssetStatusHeatMapAdapter adapter;

    private List<JSONObject> availCount;
    private String uptime;


    public AssetStatusHeatMapUptime(List<JSONObject> upTimeCount, String uptime) {
        this.availCount = upTimeCount;
        this.uptime = uptime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_asset_status_heatmap, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        if (availCount.isEmpty()){
            binding.heatMapDataEmpty.setVisibility(View.VISIBLE);
        }else {
            binding.heatMapDataEmpty.setVisibility(View.GONE);
        }
        binding.heatMapTitle.setText(uptime);
        binding.rvAssetStatusHeatMap.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new AssetStatusHeatMapAdapter(getList("uptime"));
        binding.rvAssetStatusHeatMap.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private LinkedHashMap< String,LinkedHashMap<String, String>> getList(String availability) {
//Map<String,LinkedHashMap<LinkedHashMap<String, String>, String> > totalValue = new HashMap<String,LinkedHashMap<LinkedHashMap<String, String>, String>>();
        LinkedHashMap< String,LinkedHashMap<String, String>> values = new LinkedHashMap<>();
        LinkedHashMap<String, String> newHash = null;
        String name = null;
        try {
            for (int i = 0; i < availCount.size(); i++) {
                newHash = new LinkedHashMap<>();
                JSONArray monthWiseList = availCount.get(i).getJSONArray("month_wise_details");
                //name = availCount.get(i).getString("name");
                name = availCount.get(i).getString("location");
                for (int k = 0; k < monthWiseList.length(); k++) {
                    newHash.put(monthWiseList.getJSONObject(k).getString("month")
                            , monthWiseList.getJSONObject(k).getString(availability));
                }
                values.put(name,newHash);
              //  totalValue.put(location,values);

            }
           // Log.d("hkj", "getList: "+totalValue.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values;
    }

}
