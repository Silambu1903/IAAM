package com.rax.iaam.Model;

import java.util.ArrayList;

public class FilterModel {
    private int Id;
    private ArrayList<HierarchieModel> list;

    public FilterModel( ArrayList<HierarchieModel> list) {

        this.list = list;
    }

    public int getId() {
        return Id;
    }

    public ArrayList<HierarchieModel> getList() {
        return list;
    }
}
