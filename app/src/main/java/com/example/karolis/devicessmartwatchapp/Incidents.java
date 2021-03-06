package com.example.karolis.devicessmartwatchapp;

import java.io.Serializable;


public class Incidents implements Serializable {

    private Integer inId;
    private String inTime;
    private String inSeverity;
    private String inNotes;
    private Customers customersCuId;

    public Incidents() {
    }

    public Incidents(Integer inId) {
        this.inId = inId;
    }

    public Incidents(Integer inId, String inTime, String inSeverity, Customers customer) {
        this.inId = inId;
        this.inTime = inTime;
        this.inSeverity = inSeverity;
        this.customersCuId = customer;
    }

    public Integer getInId() {
        return inId;
    }

    public void setInId(Integer inId) {
        this.inId = inId;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getInSeverity() {
        return inSeverity;
    }

    public void setInSeverity(String inSeverity) {
        this.inSeverity = inSeverity;
    }

    public String getInNotes() {
        return inNotes;
    }

    public void setInNotes(String inNotes) {
        this.inNotes = inNotes;
    }
}
