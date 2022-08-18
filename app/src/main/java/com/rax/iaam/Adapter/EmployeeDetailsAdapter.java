package com.rax.iaam.Adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.rax.iaam.Callbacks.ItemClickListener;
import com.rax.iaam.Model.EmployeeDetailsModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetailsAdapter extends Adapter<EmployeeDetailsAdapter.EmployeeDetailsViewHolder> {
    private List<EmployeeDetailsModel> employeeDetailsModelList;
    private ItemClickListener callback;

    public EmployeeDetailsAdapter(ItemClickListener callback) {
        employeeDetailsModelList = new ArrayList<>();
        this.callback = callback;
    }

    @NonNull
    @Override
    public EmployeeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_details, parent, false);
        return new EmployeeDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeDetailsViewHolder holder, int position) {
        EmployeeDetailsModel model = employeeDetailsModelList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.empNo.setTooltipText(model.getEmpNo());
            holder.empNo.setText(model.getEmpNo());
        }else {
            holder.empNo.setText(model.getEmpNo());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.lineNo.setTooltipText(model.getLineNo());
            holder.lineNo.setText(model.getLineNo());
        }else {
            holder.lineNo.setText(model.getLineNo());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.machineNo.setTooltipText(model.getMachineNo());
            holder.machineNo.setText(model.getMachineNo());
        }else {
            holder.machineNo.setText(model.getMachineNo());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.operator.setTooltipText(model.getOperator());
            holder.operator.setText(model.getOperator());
        }else {
            holder.operator.setText(model.getOperator());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.site.setTooltipText(model.getSite());
            holder.site.setText(model.getSite());
        }else {
            holder.site.setText(model.getSite());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.status.setTooltipText(model.getStatus());
            holder.status.setText(model.getStatus());
        }else {
            holder.status.setText(model.getStatus());
        }

        if (model.getStatus().equals("Active")) {
            holder.status.setTextColor(Color.parseColor("#0F9D58"));
        } else {
            holder.status.setTextColor(Color.parseColor("#DB4437"));
        }
    }

    @Override
    public int getItemCount() {
        return employeeDetailsModelList.size();
    }

    public void setData(List<EmployeeDetailsModel> list) {
        if (employeeDetailsModelList.size() != 0) {
            employeeDetailsModelList.clear();
        }
        employeeDetailsModelList.addAll(list);
        notifyDataSetChanged();
    }

    public class EmployeeDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView empNo, operator, status, site, lineNo, machineNo;

        public EmployeeDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            empNo = itemView.findViewById(R.id.txt_empDetails_empNo);
            operator = itemView.findViewById(R.id.txt_empDetails_operator);
            status = itemView.findViewById(R.id.txt_empDetails_status);
            site = itemView.findViewById(R.id.txt_empDetails_site);
            lineNo = itemView.findViewById(R.id.txt_empDetails_lineNo);
            machineNo = itemView.findViewById(R.id.txt_empDetails_machineNo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                    callback.OnItemClick(employeeDetailsModelList.get(getAdapterPosition()).getid()); //get id machine position
                }
            });
        }
    }
}
