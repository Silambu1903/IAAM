package com.rax.iaam.Model;

import java.util.ArrayList;

public class SectionModelShift {


        private String sectionLabel;
        private ArrayList<ShiftChild> itemArrayList;
        int id;
        public SectionModelShift(int id,String sectionLabel, ArrayList<ShiftChild> itemArrayList ) {
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

        public ArrayList<ShiftChild> getItemArrayList() {
            return itemArrayList;
        }

        public void setItemArrayList(ArrayList<ShiftChild> itemArrayList) {
            this.itemArrayList = itemArrayList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }







}
