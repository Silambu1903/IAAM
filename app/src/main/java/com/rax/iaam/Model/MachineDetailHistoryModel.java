package com.rax.iaam.Model;

import com.rax.iaam.Fragment.MachineDetail;

import org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle;

public class MachineDetailHistoryModel {
    public static final int UTILIZATION_TYPE=0;
    public static final int MAINTENANCE_TYPE=1;
    public static final int OPERATOR_TYPE=2;
    public static final int UTILIZATION_HEAD_TYPE=3;
    public static final int MAINTENANCE_HEAD_TYPE=4;
    public static final int OPERATOR_HEAD_TYPE=5;
    public static final int MAINTENANCE_TYPES=6;
    public static final int MAINTENANCE_HEAD_TYPES=7;
    private int id;
    private int type;
    private String fromDate;
    private String toDate;
    private String uptime;
    private String utilization;
    private String operator;
    private Integer issueID;

    public static int getUtilizationType() {
        return UTILIZATION_TYPE;
    }

    public static int getMaintenanceType() {
        return MAINTENANCE_TYPE;
    }

    public static int getOperatorType() {
        return OPERATOR_TYPE;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getUptime() {
        return uptime;
    }

    public String getUtilization() {
        return utilization;
    }

    public String getOperator() {
        return operator;
    }

    public Integer getIssueID() { return issueID;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    private String code;
    private String status;
    private String assignedTo;

    public MachineDetailHistoryModel(int type, String fromDate, String uptime, String utilization, String operator) {
        this.type = type;
        this.fromDate = fromDate;
        this.uptime = uptime;
        this.utilization = utilization;
        this.operator = operator;
    }

    public MachineDetailHistoryModel(int type, String fromDate, Integer issueID, String code, String status, String assignedTo) {
        this.type = type;
        this.fromDate = fromDate;
        this.issueID = issueID;
        this.code = code;
        this.status = status;
        this.assignedTo = assignedTo;
    }

    public MachineDetailHistoryModel(int id, int type, String fromDate, String toDate, String operator, String code, String assigned) {
        this.id = id;
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.operator = operator;
        this.code = code;
    }
}
