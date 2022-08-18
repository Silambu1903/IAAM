package com.rax.iaam.Model.UserAssignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BlockModel implements Parcelable {
    int blockId;
    String blockName;
    boolean isAssigned;
    List<FloorModel> floors;

    public BlockModel(int blockId, String blockName, boolean isAssigned) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.isAssigned = isAssigned;
    }

    public BlockModel(int blockId, String blockName, boolean isAssigned, List<FloorModel> floors) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.isAssigned = isAssigned;
        this.floors = floors;
    }


    public static final Creator<BlockModel> CREATOR = new Creator<BlockModel>() {
        @Override
        public BlockModel createFromParcel(Parcel in) {
            return new BlockModel(in);
        }

        @Override
        public BlockModel[] newArray(int size) {
            return new BlockModel[size];
        }
    };

    protected BlockModel(Parcel in) {
        blockId = in.readInt();
        blockName = in.readString();
        isAssigned = in.readByte() != 0;
        floors = in.createTypedArrayList(FloorModel.CREATOR);
    }

    public int getBlockId() {
        return blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public List<FloorModel> getFloors() {
        return floors;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(blockId);
        dest.writeString(blockName);
        dest.writeByte((byte) (isAssigned ? 1 : 0));
        dest.writeTypedList(floors);
    }
}
