package com.example.karolis.devicessmartwatchapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Karolis on 2016-11-26.
 */

public class AsyncHeartbeat extends AsyncTask<String, Void, Boolean> {

    public final static String TAG = "AsyncHeartbeat";
    public static final String MESSAGE = "hearbeat";

    protected Boolean doInBackground(String... params) {

        try {

            return heartbeat();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean heartbeat(){
        boolean isAlive = false;

        Client client = new Client();
        isAlive = client.establishContact();
        client.closeConnection();
        Log.i(TAG, "Heartbeat");
        return isAlive;
    }
}
