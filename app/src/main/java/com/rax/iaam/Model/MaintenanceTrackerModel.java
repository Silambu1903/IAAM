package com.rax.iaam.Model;

public class MaintenanceTrackerModel {

    private String requestId;
    private String location;
    private String desk;
    private String requestTime;
    private String status;
    private String assaigned;


    public MaintenanceTrackerModel(String requestId, String location, String desk, String requestTime, String status, String assaigned) {
        this.requestId = requestId;
        this.location = location;
        this.desk = desk;
        this.requestTime = requestTime;
        this.status = status;
        this.assaigned = assaigned;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssaigned() {
        return assaigned;
    }

    public void setAssaigned(String assaigned) {
        this.assaigned = assaigned;
    }
}
