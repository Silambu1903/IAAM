package com.rax.iaam.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rax.iaam.Fragment.AssetStatusHeatMap;
import com.rax.iaam.Fragment.AssetStatusHeatMapUptime;
import com.rax.iaam.Fragment.AssetStatusHeatMapUtilization;

import org.json.JSONObject;

import java.util.List;

public class AssetStatusPageAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    List<JSONObject> availCount;
    List<JSONObject> upTimeCount;
    List<JSONObject> utilizationCount;

    public AssetStatusPageAdapter(@NonNull FragmentManager fm, int behavior, List<JSONObject> availCount,
                                  List<JSONObject> upTimeCount, List<JSONObject> utilizationCount) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.availCount = availCount;
        this.upTimeCount = upTimeCount;
        this.utilizationCount = utilizationCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AssetStatusHeatMap(availCount,"Availability");
            case 1:
                return new AssetStatusHeatMapUptime(upTimeCount,"Uptime");
            case 2:
                return new AssetStatusHeatMapUtilization(utilizationCount,"Utilization");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
