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

import com.rax.iaam.Adapter.MachineDetailHistoryAdapter;
import com.rax.iaam.Model.MachineDetailHistoryModel;
import com.rax.iaam.Others.ApplicationClass;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentMachineDetailHistoryTableBinding;

import java.util.ArrayList;
import java.util.List;

public class MachineDetailHistoryTable extends Fragment {
    private ApplicationClass appClass;
    private Context mContext;
    private FragmentMachineDetailHistoryTableBinding binding;
    private List<MachineDetailHistoryModel> utilizationList = new ArrayList<>();
    private MachineDetailHistoryAdapter adapter;
    private int pageNo;
    List<MachineDetailHistoryModel> utilisationList;
    List<MachineDetailHistoryModel> operatorList;
    List<MachineDetailHistoryModel> maintenanceList;
    List<MachineDetailHistoryModel> maintenanceListTemp;

    public MachineDetailHistoryTable(int pageNo, List<MachineDetailHistoryModel> utilisationList) {
        this.pageNo = pageNo;
        this.utilisationList = utilisationList;
        this.operatorList = utilisationList;
        this.maintenanceList = utilisationList;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_machine_detail_history_table, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appClass = (ApplicationClass) getActivity().getApplication();
        mContext = getContext();
        maintenanceListTemp = new ArrayList<>();
        adapter = new MachineDetailHistoryAdapter(utilizationList, mContext);
        binding.rvMdHistories.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvMdHistories.setAdapter(adapter);
        switch (pageNo) {
            case 0:
                if (utilisationList.size() == 0 || utilisationList.isEmpty()) {
                    binding.maintanceone.setVisibility(View.GONE);
                    binding.maintancetwo.setVisibility(View.VISIBLE);
                    binding.maintancethree.setVisibility(View.GONE);
                    binding.text.setVisibility(View.VISIBLE);
                }
                getUtilization();
                break;
            case 1:
                if (utilisationList.size() == 0 || utilisationList.isEmpty()) {
                    binding.maintanceone.setVisibility(View.VISIBLE);
                    binding.maintancetwo.setVisibility(View.GONE);
                    binding.maintancethree.setVisibility(View.GONE);
                    binding.text.setVisibility(View.VISIBLE);
                }
                getMaintenance();
                break;
            case 2:
                if (utilisationList.size() == 0 || utilisationList.isEmpty()) {
                    binding.maintanceone.setVisibility(View.GONE);
                    binding.maintancetwo.setVisibility(View.GONE);
                    binding.maintancethree.setVisibility(View.VISIBLE);
                    binding.text.setVisibility(View.VISIBLE);
                }
                getOperator();
                break;
        }

    }


    private void getUtilization() {
        adapter.setData(utilisationList);
    }

    private void getMaintenance() {
        adapter.setData(maintenanceList);
    }

    private void getOperator() {
        adapter.setData(operatorList);
    }
}


