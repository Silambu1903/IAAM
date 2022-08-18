package com.rax.iaam.Model;

public class NewFloorModel {
    int FloorId;
    String FloorName;

    public int getFloorId() {
        return FloorId;
    }

    public void setFloorId(int floorId) {
        FloorId = floorId;
    }

    public String getFloorName() {
        return FloorName;
    }

    public void setFloorName(String floorName) {
        FloorName = floorName;
    }

    public NewFloorModel(int floorId, String floorName) {
        FloorId = floorId;
        FloorName = floorName;
    }
}
