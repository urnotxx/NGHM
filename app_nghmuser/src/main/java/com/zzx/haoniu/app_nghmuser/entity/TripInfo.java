package com.zzx.haoniu.app_nghmuser.entity;

/**
 * Created by Administrator on 2017/2/4.
 */

public class TripInfo {

    /**
     * create_time : 2017-01-17 19:01:56
     * destination : 星光天地(宁国南路)
     * end_latitude : 31.83426
     * end_longitude : 117.292392
     * id : 3098
     * pd_flag : false
     * reservation_address : 市场星报社
     * start_latitude : 31.830227
     * start_longitude : 117.255063
     * type : 44
     * yg_amount : 9.0
     */

    private String create_time;
    private String destination;
    private double end_latitude;
    private double end_longitude;
    private int id;
    private boolean pd_flag;
    private String reservation_address;
    private double start_latitude;
    private double start_longitude;
    private int type;
    private double yg_amount;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(double end_latitude) {
        this.end_latitude = end_latitude;
    }

    public double getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(double end_longitude) {
        this.end_longitude = end_longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPd_flag() {
        return pd_flag;
    }

    public void setPd_flag(boolean pd_flag) {
        this.pd_flag = pd_flag;
    }

    public String getReservation_address() {
        return reservation_address;
    }

    public void setReservation_address(String reservation_address) {
        this.reservation_address = reservation_address;
    }

    public double getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public double getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(double start_longitude) {
        this.start_longitude = start_longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getYg_amount() {
        return yg_amount;
    }

    public void setYg_amount(double yg_amount) {
        this.yg_amount = yg_amount;
    }
}
