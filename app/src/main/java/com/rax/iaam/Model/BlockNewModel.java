package com.rax.iaam.Model;

public class BlockNewModel {
    int blockID;
    String blockName;

    public int getBlockID() {
        return blockID;
    }

    public BlockNewModel(int blockID, String blockName) {
        this.blockID = blockID;
        this.blockName = blockName;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
}
