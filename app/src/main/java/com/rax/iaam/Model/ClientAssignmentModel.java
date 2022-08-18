package com.rax.iaam.Model;

public class ClientAssignmentModel {
    private int clientID;
    private String clientName;
    private String clientShortName;
    private String all;
    private String assigned;
    private String unAssigned;

    public ClientAssignmentModel(int clientID, String clientName, String clientShortName, String all, String assigned, String unAssigned) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientShortName = clientShortName;
        this.all = all;
        this.assigned = assigned;
        this.unAssigned = unAssigned;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientShortName() {
        return clientShortName;
    }

    public void setClientShortName(String clientShortName) {
        this.clientShortName = clientShortName;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getUnAssigned() {
        return unAssigned;
    }

    public void setUnAssigned(String unAssigned) {
        this.unAssigned = unAssigned;
    }
}
