package com.rax.iaam.Model;

import java.util.ArrayList;
import java.util.List;

public class SectionModel {
    private String sectionLabel;
    private ArrayList<Child> itemArrayList;
    int id;



        public SectionModel(int id,String sectionLabel, ArrayList<Child> itemArrayList ) {
            this.sectionLabel = sectionLabel;
            this.itemArrayList = itemArrayList;
            this.id = id;
        }

        public String getSectionLabel() {
            return sectionLabel;
        }

        public void setSectionLabel(String sectionLabel) {
            this.sectionLabel = sectionLabel;
        }

        public ArrayList<Child> getItemArrayList() {
            return itemArrayList;
        }

        public void setItemArrayList(ArrayList<Child> itemArrayList) {
            this.itemArrayList = itemArrayList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }




}

