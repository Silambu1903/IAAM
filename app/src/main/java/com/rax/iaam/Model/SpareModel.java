package com.rax.iaam.Model;

public class SpareModel {
    private int SAPID;
    private String model;
    private String make;
    private String spare1;
    private String spare2;
    private String spare3;
    private String spare4;

    public SpareModel(int SAPID, String model, String make, String spare1, String spare2, String spare3, String spare4) {
        this.SAPID = SAPID;
        this.model = model;
        this.make = make;
        this.spare1 = spare1;
        this.spare2 = spare2;
        this.spare3 = spare3;
        this.spare4 = spare4;
    }

    public int getSAPID() {
        return SAPID;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getSpare1() {
        return spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public String getSpare4() {
        return spare4;
    }
}
