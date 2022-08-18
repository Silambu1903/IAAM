package com.rax.iaam.Model;

public class MachineDetailsModel {
    private String sno;
    private String make;
    private String model;
    private String status;
    private String[] operator;
    private int id;
    private int type;



    public int getId() {
        return id;
    }

    public String getSno() {
        return sno;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getStatus() {
        return status;
    }

    public String[] getOperator() {
        return operator;
    }



    public MachineDetailsModel(String sno, String make, String model, String status, String[] operator, int id) {
        this.sno = sno;
        this.make = make;
        this.model = model;
        this.status = status;
        this.operator = operator;
        this.id = id;

    }
}
