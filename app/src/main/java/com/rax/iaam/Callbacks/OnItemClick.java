package com.rax.iaam.Callbacks;

import com.rax.iaam.Model.Child;

import java.util.ArrayList;
import java.util.List;

public interface OnItemClick {
    public void OnItemClickedSiteid(ArrayList<Integer> siteid);
    public void OnItemClickedBlockid(ArrayList<Integer> blockid);
    public void OnItemClickedFloorid(ArrayList<Integer> floorid);
    public void OnItemClickedLineid(ArrayList<Integer> lineid);
    public void OnItemClickedShiftid(ArrayList<Integer> shiftid,ArrayList<Integer> shift_parent_id);

    public void OnItemClickSite(int siteid);
    //for selectall floor
    public void OnItemClickedcheckallblock(ArrayList<Integer> blockid);
    public void OnItemClickedcheckallfloor(ArrayList<Integer> floorid);
    public void OnItemClickedcheckallLine(ArrayList<Integer> lineid);
    public void OnItemClickedcheckallshift(ArrayList<Integer> shiftid);

}
