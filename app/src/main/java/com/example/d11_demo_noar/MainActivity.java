package com.example.d11_demo_noar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.Intent;

import java.security.Permission;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // DIRECTION (compass) values
    TextView degreeVal, directionVal;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private String[] directionLabels = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] mRotationMatrix = new float[9];

    private double user_angle;

    // LOCATION coordinate values
    private int permissionCode = 1;
    private LocationManager locationManager;

    private Button refreshBut;


    TextView currLatVal, currLongVal;

    private double user_lat, user_long;

    // PLACES

    private ArrayList<locationObject> locationList = new ArrayList<locationObject>();

    private RelativeLayout home;
    private RecyclerView recycler;
    private MyAdapter adapter;

    // DETAILS

    private RelativeLayout poppy;
    private TextView top_review_textview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        degreeVal = (TextView) findViewById(R.id.degreeVal);
        directionVal = (TextView) findViewById(R.id.directionVal);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        home = findViewById(R.id.idhome);
        recycler = findViewById(R.id.display_list);

        currLatVal = (TextView) findViewById(R.id.currLatVal);
        currLongVal = (TextView) findViewById(R.id.currLongVal);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        refreshBut = findViewById(R.id.refresh);
        refreshBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });


        double[] location_coordinates = getUserCoords(locationManager);
        user_lat = location_coordinates[0];
        user_long = location_coordinates[1];
        String radius = "300";

        getData(user_lat, user_long, radius);


        adapter = new MyAdapter(this, locationList, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }
        });
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));


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
        // OBTAIN DIRECTIONAL VALUE (compass)
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
            if(dirL >= 0) {
                directionVal.setText(directionLabels[dirL]);
            }

            user_angle = (double) azimuth;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    private double [] getUserCoords(LocationManager locationManager){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},permissionCode);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double [] coordinates = new double[2];
        coordinates[0] = location.getLatitude();
        coordinates[1] = location.getLongitude();

        // textView
        currLatVal.setText("Lat: " + Double.toString(coordinates[0]));
        currLongVal.setText("Lon: " + Double.toString(coordinates[1]));

        return coordinates;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==permissionCode){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Allow permissions to view location", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getData(double latitude, double longitude, String radius) {
        home.setVisibility(View.VISIBLE);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + radius + "&key=AIzaSyBAyn3y0awsDMe0x7oUZ9i_iiu61lDWCR0";
        String placeid = "ChIJgUWV1jbKRIYR-RaDGSfFUSY";
        String fields = "name%2Cplace_id%2Cgeometry%2Crating%2Copening_hours%2Creviews";
        String url2 = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeid + "&fields=" + fields + "&reviews_sort=most_relevant" + "&key=AIzaSyBAyn3y0awsDMe0x7oUZ9i_iiu61lDWCR0";
        new PlaceTask().execute(url);
    }


    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();

        String line = "";

        while((line = reader.readLine()) != null){
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>>{

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object, user_lat, user_long, user_angle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMapList) {
            for(int i = 0; i <hashMapList.size(); i++){
//                HashMap<String,String> location_item = hashMapList.get(i);
                String name = hashMapList.get(i).get("name");
//                String place_id = hashMapList.get(i).get("place_id");
                String rating = hashMapList.get(i).get("rating");
                String latitude = hashMapList.get(i).get("latitude");
                String longitude = hashMapList.get(i).get("longitude");
                String diff_angle = hashMapList.get(i).get("diff_angle");
                String isOpen = hashMapList.get(i).get("isOpen");
                if(name != null) {
                    locationList.add(new locationObject(name,
                            "ID: " + hashMapList.get(i).get("place_id"),
                            "rating: " + rating,
                            "lat: " + latitude,
                            "lng: " + longitude,
                            "ang: " + diff_angle,
                            isOpen
                            ));
                }

            }
            System.out.println("NUM OF LOADED LOCATIONS: " + locationList.size());
            create(locationList);
        }
    }
    public void create(ArrayList<locationObject> list){
        adapter = new MyAdapter(this, locationList, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {

            }
        });
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

}
