package com.rax.iaam.Model;

public class DeviceModel {
    private int DeviceID;
    private String MacAdress;
    private int Assignment;
    private String ClientName;

    private String serialNo;
    private boolean commissionedStatus;
    private String siteName;
    private String blockName;
    private String floorName;
    private String lineName;



    public DeviceModel(int deviceID, String macAdress, String serialNo, boolean commissionedStatus, String siteName, String blockName, String floorName,
                       String lineName) {
        DeviceID = deviceID;
        MacAdress = macAdress;
        this.serialNo = serialNo;
        this.commissionedStatus = commissionedStatus;
        this.siteName = siteName;
        this.blockName = blockName;
        this.floorName = floorName;
        this.lineName = lineName;
    }

    public DeviceModel(int deviceID, String macAdress,String serialNo, int assignment, String clientName) {
        DeviceID = deviceID;
        MacAdress = macAdress;
        Assignment = assignment;
        ClientName = clientName;
        this.serialNo = serialNo;
    }


    public int getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(int deviceID) {
        DeviceID = deviceID;
    }

    public String getMacAdress() {
        return MacAdress;
    }

    public void setMacAdress(String macAdress) {
        MacAdress = macAdress;
    }

    public int getAssignment() {
        return Assignment;
    }

    public void setAssignment(int assignment) {
        Assignment = assignment;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public boolean isCommissioned() {
        return commissionedStatus;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getFloorName() {
        return floorName;
    }

    public String getLineName() {
        return lineName;
    }
}
