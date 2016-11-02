package com.example.karolis.devicessmartwatchapp;

import android.content.Context;
import android.hardware.*;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);
    public final String TAG = "MainActivity";

    //Defines how sensitive accelerometer is for detecting when the person fell. The higher the number the less sensitive it is.
    private final int ACCELEROMETER_SENSITIVITY = 15;
    private final int CUSTOMER_ID = 151515;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer, mheartRate;
    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mheartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //TODO add values[1] and values[2] to accelerometer readings
            if (sensorEvent.values[0] > ACCELEROMETER_SENSITIVITY) {
                Log.i("Accelerometer data: ", String.valueOf(sensorEvent.values[0]));
                Customer customer = new Customer(CUSTOMER_ID, "fell");
                customer.setAccelerometerData(sensorEvent.values[0]);
                new AsyncServer().execute(createJsonString(customer));
            }
        }
        if (sensor.getType() == Sensor.TYPE_HEART_RATE){
            Log.i(TAG, "HeartRate: " + sensorEvent.values[0]);
//            new AsyncServer().execute("HeartRate: " + String.valueOf(sensorEvent.values[0]));
            Customer customer = new Customer(CUSTOMER_ID, "pulse");
            customer.setHeartRate(sensorEvent.values[0]);
            new AsyncServer().execute(createJsonString(customer));
        }
    }
    private String createJsonString(Customer customer){
        return new Gson().toJson(customer);
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
}

