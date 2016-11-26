package com.example.karolis.devicessmartwatchapp;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncServer extends AsyncTask<String, Void, String> {

    public final static String TAG = "AsyncServer";

    protected String doInBackground(String... params) {

        try {
            sendData(params[0]);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(String feed) {

    }

    public void sendData(String text){
        Client client = new Client();
        client.establishContact();
        client.send(text);
        client.closeConnection();
    }
}
