package com.rax.iaam.Model.UserAssignment;

import android.os.Parcel;
import android.os.Parcelable;

public class LineModel implements Parcelable {
    int lineId;
    String lineName;
    boolean isAssigned;
    String users;

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    protected LineModel(Parcel in) {
        lineId = in.readInt();
        lineName = in.readString();
        isAssigned = in.readByte() != 0;
        users = in.readString();
    }

    public static final Creator<LineModel> CREATOR = new Creator<LineModel>() {
        @Override
        public LineModel createFromParcel(Parcel in) {
            return new LineModel(in);
        }

        @Override
        public LineModel[] newArray(int size) {
            return new LineModel[size];
        }
    };

    public int getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public String getUsers() {
        return users;
    }

    public LineModel(int lineId, String lineName, boolean isAssigned, String users) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.isAssigned = isAssigned;
        this.users = users;
    }

    public LineModel(int lineId, String lineName, boolean isAssigned) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.isAssigned = isAssigned;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lineId);
        dest.writeString(lineName);
        dest.writeByte((byte) (isAssigned ? 1 : 0));
        dest.writeString(users);
    }
}
