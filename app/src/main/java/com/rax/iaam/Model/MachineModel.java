package com.rax.iaam.Model;

import androidx.annotation.NonNull;

public class MachineModel {
    private int id;
    private int SAPID;
    private String model;
    private String make;
    private String YOP;
    private int maintenanceHr;
    private int needleHr;
    private int machineCurrent;

    private boolean commissionedStatus;
    private String siteName;
    private String blockName;
    private String floorName;
    private String lineName;
    private String detail;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MachineModel(int id, String model, String make, int maintenanceHr, int needleHr, int machineCurrent,boolean selected) {
        this.id = id;
        this.model = model;
        this.make = make;
        this.maintenanceHr = maintenanceHr;
        this.needleHr = needleHr;
        this.machineCurrent = machineCurrent;
        this.selected = selected;
    }

    public MachineModel(int SAPID, String model, String make, String YOP, String detail) {
        this.SAPID = SAPID;
        this.model = model;
        this.make = make;
        this.YOP = YOP;
        this.detail = detail;
    }

    public MachineModel(int id, int SAPID, String model, String make, String YOP, int maintenanceHr, int needleHr,
                        int machineCurrent, boolean commissionedStatus, String siteName, String blockName, String floorName, String lineName, String detail,boolean selected) {
        this.id = id;
        this.SAPID = SAPID;
        this.model = model;
        this.make = make;
        this.YOP = YOP;
        this.maintenanceHr = maintenanceHr;
        this.needleHr = needleHr;
        this.machineCurrent = machineCurrent;
        this.commissionedStatus = commissionedStatus;
        this.siteName = siteName;
        this.blockName = blockName;
        this.floorName = floorName;
        this.lineName = lineName;
        this.detail = detail;
        this.selected = selected;
    }

    public MachineModel(int SAPID, String model, String make, String YOP, int maintenanceHr, int needleHr, int machineCurrent) {
        this.SAPID = SAPID;
        this.model = model;
        this.make = make;
        this.YOP = YOP;
        this.maintenanceHr = maintenanceHr;
        this.needleHr = needleHr;
        this.machineCurrent = machineCurrent;
    }

    @NonNull
    @Override
    public String toString() {
        return SAPID + "";
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

    public String getYOP() {
        return YOP;
    }

    public int getMaintenanceHr() {
        return maintenanceHr;
    }

    public int getNeedleHr() {
        return needleHr;
    }

    public int getMachineCurrent() {
        return machineCurrent;
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

    public String getDetail() {
        return detail;
    }

    public int getId() {
        return id;
    }
}

