package com.logix.symphony.Model;


import java.util.ArrayList;

public class VerticalRecyclerModel {

    String header;
    ArrayList<HorizontalRecylerModel> list;

    public String getHeader() {
        return header;
    }

    public ArrayList<HorizontalRecylerModel> getList() {
        return list;
    }

    public VerticalRecyclerModel(String header, ArrayList<HorizontalRecylerModel> list) {
        this.header = header;
        this.list = list;
    }
}
