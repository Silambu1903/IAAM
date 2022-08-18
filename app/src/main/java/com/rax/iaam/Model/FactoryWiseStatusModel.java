package com.rax.iaam.Model;

public class FactoryWiseStatusModel {
    private int id;
    private String site;
    private String block;
    private int noOfMachines;
    private String mom;


    public FactoryWiseStatusModel(int id, String site, String block, int noOfMachines, String mom) {
        this.id = id;
        this.site = site;
        this.block = block;
        this.noOfMachines = noOfMachines;
        this.mom = mom;
    }

    public int getId() {
        return id;
    }

    public String getSite() {
        return site;
    }

    public String getBlock() {
        return block;
    }

    public int getNoOfMachines() {
        return noOfMachines;
    }

    public String getMom() {
        return mom;
    }
}
