package com.rax.iaam.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Model.EmployeeProfileSkillHsitoryModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeProfileSkillAdapter extends RecyclerView.Adapter<EmployeeProfileSkillAdapter.EmployeeProfileSkillViewHolder> {
    private List<EmployeeProfileSkillHsitoryModel> employeeProfileSkillHsitoryModelList;

    public EmployeeProfileSkillAdapter() {
        this.employeeProfileSkillHsitoryModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public EmployeeProfileSkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emp_profile_skill_history, parent, false);
        return new EmployeeProfileSkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeProfileSkillViewHolder holder, int position) {
        EmployeeProfileSkillHsitoryModel model = employeeProfileSkillHsitoryModelList.get(position);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            holder.machineCode.setTooltipText(model.getMachienCode());
            holder.machineCode.setText(model.getMachienCode());
        }else {
            holder.machineCode.setText(model.getMachienCode());
        }
       if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
           holder.needleRun.setTooltipText(model.getNeedleRun());
           holder.needleRun.setText(model.getNeedleRun());
       }else {
           holder.needleRun.setText(model.getNeedleRun());
       }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            holder.operationName.setTooltipText(model.getOperationName());
            holder.operationName.setText(model.getOperationName());
        }else {
            holder.operationName.setText(model.getOperationName());
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            holder.operationCode.setTooltipText(model.getOperationCode());
            holder.operationCode.setText(model.getOperationCode());
        }else {
            holder.operationCode.setText(model.getOperationCode());
        }


    }

    public void setData(List<EmployeeProfileSkillHsitoryModel> list) {
        if (employeeProfileSkillHsitoryModelList.size() != 0) {
            employeeProfileSkillHsitoryModelList.clear();
        }
        employeeProfileSkillHsitoryModelList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return employeeProfileSkillHsitoryModelList.size();
    }

    public class EmployeeProfileSkillViewHolder extends RecyclerView.ViewHolder {
        private TextView machineCode, operationCode, operationName, needleRun;

        public EmployeeProfileSkillViewHolder(@NonNull View itemView) {
            super(itemView);
            machineCode = itemView.findViewById(R.id.txt_EmpPro_Skill_MachineCode);
            operationCode = itemView.findViewById(R.id.txt_EmpPro_Skill_OperationCode);
            operationName = itemView.findViewById(R.id.txt_EmpPro_Skill_OperationName);
            needleRun = itemView.findViewById(R.id.txt_EmpPro_Skill_needleRun);
        }
    }
}
