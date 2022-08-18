package com.rax.iaam.Model;

public class EmployeeProfileSkillHsitoryModel {
    private String machienCode;
    private String operationCode;
    private String OperationName;
    private String NeedleRun;

    public EmployeeProfileSkillHsitoryModel(String machienCode, String operationCode, String operationName, String needleRun) {
        this.machienCode = machienCode;
        this.operationCode = operationCode;
        OperationName = operationName;
        NeedleRun = needleRun;
    }

    public String getMachienCode() {
        return machienCode;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public String getOperationName() {
        return OperationName;
    }

    public String getNeedleRun() {
        return NeedleRun;
    }
}
