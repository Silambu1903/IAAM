package com.rax.iaam.Model;

public class Child {
    int id;
    String name;
    private boolean childCheck;

    public Child(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChildCheck() {
        return childCheck;
    }

    public void setChildCheck(boolean childCheck) {
        this.childCheck = childCheck;
    }
}
