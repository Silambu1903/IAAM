package com.rax.iaam.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rax.iaam.Model.MachineDetailHistoryModel;
import com.rax.iaam.R;

import java.util.ArrayList;
import java.util.List;

public class MachineDetailHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MachineDetailHistoryModel> dataSet;
    Context mContext;

    public MachineDetailHistoryAdapter(List<MachineDetailHistoryModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        dataSet=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case MachineDetailHistoryModel.UTILIZATION_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_machine_detail_utilization, parent, false);
                return new UtilizationViewHolder(view);
            case MachineDetailHistoryModel.UTILIZATION_HEAD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md_utilization_head, parent, false);
                return new UtilizationHeadViewHolder(view);
            case MachineDetailHistoryModel.MAINTENANCE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_machine_detail_maintenance, parent, false);
                return new MaintenanceViewHolder(view);
            case MachineDetailHistoryModel.MAINTENANCE_HEAD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md_maintenance_head, parent, false);
                return new MaintenanceHeadViewHolder(view);
            case MachineDetailHistoryModel.OPERATOR_TYPE:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_machine_detail_operator, parent, false);
                return new OperatorViewHolder(view);
            case MachineDetailHistoryModel.OPERATOR_HEAD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md_operator_head, parent, false);
                return new OperatorHeadViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MachineDetailHistoryModel model = dataSet.get(position);
if(model == null){
    ((MaintenanceViewHolder) holder).date.setText("Nil");
    ((MaintenanceViewHolder) holder).issue.setText("Nil");
    ((MaintenanceViewHolder) holder).reasonCode.setText("Nil");
    ((MaintenanceViewHolder) holder).status.setText("Nil");
    ((MaintenanceViewHolder) holder).assignedTo.setText("Nil");
}
        if (model != null) {
            switch (model.getType()) {
                case MachineDetailHistoryModel.UTILIZATION_TYPE:
                    ((UtilizationViewHolder) holder).date.setText(model.getFromDate());
                    ((UtilizationViewHolder) holder).uptime.setText(model.getUptime()+"%");
                    ((UtilizationViewHolder) holder).utilization.setText(model.getUtilization()+"%");
                    ((UtilizationViewHolder) holder).operator.setText(model.getOperator());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ((UtilizationViewHolder) holder).date.setTooltipText(model.getFromDate());
                        ((UtilizationViewHolder) holder).uptime.setTooltipText(model.getUptime());
                        ((UtilizationViewHolder) holder).utilization.setTooltipText(model.getUtilization());
                        ((UtilizationViewHolder) holder).operator.setTooltipText(model.getOperator());
                    }
                    break;
                case MachineDetailHistoryModel.MAINTENANCE_TYPE:
                    if(dataSet.size()==0){
                        ((MaintenanceViewHolder) holder).date.setText("Nil");
                        ((MaintenanceViewHolder) holder).issue.setText("Nil");
                        ((MaintenanceViewHolder) holder).reasonCode.setText("Nil");
                        ((MaintenanceViewHolder) holder).status.setText("Nil");
                        ((MaintenanceViewHolder) holder).assignedTo.setText("Nil");
                    }else {
                        ((MaintenanceViewHolder) holder).date.setText(model.getFromDate());
                        ((MaintenanceViewHolder) holder).issue.setText(String.valueOf(model.getIssueID()));
                        ((MaintenanceViewHolder) holder).reasonCode.setText(model.getCode());
                        ((MaintenanceViewHolder) holder).status.setText(model.getStatus());
                        ((MaintenanceViewHolder) holder).assignedTo.setText(model.getAssignedTo());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            ((MaintenanceViewHolder) holder).date.setTooltipText(model.getFromDate());
                            ((MaintenanceViewHolder) holder).issue.setTooltipText(String.valueOf(model.getIssueID()));
                            ((MaintenanceViewHolder) holder).reasonCode.setTooltipText(model.getCode());
                            ((MaintenanceViewHolder) holder).status.setTooltipText(model.getStatus());
                            ((MaintenanceViewHolder) holder).assignedTo.setTooltipText(model.getAssignedTo());
                        }
                    }
                    break;
                case MachineDetailHistoryModel.OPERATOR_TYPE:
                    ((OperatorViewHolder) holder).fDate.setText(model.getFromDate());
                    ((OperatorViewHolder) holder).tDate.setText(model.getToDate());
                    ((OperatorViewHolder) holder).empCode.setText(model.getCode());
                    ((OperatorViewHolder) holder).operator.setText(model.getOperator());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ((OperatorViewHolder) holder).fDate.setTooltipText(model.getFromDate());
                        ((OperatorViewHolder) holder).tDate.setTooltipText(model.getToDate());
                        ((OperatorViewHolder) holder).empCode.setTooltipText(model.getCode());
                        ((OperatorViewHolder) holder).operator.setTooltipText(model.getOperator());
                    }
                    break;

            }
        }

    }

    @Override
    public int getItemCount() {

        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getType()) {

            case 0:
                return MachineDetailHistoryModel.UTILIZATION_TYPE;
            case 1:
                return MachineDetailHistoryModel.MAINTENANCE_TYPE;
            case 2:
                return MachineDetailHistoryModel.OPERATOR_TYPE;
            case 3:
                return MachineDetailHistoryModel.UTILIZATION_HEAD_TYPE;
            case 4:
                return MachineDetailHistoryModel.MAINTENANCE_HEAD_TYPE;
            case 5:
                return MachineDetailHistoryModel.OPERATOR_HEAD_TYPE;
            default:
                return -1;
        }


    }

    public void setData(List<MachineDetailHistoryModel> list) {
        if (dataSet.size() != 0) {
            dataSet.clear();
        }
        dataSet.addAll(list);
        notifyDataSetChanged();
    }

    public static class UtilizationViewHolder extends RecyclerView.ViewHolder {
        TextView date, uptime, utilization, operator;

        public UtilizationViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_md_utilization_date);
            uptime = itemView.findViewById(R.id.txt_md_utilization_uptime);
            utilization = itemView.findViewById(R.id.txt_md_utilization_utilization);
            operator = itemView.findViewById(R.id.txt_md_utilization_operator);
        }
    }

    public static class UtilizationHeadViewHolder extends RecyclerView.ViewHolder{
        public UtilizationHeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public static class MaintenanceViewHolder extends RecyclerView.ViewHolder{
        TextView date,issue,reasonCode,status,assignedTo;
        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_md__main_date);
            issue = itemView.findViewById(R.id.txt_md__main_issueID);
            reasonCode = itemView.findViewById(R.id.txt_md__main_reasonCode);
            status = itemView.findViewById(R.id.txt_md__main_status);
            assignedTo=itemView.findViewById(R.id.txt_md__main_assignedTo);

        }
    }

    public static class MaintenanceHeadViewHolder extends RecyclerView.ViewHolder{
        public MaintenanceHeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



    public static class OperatorViewHolder extends RecyclerView.ViewHolder{
        private TextView fDate,tDate,empCode,operator;
        public OperatorViewHolder(@NonNull View itemView) {
            super(itemView);
            fDate=itemView.findViewById(R.id.txt_MD_operator_FDate);
            tDate=itemView.findViewById(R.id.txt_MD_operator_TDate);
            empCode=itemView.findViewById(R.id.txt_MD_operator_EmpCode);
            operator=itemView.findViewById(R.id.txt_MD_operator_operator);
        }
    }

    public static class OperatorHeadViewHolder extends RecyclerView.ViewHolder{
        public OperatorHeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
