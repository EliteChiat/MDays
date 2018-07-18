package com.zucc.ccm31501396.mdays.data;

import java.util.Date;

/**
 * Created by mac on 2018/7/13.
 */

public class Account {
    private String useName;
    private String accountId;
    private int typeID;
    private String typeImage;
    private String typeName;
    private float price;
    private String date;
    private String note;

    public Account(){

    }

    public Account(String accountid, String typeImage, String typeName, float price, String time){
        this.accountId = accountid;
        this.typeImage = typeImage;
        this.typeName = typeName;
        this.price = price;
        this.date = time;
    }


    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
