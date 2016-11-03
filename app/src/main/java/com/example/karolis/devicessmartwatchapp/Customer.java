package com.example.karolis.devicessmartwatchapp;

/**
 * Created by Karolis on 2016-11-02.
 */

public class Customer extends Person {

    private int customerID;
    private String warning;
    private float heartRate;
    private float accelerometerData;

    public Customer(int customerID, String warning){
       this.customerID = customerID;
        this.setWarning(warning);
    }


    public int getCustomerID(){

        return customerID;
    }

    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }

    public String getWarning() {

        return warning;
    }

    public void setWarning(String warning) {

        this.warning = warning;
    }

    public float getHeartRate()
    {
        return heartRate;
    }

    public void setHeartRate(float heartRate)
    {
        this.heartRate = heartRate;
    }

    public float getAccelerometerData()
    {
        return accelerometerData;
    }

    public void setAccelerometerData(float accelerometerData) {

        this.accelerometerData = accelerometerData;
    }
}
