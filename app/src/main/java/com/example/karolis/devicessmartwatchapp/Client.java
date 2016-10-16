package com.example.karolis.devicessmartwatchapp;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.*;
import java.net.*;

/**
 * Created by Karolis on 2016-09-21.
 */

public class Client {
    private static final String TAG = "Client";
    public static final int PORT = 12345;
    private final String KAROLIS_IP = "192.168.1.92";

    private Socket clientSocket = null;
    private BufferedWriter outputToServer = null;

    public void establishContact() {
        Log.i(TAG, "Connection initiated");
        try {
            clientSocket = new Socket(InetAddress.getByName(KAROLIS_IP), PORT);
            outputToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            Log.i(TAG, "Connection established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void send(String text) {
        try {
            outputToServer.write(text + "\r\n");
            outputToServer.flush();
            Log.i(TAG, "Message sent.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            clientSocket.close();
            outputToServer.close();
            Log.i(TAG, "Connection closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
