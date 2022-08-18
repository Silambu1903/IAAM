package com.rax.iaam.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRoleModel implements Parcelable {
    public static final Creator<UserRoleModel> CREATOR = new Creator<UserRoleModel>() {
        @Override
        public UserRoleModel createFromParcel(Parcel in) {
            return new UserRoleModel(in);
        }

        @Override
        public UserRoleModel[] newArray(int size) {
            return new UserRoleModel[size];
        }
    };
    private int roleID;
    private int clientID;
    private String roleName;
    private String menuList;
    private boolean isActive;

    public UserRoleModel(int roleID, int clientId, String userName, String menuList, boolean isActive) {
        this.roleID = roleID;
        this.clientID = clientId;
        this.roleName = userName;
        this.menuList = menuList;
        this.isActive = isActive;
    }

    protected UserRoleModel(Parcel in) {
        roleID = in.readInt();
        clientID = in.readInt();
        roleName = in.readString();
        menuList = in.readString();
        isActive = in.readByte() != 0;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getClientId() {
        return roleID;
    }

    public void setClientId(int clientID) {
        this.clientID = clientID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getMenuList() {
        return menuList;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roleID);
        dest.writeInt(clientID);
        dest.writeString(roleName);
        dest.writeString(menuList);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    @Override
    public String toString() {
        return roleName;
    }
}
