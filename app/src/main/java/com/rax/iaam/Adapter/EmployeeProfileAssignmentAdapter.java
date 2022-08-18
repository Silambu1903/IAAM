package com.rax.iaam.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Model.EmployeeProfileAssignmentModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeProfileAssignmentAdapter extends RecyclerView.Adapter<EmployeeProfileAssignmentAdapter.EmployeeProfileAssignmentViewHolder> {
    private List<EmployeeProfileAssignmentModel> employeeProfileAssignmentModelList;

    public EmployeeProfileAssignmentAdapter() {
        this.employeeProfileAssignmentModelList = new ArrayList<>();
    }

    @NonNull
    @Override
    public EmployeeProfileAssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emp_profile_assignment,parent,false);
        return new EmployeeProfileAssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeProfileAssignmentViewHolder holder, int position) {
        EmployeeProfileAssignmentModel model=employeeProfileAssignmentModelList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.machine.setTooltipText(model.getMachine());
            holder.machine.setText(model.getMachine());
        }else {
            holder.machine.setText(model.getMachine());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.date.setTooltipText(model.getDate());
            holder.date.setText(model.getDate());
        }else {
            holder.date.setText(model.getDate());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.line.setTooltipText(model.getLine());
            holder.line.setText(model.getLine());
        }else {
            holder.line.setText(model.getLine());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.site.setTooltipText(model.getSite());
            holder.site.setText(model.getSite());
        }else {
            holder.site.setText(model.getSite());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.status.setTooltipText(model.getSatus());
            holder.status.setText(model.getSatus());
        }else {
            holder.status.setText(model.getSatus());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.operation.setTooltipText(model.getOperation());
            holder.operation.setText(model.getOperation());
        }else {
            holder.operation.setText(model.getOperation());
        }
     }

    @Override
    public int getItemCount() {
        return employeeProfileAssignmentModelList.size();
    }

    public void setData(List<EmployeeProfileAssignmentModel> list){
        if(employeeProfileAssignmentModelList.size()!=0){
            employeeProfileAssignmentModelList.clear();
        }
        employeeProfileAssignmentModelList.addAll(list);
        notifyDataSetChanged();
    }


    public class EmployeeProfileAssignmentViewHolder extends RecyclerView.ViewHolder{
        private TextView date,status,site,line,machine,operation;
        public EmployeeProfileAssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.txt_EP_assignment_date);
            status=itemView.findViewById(R.id.txt_EP_assignment_status);
            site=itemView.findViewById(R.id.txt_EP_assignment_site);
            line=itemView.findViewById(R.id.txt_EP_assignment_line);
            machine=itemView.findViewById(R.id.txt_EP_assignment_machine);
            operation=itemView.findViewById(R.id.txt_EP_assignment_operation);

        }
    }
}
