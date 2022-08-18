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

import com.rax.iaam.Adapter.EmployeeProfileAssignmentAdapter;
import com.rax.iaam.Model.EmployeeProfileAssignmentModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentEmpProfileAssignmentsBinding;

import java.util.ArrayList;
import java.util.List;
public class EmpProfileAssignmentsTable extends Fragment {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentEmpProfileAssignmentsBinding binding;
    private List<EmployeeProfileAssignmentModel> employeeProfileAssignmentModelList=new ArrayList<>();
    private EmployeeProfileAssignmentAdapter adapter;
    List<EmployeeProfileAssignmentModel> assigmentsList;

    public EmpProfileAssignmentsTable(List<EmployeeProfileAssignmentModel> assigmentsList) {
        this.assigmentsList = assigmentsList;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_emp_profile_assignments,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass=(ApplicationClass)getActivity().getApplication();
        mContext=getContext();
        adapter=new EmployeeProfileAssignmentAdapter();
        binding.rvEmpProfileAssignment.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvEmpProfileAssignment.setAdapter(adapter);
        adapter.setData(assigmentsList);

    }
}
