package com.rax.iaam.Model.UserAssignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SiteModel implements Parcelable {
    int siteId;
    String siteName;
    String siteCode;
    boolean isAssigned;
    List<BlockModel> blocks;

    public SiteModel(int siteId, String siteName, String siteCode, boolean isAssigned) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.siteCode = siteCode;
        this.isAssigned = isAssigned;
    }

    public SiteModel(int siteId, String siteName, String siteCode, boolean isAssigned, List<BlockModel> blocks) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.siteCode = siteCode;
        this.isAssigned = isAssigned;
        this.blocks = blocks;
    }

    public static final Creator<SiteModel> CREATOR = new Creator<SiteModel>() {
        @Override
        public SiteModel createFromParcel(Parcel in) {
            return new SiteModel(in);
        }

        @Override
        public SiteModel[] newArray(int size) {
            return new SiteModel[size];
        }
    };

    protected SiteModel(Parcel in) {
        siteId = in.readInt();
        siteName = in.readString();
        siteCode = in.readString();
        isAssigned = in.readByte() != 0;
    }

    public int getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public List<BlockModel> getBlocks() {
        return blocks;
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
        dest.writeInt(siteId);
        dest.writeString(siteName);
        dest.writeString(siteCode);
        dest.writeByte((byte) (isAssigned ? 1 : 0));
    }
}
