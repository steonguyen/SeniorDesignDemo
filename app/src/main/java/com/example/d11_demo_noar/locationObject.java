package com.example.d11_demo_noar;

public class locationObject {
    private String name;
    private String place_id;
    private String rating;
    private String latitude;
    private String longitude;

    private String diff_angle;

    private String isOpen;

    public locationObject(String name, String place_id, String rating, String latitude, String longitude, String diff_angle, String isOpen){
        this.name = name;
        this.place_id = place_id;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.diff_angle = diff_angle;
        this.isOpen = isOpen;
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
    public String getDiff_angle() {
        return diff_angle;
    }
    public String getIsOpen() {
        return isOpen;
    }




}
