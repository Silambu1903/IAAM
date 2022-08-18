package com.rax.iaam.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rax.iaam.Fragment.SessionPunctualityBreak;
import com.rax.iaam.Fragment.SessionPunctualityWork;

public class SessionPunctualityPageAdapter extends FragmentPagerAdapter {
    int tabCount;


    public SessionPunctualityPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.tabCount = behavior;


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SessionPunctualityWork();
            case 1:
                return new SessionPunctualityBreak();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
