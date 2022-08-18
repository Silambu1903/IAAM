package com.rax.iaam.Model;

public class NewLineModel {
    int lineId;
    String lineName;

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public NewLineModel(int lineId, String lineName) {
        this.lineId = lineId;
        this.lineName = lineName;
    }
}
