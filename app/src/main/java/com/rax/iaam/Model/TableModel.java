package com.rax.iaam.Model;

import androidx.annotation.NonNull;

public class TableModel {
    private int id;
    private int SAPID;
    private String deskNo;

    private boolean commissionedStatus;
    private String siteName;
    private String blockName;
    private String floorName;
    private String lineName;

    private boolean selected;

    public TableModel(int id,int SAPID, String deskNo, boolean commissionedStatus, String siteName, String blockName, String floorName, String lineName,boolean selected) {
        this.SAPID = SAPID;
        this.deskNo = deskNo;
        this.commissionedStatus = commissionedStatus;
        this.siteName = siteName;
        this.blockName = blockName;
        this.floorName = floorName;
        this.lineName = lineName;
        this.selected = selected;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public TableModel(int SAPID, String deskNo) {
        this.SAPID = SAPID;
        this.deskNo = deskNo;
    }

    public int getSAPID() {
        return SAPID;
    }

    public String getDeskNo() {
        return deskNo;
    }

    @NonNull
    @Override
    public String toString() {
        return SAPID + "";
    }
}
