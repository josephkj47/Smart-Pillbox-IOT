package com.rset.pills4u;
public class BoxModel{
    String box_pillname;
    int box_pillcount;
    float box_pillweight;

    public BoxModel(String box_pillname, int box_pillcount, float box_pillweight) {
        this.box_pillname = box_pillname;
        this.box_pillcount = box_pillcount;
        this.box_pillweight = box_pillweight;
    }
    public void consume(){
        box_pillcount--;
    }
    public String getBox_pillname() {
        return box_pillname;
    }

    public void setBox_pillname(String box_pillname) {
        this.box_pillname = box_pillname;
    }

    public int getBox_pillcount() {
        return box_pillcount;
    }

    public void setBox_pillcount(int box_pillcount) {
        this.box_pillcount = box_pillcount;
    }

    public float getBox_pillweight() {
        return box_pillweight;
    }

    public void setBox_pillweight(float box_pillweight) {
        this.box_pillweight = box_pillweight;
    }
}