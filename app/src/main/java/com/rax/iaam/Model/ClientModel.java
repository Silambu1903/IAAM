package com.rax.iaam.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientModel implements Parcelable {
    private int ID;
    private String ClientName;
    private String ClientShortName;
    private String MobileNumber;
    private String Email;
    private String Address;
    private String userName;
    private String password;
    private boolean clientEnabled;
    private boolean adminEnabled;

    public ClientModel(int ID, String clientName, String clientShortName,
                       String mobileNumber, String email, String address, String userName, String password, boolean clientEnabled, boolean adminEnabled) {
        this.ID = ID;
        ClientName = clientName;
        ClientShortName = clientShortName;
        MobileNumber = mobileNumber;
        Email = email;
        Address = address;
        this.userName = userName;
        this.password = password;
        this.clientEnabled = clientEnabled;
        this.adminEnabled = adminEnabled;
    }

    protected ClientModel(Parcel in) {
        ID = in.readInt();
        ClientName = in.readString();
        ClientShortName = in.readString();
        MobileNumber = in.readString();
        Email = in.readString();
        Address = in.readString();
        userName = in.readString();
        password = in.readString();
        clientEnabled = in.readByte() != 0;
        adminEnabled = in.readByte() != 0;
    }

    public static final Creator<ClientModel> CREATOR = new Creator<ClientModel>() {
        @Override
        public ClientModel createFromParcel(Parcel in) {
            return new ClientModel(in);
        }

        @Override
        public ClientModel[] newArray(int size) {
            return new ClientModel[size];
        }
    };

    public int getID() {
        return ID;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getClientShortName() {
        return ClientShortName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isClientEnabled() {
        return clientEnabled;
    }

    public boolean isAdminEnabled() {
        return adminEnabled;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(ClientName);
        dest.writeString(ClientShortName);
        dest.writeString(MobileNumber);
        dest.writeString(Email);
        dest.writeString(Address);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeByte((byte) (clientEnabled ? 1 : 0));
        dest.writeByte((byte) (adminEnabled ? 1 : 0));
    }
}
