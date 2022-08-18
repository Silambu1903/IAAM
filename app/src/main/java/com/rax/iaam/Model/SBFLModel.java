package com.rax.iaam.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SBFLModel implements Parcelable {
    private int sitePos;
    private int blockPos;
    private int floorPos;
    private int linePos;

    public SBFLModel() {

    }

    public int getSitePos() {
        return sitePos;
    }

    public int getBlockPos() {
        return blockPos;
    }

    public int getFloorPos() {
        return floorPos;
    }

    public int getLinePos() {
        return linePos;
    }

    public void setSitePos(int sitePos) {
        this.sitePos = sitePos;
    }

    public void setBlockPos(int blockPos) {
        this.blockPos = blockPos;
    }

    public void setFloorPos(int floorPos) {
        this.floorPos = floorPos;
    }

    public void setLinePos(int linePos) {
        this.linePos = linePos;
    }

    private SBFLModel(Parcel in) {
        sitePos = in.readInt();
        blockPos = in.readInt();
        floorPos = in.readInt();
        linePos = in.readInt();
    }

    public static final Creator<SBFLModel> CREATOR = new Creator<SBFLModel>() {
        @Override
        public SBFLModel createFromParcel(Parcel in) {
            return new SBFLModel(in);
        }

        @Override
        public SBFLModel[] newArray(int size) {
            return new SBFLModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sitePos);
        dest.writeInt(blockPos);
        dest.writeInt(floorPos);
        dest.writeInt(linePos);
    }
}
