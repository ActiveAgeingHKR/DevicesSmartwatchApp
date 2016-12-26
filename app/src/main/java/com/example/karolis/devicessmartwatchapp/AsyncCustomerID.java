package com.example.karolis.devicessmartwatchapp;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncCustomerID extends AsyncTask<String, Void, String> {

    public final static String TAG = "AsyncCustomerID";

    protected String doInBackground(String... params) {

        try {
            String deviceID = params[0];
            Log.i(TAG, deviceID);
            return heartbeat(deviceID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String heartbeat(String deviceID){

        Client client = new Client();
        client.establishContact();
        client.send(deviceID);
        String customerID = client.receive();
        client.closeConnection();
        Log.i(TAG, "Heartbeat");
        return customerID;
    }
}
