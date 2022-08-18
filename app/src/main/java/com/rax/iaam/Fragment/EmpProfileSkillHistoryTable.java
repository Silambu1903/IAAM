package com.rax.iaam.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rax.iaam.Adapter.EmployeeProfileSkillAdapter;
import com.rax.iaam.Model.EmployeeProfileSkillHsitoryModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEmpProfileSkillHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class EmpProfileSkillHistoryTable extends Fragment {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentEmpProfileSkillHistoryBinding binding;
    private List<EmployeeProfileSkillHsitoryModel> employeeProfileSkillHsitoryModelList = new ArrayList<>();
    private EmployeeProfileSkillAdapter adapter;
    List<EmployeeProfileSkillHsitoryModel> skillHistoryList;
    private static final String TAG = "EmployeeProfileskill";

    public EmpProfileSkillHistoryTable(List<EmployeeProfileSkillHsitoryModel> skillHistoryList) {
    this.skillHistoryList = skillHistoryList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_emp_profile_skill_history, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        adapter = new EmployeeProfileSkillAdapter();
        binding.rvEmpProfileSkillHistory.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvEmpProfileSkillHistory.setAdapter(adapter);
        adapter.setData(skillHistoryList);

    }
}
