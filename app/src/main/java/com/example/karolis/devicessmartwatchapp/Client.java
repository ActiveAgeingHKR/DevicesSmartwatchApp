package com.example.karolis.devicessmartwatchapp;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.System.out;

/**
 * Created by Karolis on 2016-09-21.
 */

public class Client {
    private static final String TAG = "Client";
    public static final int PORT = 12345;
    private final String KAROLIS_IP = "192.168.1.92";

    private Socket clientSocket = null;
    private BufferedWriter outputToServer = null;
    private PrintWriter pw =null;

    public void establishContact() {
        Log.i(TAG, "Connection initiated");
        try {
            clientSocket = new Socket(InetAddress.getByName(KAROLIS_IP), PORT);
            outputToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
           //pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            //pw = new PrintWriter(clientSocket.getOutputStream());

            Log.i(TAG, "Connection established");
           // Log.e("TCP Client", "C: Sent.");
            //Log.e("TCP Client", "C: Done.");
        } catch (IOException e) {
            out.println(e);
            Log.e("TCP", "S: Error in connection", e);
            e.printStackTrace();
            Log.i(TAG, "Connection rejected");
        }
    }


    public void send(String text) {
        try {
            outputToServer.write(text + "\r\n");
            //pw.write(text + "\r\n");
            outputToServer.flush();
            Log.i(TAG, "Message sent.");

        } catch (Exception e) {
            Log.e("TCP", "S: fail message sending", e);
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            clientSocket.close();
            outputToServer.close();
            Log.i(TAG, "Connection closed.");
        } catch (Exception e) {
            Log.e("TCP", "S: Error in closing", e);
            e.printStackTrace();
        }
    }
}
