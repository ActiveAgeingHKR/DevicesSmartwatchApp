package com.example.karolis.devicessmartwatchapp;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Karolis on 2016-09-21.
 */

public class Client {
    private final String TAG = "Client";
    private final String KAROLIS_IP = "192.168.1.92";
    Socket clientSocket = null;
    PrintWriter out = null;


    public void send(String text) {
        try {
            BufferedWriter outputToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outputToServer.write(text + "\r\n");
            outputToServer.flush();
            Log.i(TAG, "message sent.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void establishContact() {
        Log.i(TAG, "Connection initiated");
        try {
            clientSocket = new Socket(InetAddress.getByName(KAROLIS_IP), 12345);
            Log.i(TAG, "Connection established");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not listen on port: 12345");
            System.exit(-1);
        }
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ioe) {
            System.out.println("Failed in creating streams");
            System.exit(-1);
        }
    }

    public void closeConnection() {
        try {
            clientSocket.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Could not close");
            System.exit(-1);
        }
    }
}
