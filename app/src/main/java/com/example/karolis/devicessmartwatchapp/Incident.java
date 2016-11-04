package com.example.karolis.devicessmartwatchapp;

import java.sql.Timestamp;

/**
 * Created by wasim on 11/3/2016.
 */

public class Incident {

    public enum Severity{
        NORMAL, MILD, SERVERE
    }

    private int customerID;
    private Timestamp timestamp;
    private String notes;
    private Severity severity;

    public Incident(int customerID, Timestamp timestamp, Severity severity){
        this.customerID = customerID;
        this.timestamp = timestamp;
        this.severity = severity;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }



    public int getCustomerID(){

        return customerID;
    }

    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }

    public Timestamp getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {

        this.timestamp = timestamp;
    }

    public String getNotes() {

        return notes;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }
}
