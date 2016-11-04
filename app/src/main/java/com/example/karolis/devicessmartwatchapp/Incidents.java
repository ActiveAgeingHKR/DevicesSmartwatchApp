package com.example.karolis.devicessmartwatchapp;

/**
 * Created by wasim on 11/3/2016.
 */

public class Incidents {

    private int customerID;
    private String severity;
    private int time;
    private String notes;

    public Incidents(int customerID){
        this.customerID = customerID;

    }

    public int getCustomerID(){

        return customerID;
    }

    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }

    public String getSeverity() {

        return severity;
    }

    public void setSeverity(String severity) {

        this.severity = severity;
    }

    public int getTime() {

        return time;
    }

    public void setTime(int time) {

        this.time = time;
    }

    public String getNotes() {

        return notes;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }
}
