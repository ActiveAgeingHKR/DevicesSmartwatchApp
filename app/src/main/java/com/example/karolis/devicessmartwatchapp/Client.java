package com.example.karolis.devicessmartwatchapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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

    public boolean establishContact() {
        boolean contactEstablished = false;
        try {
            clientSocket = new Socket();
            SocketAddress sa = new InetSocketAddress(KAROLIS_IP, PORT);
            clientSocket.connect(sa, 1000);
            contactEstablished = true;
            outputToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Connection rejected");
        }
         return contactEstablished;
    }


    public void send(String text) {
        try {
            outputToServer.write(text + "\r\n");
            outputToServer.flush();
            Log.i(TAG, "Message sent." + text);

        } catch (Exception e) {
            Log.e("TCP", "S: fail message sending", e);
            e.printStackTrace();
        }

    }

    public String receive() {
        String message = null;
        try {
            BufferedReader bufferedReaderInput = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            message = bufferedReaderInput.readLine();
            Log.i(TAG, "Message received: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void closeConnection() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            Log.e("TCP", "S: Error in closing", e);
            e.printStackTrace();
        }
    }
}
