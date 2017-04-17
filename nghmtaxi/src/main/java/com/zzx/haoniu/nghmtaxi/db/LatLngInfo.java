package com.zzx.haoniu.nghmtaxi.db;

/**
 * Created by Administrator on 2017/2/21.
 */

public class LatLngInfo {
    private String orderId;
    private long addTime;
    private double lat, lng;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
