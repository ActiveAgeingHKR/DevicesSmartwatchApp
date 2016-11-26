package com.example.karolis.devicessmartwatchapp;

import java.util.concurrent.ExecutionException;

/**
 * Created by Karolis on 2016-11-26.
 */

public class HearbeatToProxy implements Runnable {

    public static Boolean proxyIsAlive;

    @Override
    public void run() {
        try {
            proxyIsAlive = new AsyncHeartbeat().execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
