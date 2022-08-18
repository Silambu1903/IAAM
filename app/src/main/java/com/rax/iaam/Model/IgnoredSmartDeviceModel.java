package com.rax.iaam.Model;

public class IgnoredSmartDeviceModel {
    private String serialNo;
    private String macID;
    private String reason;

    public IgnoredSmartDeviceModel(String serialNo, String macID, String reason) {
        this.serialNo = serialNo;
        this.macID = macID;
        this.reason = reason;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getMacID() {
        return macID;
    }

    public String getReason() {
        return reason;
    }
}
