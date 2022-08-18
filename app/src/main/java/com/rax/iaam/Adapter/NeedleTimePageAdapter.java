package com.rax.iaam.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rax.iaam.Fragment.NeedleTimeIdle;
import com.rax.iaam.Fragment.NeedleTimeMaintenance;
import com.rax.iaam.Fragment.NeedleTimeRun;

public class NeedleTimePageAdapter extends FragmentPagerAdapter {
    int tabCount;

    public NeedleTimePageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.tabCount = behavior;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NeedleTimeRun();
            case 1:
                return new NeedleTimeIdle();
            case 2:
                return new NeedleTimeMaintenance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
