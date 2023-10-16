package com.example.d11_demo_noar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private double lat;
    private double lng;
    private double fangle;

    private HashMap<String, String> parseJsonObject(JSONObject object){
        HashMap<String, String> dataList = new HashMap<>();

        try {
            String name = object.getString("name");
            String place_id = object.getString("place_id");
            System.out.println("Place ID: " + place_id);
            String rating = object.getString("rating");
            JSONObject geometry = object.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            String latitude = location.getString("lat");
            String longitude = location.getString("lng");

            double x1 = lat; // replace with your origin x value
            double y1 = lng; // replace with your origin y value
            double x2 = Double.parseDouble(latitude); // replace with your new x value
            double y2 = Double.parseDouble(longitude); // replace with your new y value
            double facingAngle = fangle; // replace with your angle value in degrees

            double positiveAngle = facingAngle % 360;
            if (positiveAngle < 0) {
                positiveAngle += 360;
            }

            double dx = x2 - x1; // -0.0008685
            double dy = y2 - y1; // 0.0001338
            double theta = Math.atan2(dy, dx);
            double angle = Math.toDegrees(theta);
            double angleDiff = Math.abs(angle - positiveAngle);

            String diff_angle = Double.toString(angleDiff);

            JSONObject opening_hours = object.getJSONObject("opening_hours");
            boolean open_now = opening_hours.getBoolean("open_now");
            String isOpen = "Open!";
            if(!open_now){
                isOpen = "Closed!";
            }



            dataList.put("name", name);
            dataList.put("place_id", place_id);
            dataList.put("rating", rating);
            dataList.put("latitude", latitude);
            dataList.put("longitude", longitude);
            dataList.put("diff_angle", diff_angle);
            dataList.put("isOpen", isOpen);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) throws JSONException {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for(int i = 0; i <jsonArray.length(); i++){
            HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
            dataList.add(data);
        }
        return dataList;
    }


    public List<HashMap<String, String>> parseResult(JSONObject object, double latitude, double longitude, double angle) throws JSONException {
        JSONArray jsonArray = null;

        lat = latitude;
        lng = longitude;
        fangle = angle;
        try {
            jsonArray = object.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseJsonArray(jsonArray);


    }

    /*

    DETAILS


     */

    public HashMap<String, String> parseDetailsResult(JSONObject object) throws JSONException {
        HashMap<String, String> detailMap = new HashMap<>();
        JSONObject detailsResult = null;
        System.out.println("BRUHHH: ");

        try {
            detailsResult = object.getJSONObject("result");

            String name = detailsResult.getString("name");
            System.out.println("BRUHHH: " + name);
            String formatted_address = "address";//detailsResult.getString("formatted_address");
            String formatted_phone_number = "phone number";//detailsResult.getString("formatted_phone_number");
            String website = "website";//detailsResult.getString("website");
            String googleurl = "googleurl";//detailsResult.getString("url");
            String price_level = "price_level";//Integer.toString(detailsResult.getInt("price_level"));
            String rating_det = detailsResult.getString("rating");

            detailMap.put("name", name);
            detailMap.put("formatted_address", formatted_address);
            detailMap.put("formatted_phone_number", formatted_phone_number);
            detailMap.put("website", website);
            detailMap.put("googleurl", googleurl);
            detailMap.put("price_level", price_level);
            detailMap.put("rating_det", rating_det);


            JSONArray reviews = detailsResult.getJSONArray("reviews");
            String rating1 = Integer.toString(((JSONObject) reviews.get(0)).getInt("rating"));
            String text1 = "[" + rating1 + "] " + ((JSONObject) reviews.get(0)).getString("text");
            detailMap.put("review1", text1);
            if(reviews.length() > 1) {
                String rating2 = Integer.toString(((JSONObject) reviews.get(1)).getInt("rating"));
                String text2 = "[" + rating1 + "] " + ((JSONObject) reviews.get(1)).getString("text");
                detailMap.put("review2", text2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detailMap;

    }



}
