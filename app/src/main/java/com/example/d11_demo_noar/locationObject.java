package com.example.d11_demo_noar;

public class locationObject {
    private String name;
    private String place_id;
    private String rating;
    private String latitude;
    private String longitude;



    public locationObject(String name, String place_id, String rating, String latitude, String longitude){
        this.name = name;
        this.place_id = place_id;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }
    public String getRating() {
        return rating;
    }
    public String getPlace_id() { return place_id;}
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }




}
