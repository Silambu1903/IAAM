package com.rax.iaam.Model;

public class UserModel {
    int userId;
    int roleId;
    String userName;
    String password;
    boolean isActive;

    public UserModel(int userId, int roleId, String userName, String password, boolean isActive) {
        this.userId = userId;
        this.roleId = roleId;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }

    public UserModel(int userId, String userName, boolean isActive) {
        this.userId = userId;
        this.userName = userName;
        this.isActive = isActive;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getPassword() {
        return password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isActive() {
        return isActive;
    }

}
