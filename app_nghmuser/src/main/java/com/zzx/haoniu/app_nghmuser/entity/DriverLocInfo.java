package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/1/11.
 */

public class DriverLocInfo {
    /**
     * accuracy : 29.0
     * driver : 71
     * id : 33
     * latitude : 31.830242
     * longitude : 117.25503
     * orientation : 0.0
     * recive_time : 2017-01-10 10:23:29
     * speed : 0.0
     * type : 4
     */

    private double accuracy;
    private int driver;
    private int id;
    private double latitude;
    private double longitude;
    private double orientation;
    private String recive_time;
    private double speed;
    private int type;

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getDriver() {
        return driver;
    }

    public void setDriver(int driver) {
        this.driver = driver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public String getRecive_time() {
        return recive_time;
    }

    public void setRecive_time(String recive_time) {
        this.recive_time = recive_time;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
