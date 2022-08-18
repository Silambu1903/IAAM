package com.rax.iaam.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShiftModel implements Parcelable {
    private int ID;
    private int clientID;
    private String shiftName;
    private String login;
    private String logout;
    private int createdBy;
    private String createdDate;
    private double minutes;

    public ShiftModel(int ID, int clientID, String shiftName, String login, String logout, double minutes) {
        this.ID = ID;
        this.clientID = clientID;
        this.shiftName = shiftName;
        this.login = login;
        this.logout = logout;
        this.minutes = minutes;
    }

    public ShiftModel(int ID, int clientID, String shiftName, String login, String logout, int createdBy, String createdDate, double minutes) {
        this.ID = ID;
        this.clientID = clientID;
        this.shiftName = shiftName;
        this.login = login;
        this.logout = logout;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.minutes = minutes;
    }

    protected ShiftModel(Parcel in) {
        ID = in.readInt();
        clientID = in.readInt();
        shiftName = in.readString();
        login = in.readString();
        logout = in.readString();
        createdBy = in.readInt();
        createdDate = in.readString();
        minutes = in.readDouble();
    }

    public static final Creator<ShiftModel> CREATOR = new Creator<ShiftModel>() {
        @Override
        public ShiftModel createFromParcel(Parcel in) {
            return new ShiftModel(in);
        }

        @Override
        public ShiftModel[] newArray(int size) {
            return new ShiftModel[size];
        }
    };

    public int getID() {
        return ID;
    }

    public int getClientID() {
        return clientID;
    }

    public String getShiftName() {
        return shiftName;
    }

    public String getLogin() {
        return login;
    }

    public String getLogout() {
        return logout;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public double getMinutes() {
        return minutes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(clientID);
        dest.writeString(shiftName);
        dest.writeString(login);
        dest.writeString(logout);
        dest.writeInt(createdBy);
        dest.writeString(createdDate);
        dest.writeDouble(minutes);
    }
}
