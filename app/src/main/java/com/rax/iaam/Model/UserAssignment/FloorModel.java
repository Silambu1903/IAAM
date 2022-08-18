package com.rax.iaam.Model.UserAssignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FloorModel implements Parcelable {
    int floorId;
    String floorName;
    boolean isAssigned;
    List<LineModel> lines;

    public FloorModel(int floorId, String floorName, boolean isAssigned) {
        this.floorId = floorId;
        this.floorName = floorName;
        this.isAssigned = isAssigned;
    }

    public FloorModel(int floorId, String floorName, boolean isAssigned, List<LineModel> lines) {
        this.floorId = floorId;
        this.floorName = floorName;
        this.isAssigned = isAssigned;
        this.lines = lines;
    }

    public static final Creator<FloorModel> CREATOR = new Creator<FloorModel>() {
        @Override
        public FloorModel createFromParcel(Parcel in) {
            return new FloorModel(in);
        }

        @Override
        public FloorModel[] newArray(int size) {
            return new FloorModel[size];
        }
    };

    protected FloorModel(Parcel in) {
        floorId = in.readInt();
        floorName = in.readString();
        isAssigned = in.readByte() != 0;
        lines = in.createTypedArrayList(LineModel.CREATOR);
    }

    public int getFloorId() {
        return floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public List<LineModel> getLines() {
        return lines;
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
        dest.writeInt(floorId);
        dest.writeString(floorName);
        dest.writeByte((byte) (isAssigned ? 1 : 0));
        dest.writeTypedList(lines);
    }
}
