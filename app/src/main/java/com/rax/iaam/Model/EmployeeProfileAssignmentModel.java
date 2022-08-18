package com.rax.iaam.Model;

public class EmployeeProfileAssignmentModel{
    String date;
    String satus;
    String site;
    String line;
    String machine;
    String operation;

    public EmployeeProfileAssignmentModel(String date, String satus, String site, String line, String machine, String operation) {
        this.date = date;
        this.satus = satus;
        this.site = site;
        this.line = line;
        this.machine = machine;
        this.operation = operation;
    }

    public String getDate() {
        return date;
    }

    public String getSatus() {
        return satus;
    }

    public String getSite() {
        return site;
    }

    public String getLine() {
        return line;
    }

    public String getMachine() {
        return machine;
    }

    public String getOperation() {
        return operation;
    }
}
