package com.rax.iaam.Model;

public class EmployeeDetailsModel {
    private String empNo;
    private String Operator;
    private String status;
    private String site;
    private String lineNo;
    private String machineNo;

    private int id;

    public EmployeeDetailsModel(String empNo, String operator, String status, String site, String lineNo, String machineNo, int id) {
        this.empNo = empNo;
        Operator = operator;
        this.status = status;
        this.site = site;
        this.lineNo = lineNo;
        this.machineNo = machineNo;

        this.id= id;
    }

    public String getEmpNo() {
        return empNo;
    }

    public String getOperator() {
        return Operator;
    }

    public String getStatus() {
        return status;
    }

    public String getSite() {
        return site;
    }

    public String getLineNo() {
        return lineNo;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public int getid(){
        return id;
    }
}
