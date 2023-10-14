package com.example.d11_demo_noar;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView degreeVal, directionVal;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private String[] directionLabels = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] mRotationMatrix = new float[9];
    private float[] mOrientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        degreeVal = (TextView) findViewById(R.id.degreeVal);
        directionVal = (TextView) findViewById(R.id.directionVal);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }


    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mGravity, 0, event.values.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mGeomagnetic, 0, event.values.length);
        }
        boolean success = SensorManager.getRotationMatrix(mRotationMatrix, null, mGravity, mGeomagnetic);
        if (success) {
            float azimuth = (float) -Math.toDegrees(Math.atan2(mRotationMatrix[3], mRotationMatrix[0]))-10;
            if (azimuth < 0) {
                azimuth += 360;
            }
            int azimuthInt = (int) azimuth;
//            degreeVal.setText(Float.toString(azimuth) + "\u00B0");
            degreeVal.setText(Integer.toString(azimuthInt) + "\u00B0");

            int dirL = (int) (((azimuth + 22.5)%360)/45);
            if(dirL >= 0){
                directionVal.setText(directionLabels[dirL]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }


}
