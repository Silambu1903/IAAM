package com.rax.iaam.Model;

public class EventsModel {
    private String alertName;
    private int alertType;
    private String alertTime;
    private int machineSapId;
    private String site;
    private String block;
    private String floor;
    private String line;

    public EventsModel(String alertName, int alertType, String alertTime, int machineSapId, String site, String block, String floor, String line) {
        this.alertName = alertName;
        this.alertType = alertType;
        this.alertTime = alertTime;
        this.machineSapId = machineSapId;
        this.site = site;
        this.block = block;
        this.floor = floor;
        this.line = line;
    }

    public String getAlertName() {
        return alertName;
    }

    public int getAlertType() {
        return alertType;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public int getMachineSapId() {
        return machineSapId;
    }

    public String getSite() {
        return site;
    }

    public String getBlock() {
        return block;
    }

    public String getFloor() {
        return floor;
    }

    public String getLine() {
        return line;
    }
}
