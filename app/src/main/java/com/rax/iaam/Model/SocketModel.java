package com.rax.iaam.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SocketModel implements Parcelable {
    private long deviceID;
    private String macID;
    private String serialNo;
    private boolean isSelected;

    public SocketModel(long deviceID, String macID, String serialNo, boolean isSelected) {
        this.deviceID = deviceID;
        this.macID = macID;
        this.serialNo = serialNo;
        this.isSelected = isSelected;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public String getMacID() {
        return macID;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected SocketModel(Parcel in) {
        deviceID = in.readLong();
        macID = in.readString();
        serialNo = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<SocketModel> CREATOR = new Creator<SocketModel>() {
        @Override
        public SocketModel createFromParcel(Parcel in) {
            return new SocketModel(in);
        }

        @Override
        public SocketModel[] newArray(int size) {
            return new SocketModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(deviceID);
        dest.writeString(macID);
        dest.writeString(serialNo);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @NonNull
    @Override
    public String toString() {
        return macID;

    }
}
