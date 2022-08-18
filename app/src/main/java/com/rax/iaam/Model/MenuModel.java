package com.rax.iaam.Model;

public class MenuModel {
    private int menuID;
    private String menuName;
    private boolean isActive;

    public MenuModel(int menuID, String menuName, boolean isActive) {
        this.menuID = menuID;
        this.menuName = menuName;
        this.isActive = isActive;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
