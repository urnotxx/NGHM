package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/1/10.
 */

public class CarInfo {

    /**
     * distance : 7.880000000142326E-7
     * id : 71
     * latitude : 31.830242
     * longitude : 117.25503
     * nick_name : 马国雷
     * orientation : 0.0
     * phone : 15385131053
     * plate_no : 皖C111111
     * speed : 0.0
     */

    private double distance;
    private int id;
    private double latitude;
    private double longitude;
    private String nick_name;
    private double orientation;
    private String phone;
    private String plate_no;
    private double speed;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
