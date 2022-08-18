package com.rax.iaam.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rax.iaam.Fragment.MachineDetailHistoryTable;
import com.rax.iaam.Model.MachineDetailHistoryModel;

import java.util.List;

public class MachineDetailPageAdapter extends FragmentPagerAdapter {
    int tabCount;
    List<MachineDetailHistoryModel> utilisationList;
    List<MachineDetailHistoryModel> operatorList;
    List<MachineDetailHistoryModel> maintenanceList;

    public MachineDetailPageAdapter(@NonNull FragmentManager fm, int behavior, List<MachineDetailHistoryModel> operatorList,
                                    List<MachineDetailHistoryModel> utilisationList,List<MachineDetailHistoryModel> maintenanceList) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.utilisationList = utilisationList;
        this.operatorList = operatorList;
        this.maintenanceList = maintenanceList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MachineDetailHistoryTable(0, utilisationList);
            case 1:
                return new MachineDetailHistoryTable(1,maintenanceList);
            case 2:
                return new MachineDetailHistoryTable(2,operatorList);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
