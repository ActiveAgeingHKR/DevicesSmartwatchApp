package com.example.karolis.devicessmartwatchapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.*;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
/**
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
**/
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);
    public final String TAG = "MainActivity";
    private static final long AMBIENT_INTERVAL_MS = TimeUnit.SECONDS.toMillis(1);
    public static final int HEARTBEAT_PERIOD = 10; //seconds
    public static final String GET_CUSTOMER_ID_URL ="";

    //Defines how sensitive accelerometer is for detecting when the person fell. The higher the number the less sensitive it is.
    private final int ACCELEROMETER_SENSITIVITY = 15;

    private String customerID;
    private String deviceID;

    private float heartRate;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mheartRate;
    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;
    private AlarmManager mAmbientStateAlarmManager;
    private PendingIntent mAmbientStatePendingIntent;
    private Customers customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        //customer = new Customers(1);
        firstTimeDeviceSetup();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mheartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        mAmbientStateAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent ambientStateIntent = new Intent(getApplicationContext(), MainActivity.class);
        mAmbientStatePendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                ambientStateIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new HearbeatToProxy(), 0, HEARTBEAT_PERIOD, TimeUnit.SECONDS);
    }

    public void firstTimeDeviceSetup(){
        //retrieves customer id by sending its device id to proxy server
        createCustomer();
    }

    public void createCustomer(){
        //TODO  instead "1515" we must retrieve Customer id which will be saved after first time device setup
        try {
            customerID = new AsyncCustomerID().execute(getDeviceSerialNr()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        customer = new Customers(Integer.valueOf(customerID));
    }

    public String getDeviceSerialNr(){
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            deviceID = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        Log.i(TAG, "Serial " + deviceID);

        return deviceID;
    }

    private void refreshDisplayAndSetNextUpdate() {
        //refresh display
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);
            mClockView.setText(String.valueOf(heartRate));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }

        long timeMs = System.currentTimeMillis();

        // Schedule a new alarm
        if (isAmbient()) {
            long delayMs = AMBIENT_INTERVAL_MS - (timeMs % AMBIENT_INTERVAL_MS);
            long triggerTimeMs = timeMs + delayMs;

            mAmbientStateAlarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    mAmbientStatePendingIntent);

        } else {
            long delayMs = AMBIENT_INTERVAL_MS - (timeMs % AMBIENT_INTERVAL_MS);
            long triggerTimeMs = timeMs + delayMs;

            mAmbientStateAlarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    mAmbientStatePendingIntent);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        Date date = new Timestamp(new Date().getTime());
        Incidents incidents = null;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //TODO add values[1] and values[2] to accelerometer readings
            if (sensorEvent.values[0] > ACCELEROMETER_SENSITIVITY) {
                Log.i("Accelerometer data: ", String.valueOf(sensorEvent.values[0]));
                incidents = new Incidents(null, date.toString(), "MILD", customer);
                incidents.setInNotes("Customer fell. Accelerometer data: " + sensorEvent.values[0]);
            }
        }

        if (sensor.getType() == Sensor.TYPE_HEART_RATE){
            heartRate = sensorEvent.values[0];
            Log.i(TAG, "HeartRate: " + sensorEvent.values[0]);
            incidents = new Incidents(null, date.toString(), "NORMAL", customer);
            incidents.setInNotes("Heart rate: " + sensorEvent.values[0]);
        }

        if(incidents!=null && HearbeatToProxy.proxyIsAlive) {
            Log.i(TAG, String.valueOf(HearbeatToProxy.proxyIsAlive));
            MyLogger.sendLoggedIncidents(getApplicationContext());
            new AsyncServer().execute(createJsonString(incidents));
        } else if(incidents != null && !HearbeatToProxy.proxyIsAlive){
            MyLogger.offline(createJsonString(incidents), getApplicationContext());
            Log.i(TAG, String.valueOf(HearbeatToProxy.proxyIsAlive));
        }
    }
    private String createJsonString(Incidents incidents) {
        return new Gson().toJson(incidents);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mheartRate, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        refreshDisplayAndSetNextUpdate();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        refreshDisplayAndSetNextUpdate();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        mAmbientStateAlarmManager.cancel(mAmbientStatePendingIntent);
    }

    @Override
    public void onDestroy() {
        mAmbientStateAlarmManager.cancel(mAmbientStatePendingIntent);
        super.onDestroy();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        refreshDisplayAndSetNextUpdate();
    }
}

