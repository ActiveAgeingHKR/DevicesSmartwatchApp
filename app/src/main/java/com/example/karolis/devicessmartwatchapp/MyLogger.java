package com.example.karolis.devicessmartwatchapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by Karolis on 2016-12-16.
 */

public class MyLogger {
    public static final String TAG = "MyLogger";

    /**
     * public static synchronized void archive(String jsonIncident) {
     * Logger logger = Logger.getLogger(Log.class.getName());
     * FileHandler fh;
     * try {
     * fh = new FileHandler("archive.log", true);
     * logger.addHandler(fh);
     * SimpleFormatter formatter = new SimpleFormatter();
     * fh.setFormatter(formatter);
     * logger.info(jsonIncident);
     * fh.close();
     * } catch (SecurityException e) {
     * e.printStackTrace();
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     **/
    public static synchronized void offline(String jsonIncident, Context context) {
        try {
            File file = new File(context.getExternalFilesDir(
                    Environment.DIRECTORY_DOCUMENTS), "offline.log");

            FileWriter pw = new FileWriter(file, true);
            pw.append(jsonIncident + System.getProperty("line.separator"));
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendLoggedIncidents(Context context) {
        try {
            File file = new File(context.getExternalFilesDir(
                    Environment.DIRECTORY_DOCUMENTS), "offline.log");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                System.out.println("Sending logged incidents");
                String json;
                while ((json = br.readLine()) != null) {
                    new AsyncServer().execute(json);
                }
                br.close();
                file.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


