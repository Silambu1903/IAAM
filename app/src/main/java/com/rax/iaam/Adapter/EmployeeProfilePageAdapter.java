package com.rax.iaam.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rax.iaam.Fragment.EmpProfileAssignmentsTable;
import com.rax.iaam.Fragment.EmpProfilePerformanceChart;
import com.rax.iaam.Fragment.EmpProfileSkillHistoryTable;
import com.rax.iaam.Model.EmployeeProfileAssignmentModel;
import com.rax.iaam.Model.EmployeeProfileSkillHsitoryModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeProfilePageAdapter extends FragmentPagerAdapter {
    int tabCount;
    List<EmployeeProfileSkillHsitoryModel> skillHistoryList;
    List<EmployeeProfileAssignmentModel> assigmentsList;
    String[] performanceGraphName;
    ArrayList<Double> performanceGraphValue;
    int empId;
    public EmployeeProfilePageAdapter(@NonNull FragmentManager fm, int behavior, List<EmployeeProfileSkillHsitoryModel> skillHistoryList, List<EmployeeProfileAssignmentModel> assigmentsList, String[] performanceGraphName, ArrayList<Double> performanceGraphValue, int empId) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.skillHistoryList = skillHistoryList;
        this.assigmentsList=assigmentsList;
        this.performanceGraphName = performanceGraphName;
        this.performanceGraphValue = performanceGraphValue;
        this.empId=empId;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EmpProfileSkillHistoryTable(skillHistoryList);
            case 1:
                return new EmpProfileAssignmentsTable(assigmentsList);
            case 2:
                return new EmpProfilePerformanceChart(performanceGraphName,performanceGraphValue,empId);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
