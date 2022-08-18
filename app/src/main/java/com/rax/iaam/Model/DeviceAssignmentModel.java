package com.rax.iaam.Model;

public class DeviceAssignmentModel {
    private long deviceID;
    private String macNo;
    private String accNo;
    private int assignment;
    private boolean checked;


    public DeviceAssignmentModel(long deviceID, String macNo, String accNo, int assignment, boolean checked) {
        this.deviceID = deviceID;
        this.macNo = macNo;
        this.accNo = accNo;
        this.assignment = assignment;
        this.checked = checked;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public String getMacNo() {
        return macNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public int getAssignment() {
        return assignment;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
