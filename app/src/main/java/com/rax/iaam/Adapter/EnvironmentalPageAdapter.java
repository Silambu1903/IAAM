package com.rax.iaam.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.rax.iaam.Fragment.EnvironmentalParamertTemperature;
import com.rax.iaam.Fragment.EnvironmentalParameterAirQuality;
import com.rax.iaam.Fragment.EnvironmentalParameterHumidity;
import com.rax.iaam.Fragment.EnvironmentalParameterLux;
import com.rax.iaam.Fragment.EnvironmentalParameterNoise;

public class EnvironmentalPageAdapter extends FragmentPagerAdapter {
    int tabCount;

    public EnvironmentalPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EnvironmentalParamertTemperature();
            case 1:
                return new EnvironmentalParameterLux();
            case 2:
                return new EnvironmentalParameterHumidity();
            case 3:
                return new EnvironmentalParameterAirQuality();
            case 4:
                return new EnvironmentalParameterNoise();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
